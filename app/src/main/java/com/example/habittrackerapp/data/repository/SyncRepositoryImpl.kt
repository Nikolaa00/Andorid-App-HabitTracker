package com.example.habittrackerapp.data.repository

import android.util.Log
import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.data.local.dao.UserDao
import com.example.habittrackerapp.data.mapper.toEntity
import com.example.habittrackerapp.data.mapper.toFirestoreDto
import com.example.habittrackerapp.data.remote.FirestoreConstants
import com.example.habittrackerapp.data.remote.dto.HabitFirestoreDto
import com.example.habittrackerapp.data.remote.dto.SettingsFirestoreDto
import com.example.habittrackerapp.data.remote.dto.UserFirestoreDto
import com.example.habittrackerapp.di.ApplicationScope
import com.example.habittrackerapp.domain.repository.ISyncRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val userDao: UserDao,
    private val firestore: FirebaseFirestore,
    @ApplicationScope private val externalScope: CoroutineScope
) : ISyncRepository {

    override suspend fun pushLocalDataToFirestore(userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        internalPush(userId)
    }

    private suspend fun internalPush(userId: String): Result<Unit> = runCatching {
        Log.d("SyncDebug", "pushLocalDataToFirestore started for $userId")
        val user = userDao.getUserByIdSuspend(userId)
        val habits = habitDao.getHabitsByUserId(userId).first()
        val settings = userDao.getSettingsByUserId(userId).first()

        val batch = firestore.batch()

        // 1. Sync User Profile
        user?.let {
            val userRef = firestore.collection(FirestoreConstants.USERS_COLLECTION).document(userId)
            batch.set(userRef, it.toFirestoreDto(), SetOptions.merge())
        }

        // 2. Sync Settings
        settings?.let {
            val settingsRef = firestore.collection(FirestoreConstants.USERS_COLLECTION)
                .document(userId)
                .collection(FirestoreConstants.SETTINGS_SUB_COLLECTION)
                .document("app_settings")
            batch.set(settingsRef, it.toFirestoreDto(), SetOptions.merge())
        }

        // 3. Upsert Habits
        Log.d("SyncDebug", "Pushing ${habits.size} habits to Firestore")
        habits.forEach { habit ->
            val habitRef = firestore.collection(FirestoreConstants.USERS_COLLECTION)
                .document(userId)
                .collection(FirestoreConstants.HABITS_COLLECTION)
                .document(habit.id.toString())
            batch.set(habitRef, habit.toFirestoreDto(), SetOptions.merge())
        }

        batch.commit().await()
        Log.d("SyncDebug", "pushLocalDataToFirestore success")

        // Update local sync status
        habitDao.markHabitsAsSynced(habits.map { it.id })
    }.onFailure {
        if (it is CancellationException) throw it
        Log.e("SyncDebug", "pushLocalDataToFirestore failed", it)
    }

    override suspend fun pullFirestoreDataToLocal(userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            Log.d("SyncDebug", "pullFirestoreDataToLocal started for $userId")
        val userDoc = firestore.collection(FirestoreConstants.USERS_COLLECTION).document(userId).get().await()
        val settingsDoc = firestore.collection(FirestoreConstants.USERS_COLLECTION)
            .document(userId)
            .collection(FirestoreConstants.SETTINGS_SUB_COLLECTION)
            .document("app_settings")
            .get().await()
        val habitsSnapshot = firestore.collection(FirestoreConstants.USERS_COLLECTION)
            .document(userId)
            .collection(FirestoreConstants.HABITS_COLLECTION)
            .get().await()

        userDoc.toObject(UserFirestoreDto::class.java)?.let {
            userDao.upsertUser(it.toEntity())
        }

        settingsDoc.toObject(SettingsFirestoreDto::class.java)?.let {
            userDao.upsertSettings(it.toEntity())
        }

        val habits = habitsSnapshot.toObjects(HabitFirestoreDto::class.java)
        Log.d("SyncDebug", "Pulled ${habits.size} habits from Firestore")
        for (habitDto in habits) {
            habitDao.insertHabit(habitDto.toEntity())
        }
        Log.d("SyncDebug", "pullFirestoreDataToLocal success")
        Unit
    }.onFailure {
        if (it is CancellationException) throw it
        Log.e("SyncDebug", "pullFirestoreDataToLocal failed", it)
    }
}

    override suspend fun fullSync(userId: String): Result<Unit> {
        externalScope.launch {
            Log.d("SyncDebug", "fullSync (background) started for $userId")
            try {
                val firestoreHabits = firestore.collection(FirestoreConstants.USERS_COLLECTION)
                    .document(userId)
                    .collection(FirestoreConstants.HABITS_COLLECTION)
                    .get().await()
                    .toObjects(HabitFirestoreDto::class.java)

                val localHabits = habitDao.getHabitsByUserId(userId).first()

                for (remoteHabit in firestoreHabits) {
                    val localHabit = localHabits.find { it.id == remoteHabit.id }
                    if (localHabit == null || remoteHabit.lastUpdated > localHabit.lastUpdated) {
                        habitDao.insertHabit(remoteHabit.toEntity())
                    }
                }
                
                internalPush(userId).getOrThrow()
                Log.d("SyncDebug", "fullSync (background) success")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e("SyncDebug", "fullSync (background) failed", e)
            }
        }
        return Result.success(Unit)
    }
}
