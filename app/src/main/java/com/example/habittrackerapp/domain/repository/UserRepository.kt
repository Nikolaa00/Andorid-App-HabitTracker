package com.example.habittrackerapp.domain.repository

import com.example.habittrackerapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(userId: String): Flow<User?>
    suspend fun updateUserProfile(user: User)
    suspend fun syncUserProfileWithRemote(userId: String)
}
