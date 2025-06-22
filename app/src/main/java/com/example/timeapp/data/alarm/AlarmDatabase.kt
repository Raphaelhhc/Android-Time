package com.example.timeapp.data.alarm

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Alarm::class],
    version = 1
)
@TypeConverters(LocalTimeConverter::class)
abstract class AlarmDatabase: RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}