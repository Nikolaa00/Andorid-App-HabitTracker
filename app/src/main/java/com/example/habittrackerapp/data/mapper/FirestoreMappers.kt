package com.example.habittrackerapp.data.mapper

import com.example.habittrackerapp.data.local.entity.AppSettingsEntity
import com.example.habittrackerapp.data.local.entity.HabitEntity
import com.example.habittrackerapp.data.local.entity.UserEntity
import com.example.habittrackerapp.data.remote.dto.HabitFirestoreDto
import com.example.habittrackerapp.data.remote.dto.SettingsFirestoreDto
import com.example.habittrackerapp.data.remote.dto.UserFirestoreDto
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

fun HabitEntity.toFirestoreDto(): HabitFirestoreDto {
    return HabitFirestoreDto(
        id = id,
        userId = userId,
        name = name,
        description = description,
        frequency = frequency,
        dailyGoal = dailyGoal,
        currentProgress = currentProgress,
        reminders = reminders.map { it.format(timeFormatter) },
        lastUpdated = lastUpdated,
        createdAt = createdAt
    )
}

fun HabitFirestoreDto.toEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        userId = userId,
        name = name,
        description = description,
        frequency = frequency,
        dailyGoal = dailyGoal,
        currentProgress = currentProgress,
        reminders = reminders.map { LocalTime.parse(it, timeFormatter) },
        lastUpdated = lastUpdated,
        createdAt = createdAt
    )
}

fun UserEntity.toFirestoreDto(): UserFirestoreDto {
    return UserFirestoreDto(
        uid = uid,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        bio = bio,
        totalPoints = totalPoints,
        createdAt = createdAt
    )
}

fun UserFirestoreDto.toEntity(): UserEntity {
    return UserEntity(
        uid = uid,
        displayName = displayName,
        email = email,
        photoUrl = photoUrl,
        bio = bio,
        totalPoints = totalPoints,
        createdAt = createdAt
    )
}

fun AppSettingsEntity.toFirestoreDto(): SettingsFirestoreDto {
    return SettingsFirestoreDto(
        userId = userId,
        isDarkMode = isDarkMode,
        notificationsEnabled = notificationsEnabled,
        preferredLanguage = preferredLanguage
    )
}

fun SettingsFirestoreDto.toEntity(): AppSettingsEntity {
    return AppSettingsEntity(
        userId = userId,
        isDarkMode = isDarkMode,
        notificationsEnabled = notificationsEnabled,
        preferredLanguage = preferredLanguage
    )
}
