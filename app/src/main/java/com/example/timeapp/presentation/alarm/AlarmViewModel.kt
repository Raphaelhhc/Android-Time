package com.example.timeapp.presentation.alarm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeapp.data.alarm.Alarm
import com.example.timeapp.data.alarm.AlarmDao
import com.example.timeapp.presentation.alarm.notification.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmScheduler: AlarmScheduler,
    private val alarmDao: AlarmDao
) : ViewModel() {

    val alarms = mutableStateListOf<Alarm>()

    init {
        viewModelScope.launch {
            alarmDao.getAllAlarmsFlow().collectLatest { stored ->
                updateAlarms(stored)
            }
        }
    }

    private fun updateAlarms(stored: List<Alarm>) {
        alarms.clear()
        stored.forEach { alarms.add(it) }
    }

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
        viewModelScope.launch {
            alarmDao.insert(newAlarm)
        }

        if (newAlarm.activated) {
            scheduleAlarm(newAlarm)
        }
    }

    fun deleteAlarm(id: String) {
        val alarm = alarms.firstOrNull { it.id == id }
        if (alarm != null) {
            if (alarm.activated) cancelAlarm(alarm)
            viewModelScope.launch {
                alarmDao.delete(alarm.id)
            }
        }
    }

    fun editAlarm(
        id: String,
        alarmTime: LocalTime,
    ) {
        val idx = alarms.indexOfFirst { it.id == id }
        if (idx >= 0) {
            val updated = alarms[idx].copy(alarmTime = alarmTime)
            alarms[idx] = updated
        }
    }

    fun activateAlarm(id: String) {
        val idx = alarms.indexOfFirst { it.id == id }
        if (idx >= 0) {
            val updated = alarms[idx].copy(activated = true)
            alarms[idx] = updated
            scheduleAlarm(alarms[idx])
        }
    }

    fun inactivateAlarm(id: String) {
        val idx = alarms.indexOfFirst { it.id == id }
        if (idx >= 0) {
            val updated = alarms[idx].copy(activated = false)
            alarms[idx] = updated
            cancelAlarm(updated)
        }
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