package com.example.habittrackerapp.data.mapper

import com.example.habittrackerapp.data.local.entity.HabitEntity
import com.example.habittrackerapp.domain.model.Habit

fun HabitEntity.toDomain(): Habit {
    return Habit(
        id = id,
        name = name,
        description = description,
        frequency = frequency,
        dailyGoal = dailyGoal,
        currentProgress = currentProgress,
        reminders = reminders,
        lastUpdated = lastUpdated
    )
}

fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        name = name,
        description = description,
        frequency = frequency,
        dailyGoal = dailyGoal,
        currentProgress = currentProgress,
        reminders = reminders,
        lastUpdated = lastUpdated
    )
}
