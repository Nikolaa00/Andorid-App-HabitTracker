package com.example.habittrackerapp.data.repository

import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.data.local.dao.UserDao
import com.example.habittrackerapp.data.mapper.toDomain
import com.example.habittrackerapp.data.mapper.toEntity
import com.example.habittrackerapp.di.ApplicationScope
import com.example.habittrackerapp.domain.model.AppSettings
import com.example.habittrackerapp.domain.model.Habit
import com.example.habittrackerapp.domain.model.User
import com.example.habittrackerapp.domain.repository.ISyncRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth,
    private val syncRepository: ISyncRepository,
    @ApplicationScope private val externalScope: CoroutineScope
) {
    // Habit Operations
    fun getHabitsForUser(userId: String): Flow<List<Habit>> = 
        habitDao.getHabitsByUserId(userId).map { entities ->
            entities.map { it.toDomain() }
        }

    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getHabitById(id: Int): Habit? = habitDao.getHabitById(id)?.toDomain()

    suspend fun insertHabit(habit: Habit) {
        habitDao.insertHabit(habit.toEntity().copy(isSynced = false))
        triggerBackgroundSync(habit.userId)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity().copy(isSynced = false))
        triggerBackgroundSync(habit.userId)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toEntity())
        triggerBackgroundSync(habit.userId)
    }

    suspend fun deleteAllHabits() {
        val currentUserId = firebaseAuth.currentUser?.uid
        habitDao.deleteAllHabits()
        currentUserId?.let { triggerBackgroundSync(it) }
    }

    suspend fun claimGuestHabits(newUserId: String) {
        val allLocalHabits = habitDao.getAllHabits().first()
        allLocalHabits.forEach { entity ->
            if (entity.userId != newUserId) {
                habitDao.updateHabit(entity.copy(userId = newUserId, lastUpdated = System.currentTimeMillis(), isSynced = false))
            }
        }
        triggerBackgroundSync(newUserId)
    }

    private fun triggerBackgroundSync(userId: String) {
        externalScope.launch {
            syncRepository.pushLocalDataToFirestore(userId)
        }
    }

    // User & Session Operations
    private val _userSession = MutableStateFlow<User?>(null)
    val userSession: StateFlow<User?> = _userSession.asStateFlow()

    private val _isSessionLoaded = MutableStateFlow(false)
    val isSessionLoaded: StateFlow<Boolean> = _isSessionLoaded.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val currentUid = firebaseAuth.currentUser?.uid
            if (currentUid != null) {
                val user = userDao.getUserByIdSuspend(currentUid)
                _userSession.value = user?.toDomain()
            } else {
                _userSession.value = null
            }
            _isSessionLoaded.value = true
        }
    }

    fun getUser(uid: String): Flow<User?> = userDao.getUserById(uid).map { it?.toDomain() }

    suspend fun upsertUser(user: User) {
        userDao.upsertUser(user.toEntity())
        _userSession.value = user
    }

    fun getSettings(userId: String): Flow<AppSettings?> = 
        userDao.getSettingsByUserId(userId).map { it?.toDomain() }

    suspend fun upsertSettings(settings: AppSettings) = 
        userDao.upsertSettings(settings.toEntity())

    suspend fun clearSession() {
        _userSession.value = null
    }
}
