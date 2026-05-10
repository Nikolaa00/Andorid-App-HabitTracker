package com.example.habittrackerapp.domain.repository

import com.example.habittrackerapp.domain.model.Habit
import com.example.habittrackerapp.domain.model.HabitCompletion
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getHabits(): Flow<List<Habit>>
    fun getHabitById(id: String): Flow<Habit?>
    suspend fun insertHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)

    fun getCompletions(habitId: String): Flow<List<HabitCompletion>>
    fun getCompletionsForDate(date: String): Flow<List<HabitCompletion>>
    suspend fun upsertCompletion(completion: HabitCompletion)
    suspend fun deleteCompletion(completion: HabitCompletion)
    
    suspend fun syncWithRemote()
}
