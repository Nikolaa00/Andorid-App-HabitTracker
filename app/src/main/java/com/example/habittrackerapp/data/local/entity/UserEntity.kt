package com.example.habittrackerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val bio: String?,
    val totalPoints: Int,
    val createdAt: String
)
