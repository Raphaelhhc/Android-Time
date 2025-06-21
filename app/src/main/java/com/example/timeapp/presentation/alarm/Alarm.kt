package com.example.timeapp.presentation.alarm

import java.time.LocalTime

data class Alarm(
    val id: String,
    val createdAt: Long,
    val alarmTime: LocalTime,
    val activated: Boolean
)