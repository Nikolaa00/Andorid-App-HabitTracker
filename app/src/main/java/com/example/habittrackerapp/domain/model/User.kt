package com.example.habittrackerapp.domain.model

data class User(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val bio: String?,
    val totalPoints: Int,
    val createdAt: String
)
