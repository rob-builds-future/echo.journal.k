package com.example.echojournal.ui.components.settingsScreen.settingDetailScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


private const val dummyMemberSince = "01.01.2023"
private const val dummyUsername = "EchoUser"
private const val dummyLanguage = "Deutsch"
private const val dummyTheme = "Schwarz & Weiß"
private const val dummyTemplates = "3 Vorlagen"
private const val dummyReminders = "Aus"

/**
 * Profil-Info Screen: zeigt Mitglied seit und Zusammenfassung der Einstellungen,
 * sowie Button zum Löschen des Profils.
 *
 * @param onDeleteProfile Callback, wenn "Profil Löschen" bestätigt wird
 */
@Composable
fun ProfileSettingInfo(
    onDeleteProfile: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column {
        Text(text = "Mitglied seit: $dummyMemberSince")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Benutzername: $dummyUsername")
        Text(text = "Zielsprache: $dummyLanguage")
        Text(text = "Farbschema: $dummyTheme")
        Text(text = "Vorlagen: $dummyTemplates")
        Text(text = "Erinnerungen: $dummyReminders")
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Profil Löschen")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Profil löschen") },
            text = { Text("Möchtest du dein Profil wirklich löschen?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDeleteProfile()
                }) { Text("Ja") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Nein") }
            }
        )
    }
}