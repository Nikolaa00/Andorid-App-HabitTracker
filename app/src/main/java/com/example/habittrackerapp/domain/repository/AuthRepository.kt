package com.example.habittrackerapp.domain.repository

import com.example.habittrackerapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun loginWithEmail(email: String, password: String): Result<User>
    suspend fun registerWithEmail(username: String, email: String, password: String): Result<User>
    suspend fun loginWithGoogle(idToken: String): Result<User>
    suspend fun loginWithFacebook(token: String): Result<User>
    suspend fun loginAnonymously(): Result<User>
    suspend fun logout()
}
