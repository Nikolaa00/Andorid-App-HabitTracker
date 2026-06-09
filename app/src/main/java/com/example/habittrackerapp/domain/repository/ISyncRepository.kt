package com.example.habittrackerapp.domain.repository

interface ISyncRepository {
    suspend fun pushLocalDataToFirestore(userId: String): Result<Unit>
    suspend fun pullFirestoreDataToLocal(userId: String): Result<Unit>
    suspend fun fullSync(userId: String): Result<Unit>
}
