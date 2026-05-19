package com.example.habittrackerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey
    val userId: String,
    val isDarkMode: Boolean,
    val notificationsEnabled: Boolean,
    val preferredLanguage: String
)
