package com.example.habittrackerapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.data.local.dao.UserDao
import com.example.habittrackerapp.data.local.entity.AppSettingsEntity
import com.example.habittrackerapp.data.local.entity.HabitEntity
import com.example.habittrackerapp.data.local.entity.UserEntity

@Database(
    entities = [
        HabitEntity::class,
        UserEntity::class,
        AppSettingsEntity::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao
    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "habit_tracker_db"
    }
}
