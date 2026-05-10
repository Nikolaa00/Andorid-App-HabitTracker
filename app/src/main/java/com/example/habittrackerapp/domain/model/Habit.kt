package com.example.habittrackerapp.domain.model

data class Habit(
    val id: String,
    val userId: String,
    val name: String,
    val description: String,
    val icon: String,
    val color: Long,
    val frequency: List<String>, // e.g., ["MONDAY", "WEDNESDAY"]
    val goalType: GoalType,
    val targetValue: Double,
    val unit: String,
    val reminders: List<String>, // e.g., ["08:00", "20:00"]
    val createdAt: Long
)
