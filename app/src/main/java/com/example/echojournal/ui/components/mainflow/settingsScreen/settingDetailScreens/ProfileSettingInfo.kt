package com.example.echojournal.ui.components.settingsScreen.settingDetailScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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

    Column  {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                text = "Mitglied seit: $dummyMemberSince",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Benutzername: $dummyUsername",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(text = "Zielsprache: $dummyLanguage",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(text = "Farbschema: $dummyTheme",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(text = "Vorlagen: $dummyTemplates",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(text = "Erinnerungen: $dummyReminders",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .clickable { showDeleteDialog = true },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                text = "Profil Löschen",
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
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