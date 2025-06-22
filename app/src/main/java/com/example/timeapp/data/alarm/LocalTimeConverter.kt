package com.example.timeapp.data.alarm

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeConverter {
    @TypeConverter
    fun fromString(value: String?): LocalTime? = value?.let { LocalTime.parse(it) }

    @TypeConverter
    fun localTimeToString(time: LocalTime?): String? = time?.toString()
}