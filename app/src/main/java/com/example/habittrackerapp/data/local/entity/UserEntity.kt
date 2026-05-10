package com.example.habittrackerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String?,
    val streak: Int,
    val totalCompletions: Int,
    val memberSinceYear: Int,
    val isAnonymous: Boolean,
    val lastSync: Long = System.currentTimeMillis()
)
