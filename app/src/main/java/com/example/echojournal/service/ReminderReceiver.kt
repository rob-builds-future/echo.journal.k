package com.example.echojournal.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            println("Hello world, I'm booted up!")
        }
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        println("Alarm triggered: $message")
    }

}