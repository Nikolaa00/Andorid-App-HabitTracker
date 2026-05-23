package com.example.habittrackerapp.domain.model

data class AppSettings(
    val userId: String,
    val isDarkMode: Boolean,
    val notificationsEnabled: Boolean,
    val preferredLanguage: String
)
