package com.example.habittrackerapp.domain.model

data class DailyProgress(
    val date: String,
    val completedHabits: Int,
    val totalHabits: Int,
    val progress: Float
)
