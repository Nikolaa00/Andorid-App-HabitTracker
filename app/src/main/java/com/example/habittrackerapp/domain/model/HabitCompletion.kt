package com.example.habittrackerapp.domain.model

data class HabitCompletion(
    val id: String,
    val habitId: String,
    val date: String, // format "yyyy-MM-dd"
    val value: Double,
    val isCompleted: Boolean
)
