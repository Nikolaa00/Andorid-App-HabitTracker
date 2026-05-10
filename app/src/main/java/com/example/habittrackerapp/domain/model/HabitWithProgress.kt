package com.example.habittrackerapp.domain.model

data class HabitWithProgress(
    val habit: Habit,
    val currentValue: Double,
    val isCompleted: Boolean
)
