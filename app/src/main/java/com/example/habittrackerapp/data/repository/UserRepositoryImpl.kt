package com.example.habittrackerapp.data.repository

import com.example.habittrackerapp.data.local.dao.UserDao
import com.example.habittrackerapp.data.mapper.toDomain
import com.example.habittrackerapp.data.mapper.toEntity
import com.example.habittrackerapp.domain.model.User
import com.example.habittrackerapp.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firestore: FirebaseFirestore
) : UserRepository {

    override fun getUserProfile(userId: String): Flow<User?> {
        return userDao.getUserById(userId).map { it?.toDomain() }
    }

    override suspend fun updateUserProfile(user: User) {
        userDao.insertUser(user.toEntity())
        syncUserProfileWithRemote(user.id)
    }

    override suspend fun syncUserProfileWithRemote(userId: String) {
        try {
            val document = firestore.collection("users").document(userId).get().await()
            val user = document.toObject(User::class.java)
            if (user != null) {
                userDao.insertUser(user.toEntity())
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
}
