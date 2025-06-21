package com.example.timeapp.presentation.alarm

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
            // Schedule alarm
        }
    }

    fun deleteAlarm(id: String) {
        alarms.removeIf { it.id == id }
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
        // Schedule alarm
    }

    fun inactivateAlarm(id: String) {
        val idx = alarms.indexOfFirst { it.id == id }
        alarms[idx].activated = false
        // Cancel alarm
    }

    private fun scheduleAlarm() {
        //
    }

    private fun cancelAlarm() {
        //
    }

}