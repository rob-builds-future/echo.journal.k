package com.example.echojournal.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object AlarmScheduler {

    private const val DAILY_REQUEST_CODE = 1001
    private const val WEEKLY_REQUEST_CODE = 1002

    fun scheduleDailyReminder(
        context: Context,
        hour: Int,
        minute: Int,
        title: String,
        message: String
    ) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            // Wenn die Zeit heute schon vorbei ist, auf morgen verschieben
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.EXTRA_TITLE, title)
            putExtra(NotificationReceiver.EXTRA_MESSAGE, message)
        }

        val pending = PendingIntent.getBroadcast(
            context,
            DAILY_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Hier könnt ihr auf setExactAndAllowWhileIdle wechseln, wenn ihr exakte Alarme braucht
        mgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pending
        )
    }

    fun cancelDailyReminder(context: Context) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            context,
            DAILY_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pending)
    }

    fun scheduleWeeklyReminder(
        context: Context,
        dayOfWeek: Int,
        hour: Int,
        minute: Int,
        title: String,
        message: String
    ) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.EXTRA_TITLE, title)
            putExtra(NotificationReceiver.EXTRA_MESSAGE, message)
        }

        val pending = PendingIntent.getBroadcast(
            context,
            WEEKLY_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pending
        )
    }

    fun cancelWeeklyReminder(context: Context) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            context,
            WEEKLY_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pending)
    }

    fun scheduleOneShotTest(
        context: Context,
        delayMillis: Long,
        title: String,
        message: String
    ) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 1) Bei Android 12+ prüfen, ob wir "exact alarms" setzen dürfen:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Wenn keine Berechtigung, dann können wir hier entweder:
                // a) selbst eine SecurityException abfangen,
                // b) einfach zurückkehren und nicht planen,
                // c) oder den Nutzer per Intent in die Systemeinstellung leiten.
                return
            }
        }

        // 2) Intent für den Broadcast
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.EXTRA_TITLE, title)
            putExtra(NotificationReceiver.EXTRA_MESSAGE, message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = System.currentTimeMillis() + delayMillis

        // 3) Exakten Alarm setzen (bei Android 6+ mit setExactAndAllowWhileIdle, sonst setExact)
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } catch (e: SecurityException) {
            // Falls die Berechtigung fehlt, hier abfangen und ggf. loggen
            e.printStackTrace()
        }
    }
}