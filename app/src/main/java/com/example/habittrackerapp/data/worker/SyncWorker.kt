package com.example.habittrackerapp.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.domain.repository.ISyncRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val habitDao: HabitDao,
    private val syncRepository: ISyncRepository,
    private val firebaseAuth: FirebaseAuth
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val userId = firebaseAuth.currentUser?.uid ?: return Result.failure()
        
        Log.d("SyncWorker", "Starting background sync for $userId")
        
        return try {
            val unsyncedHabits = habitDao.getUnsyncedHabits(userId)
            if (unsyncedHabits.isNotEmpty()) {
                Log.d("SyncWorker", "Found ${unsyncedHabits.size} unsynced habits. Pushing...")
                syncRepository.pushLocalDataToFirestore(userId).getOrThrow()
                
                val ids = unsyncedHabits.map { it.id }
                habitDao.markHabitsAsSynced(ids)
                Log.d("SyncWorker", "Sync successful")
            } else {
                Log.d("SyncWorker", "No unsynced habits found")
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Sync failed", e)
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
