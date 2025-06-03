package com.example.echojournal.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.echojournal.data.local.ReminderItem
import java.time.ZoneId

class ReminderSchedulerImpl (
    private val context: Context
): ReminderScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @RequiresApi(Build.VERSION_CODES.S)
    override fun schedule(item: ReminderItem) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", item.message)
        }

        if (alarmManager != null && alarmManager.canScheduleExactAlarms()) {
            // 2) Erlaubt: Terminieren
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        } else {
            // 3) Keine Erlaubnis: alternativ auf inexact ausweichen
            //    oder den Nutzer informieren, dass exakte Alarme nicht möglich sind.
            alarmManager?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                AlarmManager.INTERVAL_DAY, // Beispiel: täglicher Wiederholung
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    override fun cancel(item: ReminderItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, ReminderReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}