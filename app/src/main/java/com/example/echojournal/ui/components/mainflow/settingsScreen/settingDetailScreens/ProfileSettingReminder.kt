package com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.local.ReminderItem
import com.example.echojournal.service.ReminderSchedulerImpl
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ProfileSettingReminder(
) {
    val context = LocalContext.current
    val scheduler = ReminderSchedulerImpl(context = context)

    var secondsText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var scheduledItem by remember { mutableStateOf<ReminderItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = secondsText,
            onValueChange = { secondsText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Trigger alarm in seconds")
            }
        )
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Message")
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                val delaySeconds = secondsText.toLongOrNull() ?: 0L
                val newItem = ReminderItem(
                    time    = LocalDateTime.now().plusSeconds(delaySeconds),
                    message = message
                )


                scheduledItem = newItem
                scheduler.schedule(newItem)

                secondsText = ""
                message = ""
            }) {
                Text(text = "Schedule")
            }
            Button(onClick = {
                scheduledItem?.let {
                    scheduler.cancel(it)
                    scheduledItem = null
                }
            }) {
                Text(text = "Cancel")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (scheduledItem != null) {
            Text(
                text = "Geplanter Alarm um: " +
                        "${scheduledItem!!.time.toLocalTime()} – \"${scheduledItem!!.message}\"",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Text(
                text = "Kein Alarm geplant.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
