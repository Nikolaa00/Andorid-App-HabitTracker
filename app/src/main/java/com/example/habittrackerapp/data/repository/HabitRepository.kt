package com.example.habittrackerapp.data.repository

import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.data.local.dao.UserDao
import com.example.habittrackerapp.data.local.entity.AppSettingsEntity
import com.example.habittrackerapp.data.local.entity.HabitEntity
import com.example.habittrackerapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val userDao: UserDao
) {
    // Habit Operations
    val allHabits: Flow<List<HabitEntity>> = habitDao.getAllHabits()

    suspend fun getHabitById(id: Int): HabitEntity? = habitDao.getHabitById(id)

    suspend fun insertHabit(habit: HabitEntity) = habitDao.insertHabit(habit)

    suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)

    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)

    suspend fun deleteAllHabits() = habitDao.deleteAllHabits()

    // User & Session Operations
    private val _userSession = MutableStateFlow<UserEntity?>(null)
    val userSession: StateFlow<UserEntity?> = _userSession.asStateFlow()

    fun getUser(uid: String): Flow<UserEntity?> = userDao.getUserById(uid)

    suspend fun upsertUser(user: UserEntity) {
        userDao.upsertUser(user)
        _userSession.value = user
    }

    fun getSettings(userId: String): Flow<AppSettingsEntity?> = userDao.getSettingsByUserId(userId)

    suspend fun upsertSettings(settings: AppSettingsEntity) = userDao.upsertSettings(settings)

    suspend fun clearSession() {
        _userSession.value = null
    }
}
