package com.example.habittrackerapp.data.repository

import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.data.local.dao.UserDao
import com.example.habittrackerapp.data.mapper.toDomain
import com.example.habittrackerapp.data.mapper.toEntity
import com.example.habittrackerapp.data.notification.ReminderScheduler
import com.example.habittrackerapp.di.ApplicationScope
import com.example.habittrackerapp.domain.model.AppSettings
import com.example.habittrackerapp.domain.model.Habit
import com.example.habittrackerapp.domain.model.User
import com.example.habittrackerapp.domain.repository.ISyncRepository
import com.example.habittrackerapp.data.worker.SyncManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
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
    private val authRepository: AuthRepository,
    private val syncRepository: ISyncRepository,
    private val syncManager: SyncManager,
    private val reminderScheduler: ReminderScheduler,
    private val analytics: FirebaseAnalytics,
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
        val habitEntity = habit.toEntity().copy(isSynced = false)
        val id = habitDao.insertHabit(habitEntity)
        val insertedHabit = habit.copy(id = id.toInt())
        
        // Schedule reminders if enabled
        val settings = userDao.getSettingsByUserId(habit.userId).first()
        if (settings?.notificationsEnabled == true) {
            reminderScheduler.scheduleReminders(insertedHabit)
        }

        // Log analytics
        analytics.logEvent("habit_created", Bundle().apply {
            putString("habit_name", habit.name)
        })

        triggerBackgroundSync(habit.userId)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity().copy(isSynced = false))
        
        // Update reminders
        val settings = userDao.getSettingsByUserId(habit.userId).first()
        reminderScheduler.cancelReminders(habit) // Cancel old ones
        if (settings?.notificationsEnabled == true) {
            reminderScheduler.scheduleReminders(habit)
        }

        // Check if completed (progress reached goal)
        if (habit.currentProgress >= habit.dailyGoal) {
            analytics.logEvent("habit_completed", Bundle().apply {
                putString("habit_name", habit.name)
            })
        }

        triggerBackgroundSync(habit.userId)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toEntity())
        reminderScheduler.cancelReminders(habit)
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

    private var syncJob: kotlinx.coroutines.Job? = null
    private fun triggerBackgroundSync(userId: String) {
        // 1. Schedule WorkManager for reliable background synchronization
        syncManager.triggerOneTimeSync()

        // 2. Immediate direct sync with debounce to satisfy "instant" requirement
        // without flooding the network during bulk operations.
        syncJob?.cancel()
        syncJob = externalScope.launch {
            kotlinx.coroutines.delay(1000)
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
                analytics.setUserId(currentUid) // Set Analytics User ID
                authRepository.syncFCMToken()   // Sync FCM Token on app start
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
        triggerBackgroundSync(user.uid)
    }

    fun getSettings(userId: String): Flow<AppSettings?> = 
        userDao.getSettingsByUserId(userId).map { it?.toDomain() }

    suspend fun upsertSettings(settings: AppSettings) {
        userDao.upsertSettings(settings.toEntity())
        
        // Handle global notification toggle
        externalScope.launch {
            val habits = habitDao.getHabitsByUserId(settings.userId).first()
            habits.forEach { entity ->
                val habit = entity.toDomain()
                if (settings.notificationsEnabled) {
                    reminderScheduler.scheduleReminders(habit)
                } else {
                    reminderScheduler.cancelReminders(habit)
                }
            }
        }
        triggerBackgroundSync(settings.userId)
    }

    suspend fun clearSession() {
        _userSession.value = null
    }

    fun logStreakMilestone(userId: String, streak: Int) {
        analytics.logEvent("streak_milestone", Bundle().apply {
            putInt("streak_days", streak)
            putString("user_id", userId)
        })
    }
}
