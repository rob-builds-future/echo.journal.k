package com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.echojournal.service.AlarmScheduler
import com.example.echojournal.service.NotificationPermissionRequester
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Wir kapseln für jeden Reminder:
 *  - enabled (Boolean)
 *  - time     (LocalTime)
 *  - weekday  (Int 1..7) nur relevant für "Wochenzusammenfassung"
 */
data class ReminderState(
    val enabled: Boolean,
    val time: LocalTime,
    val dayOfWeek: Int = 0
)

@Composable
fun ProfileSettingReminder(
    initialReminders: Map<String, ReminderState>,
    onChange: (label: String, enabled: Boolean, time: LocalTime, dayOfWeek: Int) -> Unit
) {
    val context = LocalContext.current

    // 1) State‐Map für alle Reminder
    val prefs = remember {
        mutableStateMapOf<String, ReminderState>().apply {
            if (initialReminders.isEmpty()) {
                // Default‐Konfigurationen:
                this["Tägliche Erinnerung"] = ReminderState(enabled = false, time = LocalTime.of(9, 0), dayOfWeek = 0)
                this["Wochenzusammenfassung"] = ReminderState(enabled = false, time = LocalTime.of(18, 0), dayOfWeek = DayOfWeek.MONDAY.value)
            } else {
                this.putAll(initialReminders)
            }
        }
    }

    // 2) Welches Label wartet auf Notification-Permission?
    var permissionToRequestLabel by remember { mutableStateOf<String?>(null) }

    // 3) Wird aufgerufen, sobald POST_NOTIFICATIONS genehmigt wurde:
    val onGranted: () -> Unit = {
        permissionToRequestLabel?.let { label ->
            val state = prefs[label]!!
            if (state.enabled) {
                if (label == "Tägliche Erinnerung") {
                    AlarmScheduler.scheduleDailyReminder(
                        context,
                        state.time.hour,
                        state.time.minute,
                        title   = "Tägliche Erinnerung",
                        message = "Zeit, dein Echo‐Journal auszufüllen!"
                    )
                } else if (label == "Wochenzusammenfassung") {
                    AlarmScheduler.scheduleWeeklyReminder(
                        context,
                        dayOfWeek = state.dayOfWeek,
                        hour      = state.time.hour,
                        minute    = state.time.minute,
                        title     = "Wochenzusammenfassung",
                        message   = "Hier ist deine Wochenstatistik!"
                    )
                }
            }
            permissionToRequestLabel = null
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Erinnerungen ein-/ausschalten.", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        prefs.forEach { (label, config) ->
            // Lokale States fürs UI-Widget
            var enabled by remember { mutableStateOf(config.enabled) }
            var time by remember { mutableStateOf(config.time) }
            var weekday by remember { mutableIntStateOf(config.dayOfWeek) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = label, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(4.dp))

                    // Wenn Wochenzusammenfassung, zusätzlich Wochentag‐Dropdown
                    if (label == "Wochenzusammenfassung") {
                        WeekdayDropdown(
                            initialDay = weekday,
                            onDaySelected = { newDay ->
                                weekday = newDay
                                prefs[label] = config.copy(dayOfWeek = newDay)
                                onChange(label, enabled, time, newDay)
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    // Zeit-Auswahl (für beide Remindertypen)
                    TimeSelector(
                        initial = time,
                        onTimeSelected = { newTime ->
                            time = newTime
                            prefs[label] = config.copy(time = newTime)
                            onChange(label, enabled, time, weekday)
                        }
                    )
                }
                // Switch zum An/Aus der Erinnerung
                Switch(
                    checked = enabled,
                    onCheckedChange = { checked ->
                        enabled = checked
                        prefs[label] = config.copy(enabled = checked)
                        onChange(label, checked, time, weekday)

                        if (checked) {
                            // Switch auf „an“ → merken, dass wir die Permission brauchen
                            permissionToRequestLabel = label
                        } else {
                            // Switch auf „aus“ → Alarm abbrechen
                            when (label) {
                                "Tägliche Erinnerung" -> AlarmScheduler.cancelDailyReminder(context)
                                "Wochenzusammenfassung" -> AlarmScheduler.cancelWeeklyReminder(context)
                            }
                        }
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            // Ein One‐Shot‐Test‐Alarm in 10 Sekunden
            AlarmScheduler.scheduleOneShotTest(
                context   = context,
                delayMillis = 10_000L,
                title     = "Test-Erinnerung",
                message   = "Nach 10 Sekunden!"
            )
        }) {
            Text("Test-Alarm in 10 Sekunden")
        }
    }

    // 5) Ganz unten binden wir einmal die Permission-Komponente ein.
    NotificationPermissionRequester(onPermissionGranted = onGranted)
}