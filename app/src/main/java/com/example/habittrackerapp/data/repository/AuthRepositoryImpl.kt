package com.example.habittrackerapp.data.repository

import android.content.Context
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.example.habittrackerapp.data.remote.FirestoreConstants
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val messaging: FirebaseMessaging,
    @ApplicationContext private val context: Context
) : AuthRepository {
    override suspend fun signInAnonymously(): Result<AuthResult> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            syncFCMToken()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(email: String, password: String): Result<AuthResult> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            syncFCMToken()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<AuthResult> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            syncFCMToken()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<AuthResult> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            syncFCMToken()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithFacebook(accessToken: String): Result<AuthResult> {
        return try {
            val credential = FacebookAuthProvider.getCredential(accessToken)
            val result = firebaseAuth.signInWithCredential(credential).await()
            syncFCMToken()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDisplayName(name: String): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user.updateProfile(profileUpdates).await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("No user logged in"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        try {
            // Sign out from Firebase
            firebaseAuth.signOut()
            
            // Clear Credential Manager state (Google / Passkeys)
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())

            // Sign out from Facebook
            LoginManager.getInstance().logOut()
        } catch (e: Exception) {
            // Log or handle error if necessary
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun syncFCMToken() {
        try {
            val userId = firebaseAuth.currentUser?.uid ?: return
            val token = messaging.token.await()
            
            firestore.collection(FirestoreConstants.USERS_COLLECTION)
                .document(userId)
                .update("fcmToken", token)
                .await()
        } catch (e: Exception) {
            // If document doesn't exist or update fails, we might need to set it
            // but usually users are created first.
            try {
                val userId = firebaseAuth.currentUser?.uid ?: return
                val token = messaging.token.await()
                val data = mapOf("fcmToken" to token)
                firestore.collection(FirestoreConstants.USERS_COLLECTION)
                    .document(userId)
                    .set(data, SetOptions.merge())
                    .await()
            } catch (inner: Exception) {
                android.util.Log.e("AuthRepo", "Failed to sync FCM token", inner)
            }
        }
    }
}
