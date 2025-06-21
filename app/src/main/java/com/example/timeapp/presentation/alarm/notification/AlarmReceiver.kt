package com.example.timeapp.presentation.alarm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.timeapp.R

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        // Channel Id
        val channelId = "alarm_channel"

        // Notification Manager
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel
        if (notificationManager.getNotificationChannel(channelId) == null) {

            val channel = NotificationChannel(
                channelId,
                "Alarm",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                    null
                )
            }

            notificationManager.createNotificationChannel(channel)
        }

        // Build notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Alarm")
            .setContentText("It's Time!")
            .setSmallIcon(R.drawable.ic_alarm)
            .setAutoCancel(true)
            .build()

        // Notify
        notificationManager.notify(1, notification)

    }
}