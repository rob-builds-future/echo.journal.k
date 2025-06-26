package com.rbf.echojournal.util

import com.google.firebase.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Formatiert den Firestore‐Timestamp in das Locale‐abhängige, lesbare Datum.
 * Wenn keine Locale übergeben wird, wird die System‐Locale verwendet.
 */
fun formatDate(
    timestamp: Timestamp?,
    locale: Locale = Locale.getDefault()    // Standard: System‐Locale
): String {
    if (timestamp == null) return ""

    // Nutze einen vordefinierten Stil, statt eines harten Patterns:
    val dateFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.LONG)    // z.B. "6. Juni 2025" / "June 6, 2025" / "6 de junho de 2025"
        .withLocale(locale)

    return timestamp.toDate()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter)
}
