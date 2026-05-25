package com.example.habittrackerapp.data.mapper

import com.example.habittrackerapp.data.local.entity.HabitEntity
import com.example.habittrackerapp.domain.model.Habit

fun HabitEntity.toDomain(): Habit {
    return Habit(
        id = id,
        userId = userId,
        name = name,
        description = description,
        frequency = frequency,
        dailyGoal = dailyGoal,
        currentProgress = currentProgress,
        reminders = reminders,
        lastUpdated = lastUpdated,
        createdAt = createdAt
    )
}

fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        userId = userId,
        name = name,
        description = description,
        frequency = frequency,
        dailyGoal = dailyGoal,
        currentProgress = currentProgress,
        reminders = reminders,
        lastUpdated = lastUpdated,
        createdAt = createdAt
    )
}
