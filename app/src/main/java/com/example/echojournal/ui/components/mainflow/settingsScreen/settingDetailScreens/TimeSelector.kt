package com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime

@Composable
fun TimeSelector(
    initial: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    var showPicker by remember { mutableStateOf(false) }
    var pickedTime by remember { mutableStateOf(initial) }

    if (showPicker) {
        TimePickerDialog(
            context,
            { _: TimePicker, hour: Int, minute: Int ->
                val newTime = LocalTime.of(hour, minute)
                pickedTime = newTime
                onTimeSelected(newTime)
                showPicker = false
            },
            initial.hour,
            initial.minute,
            true
        ).show()
    }

    // Wir zeigen nur den Text; Klick öffnet den Dialog
    Text(
        text = pickedTime.toString().padStart(5, '0'),
        modifier = Modifier.clickable {
            showPicker = true
        }
    )
}