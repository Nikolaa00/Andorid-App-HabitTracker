package com.example.habittrackerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_completions")
data class HabitCompletionEntity(
    @PrimaryKey val id: String,
    val habitId: String,
    val date: String, // "yyyy-MM-dd"
    val value: Double,
    val isCompleted: Boolean,
    val lastSync: Long = System.currentTimeMillis()
)
