package com.example.echojournal.util

import com.google.firebase.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(timestamp: Timestamp?): String {
    val dateFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMAN)
    return timestamp?.toDate()
        ?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.format(dateFormatter)
        ?: ""
}