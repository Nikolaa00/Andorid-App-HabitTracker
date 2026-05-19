package com.example.habittrackerapp.data.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Converters {
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }

    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() }
    }

    @TypeConverter
    fun fromLocalTimeList(value: List<LocalTime>?): String? {
        return value?.joinToString(",") { it.format(timeFormatter) }
    }

    @TypeConverter
    fun toLocalTimeList(value: String?): List<LocalTime>? {
        return value?.split(",")?.filter { it.isNotEmpty() }?.map { LocalTime.parse(it, timeFormatter) }
    }
}
