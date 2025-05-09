package com.example.echojournal.ui.components.settingsScreen.settingDetailScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Text(text = "Hier kannst du deinen Benutzernamen ändern.")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Dein aktueller Benutzername: $initialUsername")
        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Neuer Benutzername") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSave(username)
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Speichern")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { Text("Dein Nutzername wurde geändert zu $username") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
