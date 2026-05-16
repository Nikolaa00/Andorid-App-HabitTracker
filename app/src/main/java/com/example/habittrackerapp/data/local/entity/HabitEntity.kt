package com.example.habittrackerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val frequencyDays: Int, // e.g., 7 for every day, 1 for once a week (or custom logic)
    val dailyGoalCount: Int,
    val completedCount: Int,
    val lastUpdatedTimestamp: Long
)
