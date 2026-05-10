package com.example.habittrackerapp.data.local

import androidx.room.TypeConverter
import com.example.habittrackerapp.domain.model.GoalType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromGoalType(value: GoalType): String {
        return value.name
    }

    @TypeConverter
    fun toGoalType(value: String): GoalType {
        return GoalType.valueOf(value)
    }
}
