package com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/**
 * Dropdown, um einen Wochentag auszuwählen.
 * @param initialDay Der anfänglich gewählte Wochentag (1 = Montag, … , 7 = Sonntag).
 * @param onDaySelected Callback, wenn der Nutzer einen neuen Tag ausgewählt hat.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekdayDropdown(
    initialDay: Int,
    onDaySelected: (Int) -> Unit
) {
    // Erzeuge eine Liste von Wochentags-Paaren (DayOfWeek, Anzeigename).
    // Statt DayOfWeek.values() nutzen wir jetzt DayOfWeek.entries.
    val weekdays = remember {
        DayOfWeek.entries.map { dow ->
            dow to dow.getDisplayName(TextStyle.FULL, Locale.getDefault())
        }
    }

    // Interne States fürs Dropdown
    var expanded by remember { mutableStateOf(false) }
    var selectedDayOfWeek by remember { mutableIntStateOf(initialDay) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        // TextField, das den aktuellen Tag anzeigt und als Menü-Anker dient.
        TextField(
            readOnly = true,
            value = weekdays.first { it.first.value == selectedDayOfWeek }.second,
            onValueChange = { },
            modifier = Modifier
                .menuAnchor(
                    type = MenuAnchorType.PrimaryEditable,
                    enabled = true
                )
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            label = { Text("Wochentag") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            weekdays.forEach { (dow, displayName) ->
                DropdownMenuItem(
                    text = { Text(displayName) },
                    onClick = {
                        selectedDayOfWeek = dow.value
                        onDaySelected(dow.value)
                        expanded = false
                    }
                )
            }
        }
    }
}
