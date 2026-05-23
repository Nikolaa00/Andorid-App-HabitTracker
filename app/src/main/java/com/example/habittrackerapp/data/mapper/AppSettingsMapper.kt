package com.example.habittrackerapp.data.mapper

import com.example.habittrackerapp.data.local.entity.AppSettingsEntity
import com.example.habittrackerapp.domain.model.AppSettings

fun AppSettingsEntity.toDomain(): AppSettings {
    return AppSettings(
        userId = userId,
        isDarkMode = isDarkMode,
        notificationsEnabled = notificationsEnabled,
        preferredLanguage = preferredLanguage
    )
}

fun AppSettings.toEntity(): AppSettingsEntity {
    return AppSettingsEntity(
        userId = userId,
        isDarkMode = isDarkMode,
        notificationsEnabled = notificationsEnabled,
        preferredLanguage = preferredLanguage
    )
}
