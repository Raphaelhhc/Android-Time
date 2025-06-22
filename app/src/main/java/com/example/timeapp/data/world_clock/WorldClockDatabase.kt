package com.example.timeapp.data.world_clock

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WorldClockCity::class],
    version = 1,
    exportSchema = false
)
abstract class WorldClockDatabase : RoomDatabase() {
    abstract fun worldClockDao(): WorldClockDao
}