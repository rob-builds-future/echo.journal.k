package com.example.echojournal.service

import com.example.echojournal.data.local.ReminderItem


interface ReminderScheduler {
    fun schedule(item: ReminderItem)
    fun cancel(item: ReminderItem)
}