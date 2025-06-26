package com.rbf.echojournal.data.local

import java.time.LocalDateTime

/**
 * Wir kapseln für jeden Reminder:
 *  - enabled (Boolean)
 *  - time     (LocalTime)
 */
data class ReminderItem(
    //val enabled: Boolean,
    val time: LocalDateTime,
    val message: String
)