package com.example.habittrackerapp.data.repository

import com.google.firebase.auth.AuthResult

interface AuthRepository {
    suspend fun signInAnonymously(): Result<AuthResult>
    suspend fun registerWithEmail(email: String, password: String): Result<AuthResult>
    suspend fun loginWithEmail(email: String, password: String): Result<AuthResult>
    suspend fun updateDisplayName(name: String): Result<Unit>
    fun logout()
    fun isUserLoggedIn(): Boolean
}
