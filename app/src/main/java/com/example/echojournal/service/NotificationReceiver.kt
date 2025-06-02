package com.example.echojournal.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


/**
 * Dieser Receiver wird vom AlarmManager getriggert, wenn ein Reminder‐Alarm ablaufen soll.
 * Er ruft dann unseren EchoNotificationService auf, um die Notification anzuzeigen.
 */
class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Hier holen wir aus dem Intent womöglich ein Extra, um zu unterscheiden:
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Erinnerung"
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "Zeit für dein Echo‐Journal!"
        Log.d("NotificationReceiver", "onReceive: title=$title, message=$message")

        // NotificationService instanziieren und aufrufen
        val notificationService = EchoNotificationService(context)
        notificationService.showImportantMessageNotification(message)
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
    }
}