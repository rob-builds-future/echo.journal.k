package com.example.echojournal.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.SettingsScreen.SettingDetailScreens.ProfileSettingUsername
import com.example.echojournal.util.SettingType

private const val dummyMemberSince = "01.01.2023"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDetailScreen(
    type: SettingType,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = when(type) {
                    SettingType.Username       -> "Benutzername ändern"
                    SettingType.TargetLanguage -> "Zielsprache wählen"
                    SettingType.MemberSince    -> "Mitglied seit anzeigen"
                    SettingType.Theme          -> "Farbschema"
                    SettingType.Templates      -> "Journaling-Vorlagen"
                    SettingType.Reminders      -> "Erinnerungen"
                }) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            when(type) {
                SettingType.Username -> {
                    Text("Hier kannst du deinen Benutzernamen ändern.")
                    ProfileSettingUsername(
                        onSave = { newName ->
                            // hier UserStore.updateName(newName) o.ä.
                        },
                        initialUsername = "EchoUser",
                    )
                }
                SettingType.TargetLanguage -> {
                    Text("Wähle deine Zielsprache aus.")
                    // Dropdown, RadioButtons …
                }
                SettingType.MemberSince -> {
                    Text("Du bist seit $dummyMemberSince Mitglied.")
                }
                SettingType.Theme -> {
                    Text("Theme wechseln: Hell, Dunkel, Automatisch.")
                }
                SettingType.Templates -> {
                    Text("Deine Journaling-Vorlagen verwalten.")
                }
                SettingType.Reminders -> {
                    Text("Erinnerungen ein-/ausschalten.")
                }
            }
        }
    }
}