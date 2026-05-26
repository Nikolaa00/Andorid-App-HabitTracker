package com.example.habittrackerapp.domain.model

import java.time.LocalTime

data class Habit(
    val id: Int = 0,
    val userId: String, // Owner ID
    val name: String,
    val description: String,
    val frequency: List<Int>,
    val dailyGoal: Int,
    val currentProgress: Int,
    val reminders: List<LocalTime>,
    val lastUpdated: Long,
    val createdAt: String
)
