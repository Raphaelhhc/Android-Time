package com.example.timeapp.presentation.alarm.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmScheduler @Inject constructor(private  val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            context.startActivity(intent)
        }
    }

    fun schedule(alarmTime: LocalTime, id: String) {

        val intent = Intent(context, AlarmReceiver::class.java)

        val pending = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        var alarmDateTime = LocalDateTime.of(LocalDate.now(), alarmTime)
        Log.d("SCHEDULE", "alarmDateTime: $alarmDateTime")
        val nowDateTime = LocalDateTime.now()
        Log.d("SCHEDULE", "nowDatetime: $nowDateTime")
        if (!alarmDateTime.isAfter(nowDateTime)) {
            alarmDateTime = alarmDateTime.plusDays(1)
        }
        val triggerAt = alarmDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        Log.d("SCHEDULE", "triggerAt: $triggerAt")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pending
        )

    }

    fun cancel(id: String) {

        val intent = Intent(context, AlarmReceiver::class.java)

        val pending = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pending)

    }

}