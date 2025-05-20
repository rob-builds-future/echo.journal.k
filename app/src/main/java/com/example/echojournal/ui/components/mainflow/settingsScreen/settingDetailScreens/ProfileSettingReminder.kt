package com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalTime

/**
 * Screen zur Konfiguration von Erinnerungen.
 * Nutzer kann verschiedene Reminder aktivieren/deaktivieren und die Uhrzeit einstellen.
 *
 * @param initialReminders map mit Reminder-Namen und ihren Initialwerten (aktiv und Zeit)
 * @param onChange Callback, wenn sich eine Einstellung ändert
 */
@Composable
fun ProfileSettingReminder(
    initialReminders: Map<String, Pair<Boolean, LocalTime>>,
    onChange: (String, Boolean, LocalTime) -> Unit
) {
    // Lokale statische Config falls initialReminders leer
    val defaultReminders = listOf(
        "Tägliche Erinnerung" to (false to LocalTime.of(9, 0)),
        "Wochenzusammenfassung" to (false to LocalTime.of(18, 0))
    )

    // State pro Reminder: initial oder defaults
    val reminders = remember {
        val stateMap = mutableStateMapOf<String, Pair<Boolean, LocalTime>>()
        (initialReminders.ifEmpty { defaultReminders.toMap() }).forEach { (k, v) ->
            stateMap[k] = v
        }
        stateMap
    }

    Column {
        Text("Erinnerungen ein-/ausschalten.")
        Spacer(modifier = Modifier.height(16.dp))
        reminders.forEach { (label, config) ->
            var enabled by remember { mutableStateOf(config.first) }
            var time by remember { mutableStateOf(config.second) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = label)
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = time.toString(),
                        modifier = Modifier.clickable {
                            // TODO: open TimePicker-Dialog
                        }
                    )
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = {
                        enabled = it
                        reminders[label] = it to time
                        onChange(label, it, time)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                // Zurücksetzen auf Default-Werte
                defaultReminders.forEach { (k, v) ->
                    reminders[k] = v
                    onChange(k, v.first, v.second)
                }
            }) {
                Text("Zurücksetzen")
            }
            Spacer(modifier = Modifier.size(8.dp))
            Button(onClick = {
                // Speichern: hier ggf. alle Einstellungen übergeben
            }) {
                Text("Speichern")
            }
        }
    }
}
