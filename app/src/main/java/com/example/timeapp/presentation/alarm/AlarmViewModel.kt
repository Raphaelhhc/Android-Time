package com.example.timeapp.presentation.alarm

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.timeapp.presentation.alarm.notification.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    val alarms = mutableStateListOf<Alarm>()

    fun addNewAlarm(
        alarmTime: LocalTime,
        activated: Boolean
    ) {
        val id = UUID.randomUUID().toString()
        val newAlarm = Alarm(
            id = id,
            createdAt = System.currentTimeMillis(),
            alarmTime = alarmTime,
            activated = activated
        )
        alarms.add(newAlarm)

        if (newAlarm.activated) {
            scheduleAlarm(newAlarm)
        }
    }

    fun deleteAlarm(id: String) {
        val alarm = alarms.firstOrNull { it.id == id }
        if (alarm != null) {
            if (alarm.activated) cancelAlarm(alarm)
            alarms.remove(alarm)
        }
    }

    fun editAlarm(
        id: String,
        alarmTime: LocalTime,
    ) {
        val idx = alarms.indexOfFirst { it.id == id }
        alarms[idx].alarmTime = alarmTime
    }

    fun activateAlarm(id: String) {
        val idx = alarms.indexOfFirst { it.id == id }
        alarms[idx].activated = true
        scheduleAlarm(alarms[idx])
    }

    fun inactivateAlarm(id: String) {
        val idx = alarms.indexOfFirst { it.id == id }
        alarms[idx].activated = false
        cancelAlarm(alarms[idx])
    }

    private fun scheduleAlarm(alarm: Alarm) {
        if (alarmScheduler.canScheduleExactAlarms()) {
            alarmScheduler.schedule(alarm.alarmTime, alarm.id)
        } else {
            alarmScheduler.requestExactAlarmPermission()
        }
    }

    private fun cancelAlarm(alarm: Alarm) {
        alarmScheduler.cancel(alarm.id)
    }

}