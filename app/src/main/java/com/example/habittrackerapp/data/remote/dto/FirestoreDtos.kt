package com.example.habittrackerapp.data.remote.dto

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class HabitFirestoreDto(
    val id: Int = 0,
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val frequency: List<Int> = emptyList(),
    val dailyGoal: Int = 0,
    val currentProgress: Int = 0,
    val reminders: List<String> = emptyList(), // ISO-8601 strings
    val lastUpdated: Long = 0,
    val createdAt: String = ""
)

@IgnoreExtraProperties
data class UserFirestoreDto(
    val uid: String = "",
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val bio: String? = null,
    val totalPoints: Int = 0,
    val createdAt: String = ""
)

@IgnoreExtraProperties
data class SettingsFirestoreDto(
    val userId: String = "",
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val preferredLanguage: String = "en"
)
