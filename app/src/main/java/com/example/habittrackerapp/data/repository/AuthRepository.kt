package com.example.habittrackerapp.data.repository

import com.google.firebase.auth.AuthResult

interface AuthRepository {
    suspend fun signInAnonymously(): Result<AuthResult>
    suspend fun registerWithEmail(email: String, password: String): Result<AuthResult>
    suspend fun loginWithEmail(email: String, password: String): Result<AuthResult>
    suspend fun signInWithGoogle(idToken: String): Result<AuthResult>
    suspend fun signInWithFacebook(accessToken: String): Result<AuthResult>
    suspend fun updateDisplayName(name: String): Result<Unit>
    suspend fun logout()
    fun isUserLoggedIn(): Boolean
}
