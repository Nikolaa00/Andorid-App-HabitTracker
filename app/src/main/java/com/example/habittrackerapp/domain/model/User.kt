package com.example.habittrackerapp.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String?,
    val streak: Int,
    val totalCompletions: Int,
    val memberSinceYear: Int,
    val isAnonymous: Boolean
)
