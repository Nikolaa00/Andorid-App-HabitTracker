package com.example.habittrackerapp.data.repository

import com.google.firebase.auth.AuthResult

interface AuthRepository {
    suspend fun signInAnonymously(): Result<AuthResult>
}
