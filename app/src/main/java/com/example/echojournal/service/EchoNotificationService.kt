package com.example.echojournal.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class EchoNotificationService(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val existingChannel = notificationManager.getNotificationChannel("EchoNotificationChannel")
            if (existingChannel == null) {
                val channel = NotificationChannel(
                    "EchoNotificationChannel",
                    "Name des Channels",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Eine Beschreibung des Channels"
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    fun showImportantMessageNotification(message: String) {
        Log.d("EchoNotificationService", "Baue Notification mit Message: $message")
        val notification = NotificationCompat.Builder(context, "EchoNotificationChannel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Wichtige Benachrichtgung")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }


}