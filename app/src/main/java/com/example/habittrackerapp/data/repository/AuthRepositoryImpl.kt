package com.example.habittrackerapp.data.repository

import com.example.habittrackerapp.domain.model.User
import com.example.habittrackerapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomainUser())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user?.toDomainUser()
            if (user != null) Result.success(user) else Result.failure(Exception("User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(username: String, email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user?.toDomainUser()
            if (user != null) Result.success(user) else Result.failure(Exception("User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return Result.failure(NotImplementedError("Google login implementation required"))
    }

    override suspend fun loginWithFacebook(token: String): Result<User> {
        return Result.failure(NotImplementedError("Facebook login implementation required"))
    }

    override suspend fun loginAnonymously(): Result<User> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            val user = result.user?.toDomainUser()
            if (user != null) Result.success(user) else Result.failure(Exception("User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    private fun FirebaseUser.toDomainUser(): User {
        return User(
            id = uid,
            name = displayName ?: "User",
            email = email ?: "",
            profilePictureUrl = photoUrl?.toString(),
            streak = 0,
            totalCompletions = 0,
            memberSinceYear = 2024,
            isAnonymous = isAnonymous
        )
    }
}
