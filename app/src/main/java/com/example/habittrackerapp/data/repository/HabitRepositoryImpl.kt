package com.example.habittrackerapp.data.repository

import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.data.mapper.toDomain
import com.example.habittrackerapp.data.mapper.toEntity
import com.example.habittrackerapp.domain.model.Habit
import com.example.habittrackerapp.domain.model.HabitCompletion
import com.example.habittrackerapp.domain.repository.HabitRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val firestore: FirebaseFirestore
) : HabitRepository {

    override fun getHabits(): Flow<List<Habit>> {
        return habitDao.getHabits().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getHabitById(id: String): Flow<Habit?> {
        return habitDao.getHabitById(id).map { it?.toDomain() }
    }

    override suspend fun insertHabit(habit: Habit) {
        habitDao.insertHabit(habit.toEntity())
        syncHabitToRemote(habit)
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity())
        syncHabitToRemote(habit)
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toEntity())
        deleteHabitFromRemote(habit.id)
    }

    override fun getCompletions(habitId: String): Flow<List<HabitCompletion>> {
        return habitDao.getCompletions(habitId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCompletionsForDate(date: String): Flow<List<HabitCompletion>> {
        return habitDao.getCompletionsForDate(date).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun upsertCompletion(completion: HabitCompletion) {
        habitDao.upsertCompletion(completion.toEntity())
        syncCompletionToRemote(completion)
    }

    override suspend fun deleteCompletion(completion: HabitCompletion) {
        habitDao.deleteCompletion(completion.toEntity())
        deleteCompletionFromRemote(completion.id)
    }

    override suspend fun syncWithRemote() {
        // Implementation for full sync between Room and Firestore
    }

    private suspend fun syncHabitToRemote(habit: Habit) {
        try {
            firestore.collection("habits").document(habit.id).set(habit).await()
        } catch (e: Exception) {
            // Handle sync failure
        }
    }

    private suspend fun deleteHabitFromRemote(habitId: String) {
        try {
            firestore.collection("habits").document(habitId).delete().await()
        } catch (e: Exception) {
            // Handle sync failure
        }
    }

    private suspend fun syncCompletionToRemote(completion: HabitCompletion) {
        try {
            firestore.collection("completions").document(completion.id).set(completion).await()
        } catch (e: Exception) {
            // Handle sync failure
        }
    }

    private suspend fun deleteCompletionFromRemote(completionId: String) {
        try {
            firestore.collection("completions").document(completionId).delete().await()
        } catch (e: Exception) {
            // Handle sync failure
        }
    }
}
