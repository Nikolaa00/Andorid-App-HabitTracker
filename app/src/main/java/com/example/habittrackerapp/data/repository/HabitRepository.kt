package com.example.habittrackerapp.data.repository

import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.data.local.dao.UserDao
import com.example.habittrackerapp.data.mapper.toDomain
import com.example.habittrackerapp.data.mapper.toEntity
import com.example.habittrackerapp.domain.model.AppSettings
import com.example.habittrackerapp.domain.model.Habit
import com.example.habittrackerapp.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val userDao: UserDao
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

    suspend fun insertHabit(habit: Habit) = habitDao.insertHabit(habit.toEntity())

    suspend fun updateHabit(habit: Habit) = habitDao.updateHabit(habit.toEntity())

    suspend fun deleteHabit(habit: Habit) = habitDao.deleteHabit(habit.toEntity())

    suspend fun deleteAllHabits() = habitDao.deleteAllHabits()

    // User & Session Operations
    private val _userSession = MutableStateFlow<User?>(null)
    val userSession: StateFlow<User?> = _userSession.asStateFlow()

    private val _isSessionLoaded = MutableStateFlow(false)
    val isSessionLoaded: StateFlow<Boolean> = _isSessionLoaded.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val lastUser = userDao.getAllUsers().first().firstOrNull()
            _userSession.value = lastUser?.toDomain()
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
