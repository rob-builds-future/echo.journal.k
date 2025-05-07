package com.example.echojournal.ui.components.settingsScreen.settingDetailScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Screen zum Ändern des Benutzernamens.
 * @param initialUsername aktueller Benutzername zum Vorbefüllen
 * @param onSave Callback mit neuem Benutzernamen
 */
@Composable
fun ProfileSettingUsername(
    initialUsername: String,
    onSave: (String) -> Unit,
) {
    var username by remember { mutableStateOf(initialUsername) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Neuer Benutzername") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSave(username) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Speichern")
        }
    }
}
