package com.example.timeapp.data.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "alarm")
data class Alarm(
    @PrimaryKey val id: String,
    val createdAt: Long,
    val alarmTime: LocalTime,
    val activated: Boolean
)