package com.example.habittrackerapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.habittrackerapp.domain.model.GoalType

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val description: String,
    val icon: String,
    val color: Long,
    val frequency: List<String>,
    val goalType: GoalType,
    val targetValue: Double,
    val unit: String,
    val reminders: List<String>,
    val createdAt: Long,
    val lastSync: Long = System.currentTimeMillis()
)
