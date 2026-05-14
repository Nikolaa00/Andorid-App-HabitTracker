package com.example.habittrackerapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.data.local.entity.HabitEntity

@Database(entities = [HabitEntity::class], version = 1, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao

    companion object {
        const val DATABASE_NAME = "habit_tracker_db"
    }
}
