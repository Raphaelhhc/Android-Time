package com.example.timeapp.presentation.alarm

import java.time.LocalTime

data class Alarm(
    val id: String,
    val createdAt: Long,
    var alarmTime: LocalTime,
    var activated: Boolean
)