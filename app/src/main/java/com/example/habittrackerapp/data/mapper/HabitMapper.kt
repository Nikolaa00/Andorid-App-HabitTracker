package com.example.habittrackerapp.data.mapper

import com.example.habittrackerapp.data.local.entity.HabitCompletionEntity
import com.example.habittrackerapp.data.local.entity.HabitEntity
import com.example.habittrackerapp.domain.model.Habit
import com.example.habittrackerapp.domain.model.HabitCompletion

fun HabitEntity.toDomain(): Habit {
    return Habit(
        id = id,
        userId = userId,
        name = name,
        description = description,
        icon = icon,
        color = color,
        frequency = frequency,
        goalType = goalType,
        targetValue = targetValue,
        unit = unit,
        reminders = reminders,
        createdAt = createdAt
    )
}

fun Habit.toEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        userId = userId,
        name = name,
        description = description,
        icon = icon,
        color = color,
        frequency = frequency,
        goalType = goalType,
        targetValue = targetValue,
        unit = unit,
        reminders = reminders,
        createdAt = createdAt
    )
}

fun HabitCompletionEntity.toDomain(): HabitCompletion {
    return HabitCompletion(
        id = id,
        habitId = habitId,
        date = date,
        value = value,
        isCompleted = isCompleted
    )
}

fun HabitCompletion.toEntity(): HabitCompletionEntity {
    return HabitCompletionEntity(
        id = id,
        habitId = habitId,
        date = date,
        value = value,
        isCompleted = isCompleted
    )
}
