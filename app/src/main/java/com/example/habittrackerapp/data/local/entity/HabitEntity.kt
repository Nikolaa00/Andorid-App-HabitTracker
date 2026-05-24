package com.example.habittrackerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val frequency: List<Int>, // e.g., List of days of the week (1-7)
    val dailyGoal: Int,
    val currentProgress: Int,
    val reminders: List<LocalTime>,
    val lastUpdated: Long,
    val createdAt: String
)
