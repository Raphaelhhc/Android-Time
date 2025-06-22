package com.example.timeapp.data.world_clock

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "world_clock_cities")
data class WorldClockCity(
    @PrimaryKey val zoneId: String
)
