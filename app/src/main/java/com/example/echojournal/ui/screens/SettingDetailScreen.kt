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
import com.example.echojournal.ui.components.settingsScreen.settingDetailScreens.ProfileSettingInfo
import com.example.echojournal.ui.components.settingsScreen.settingDetailScreens.ProfileSettingLanguage
import com.example.echojournal.ui.components.settingsScreen.settingDetailScreens.ProfileSettingUsername
import com.example.echojournal.util.SettingType

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
                    SettingType.ProfileInfo    -> "Profil Info"
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
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            when(type) {
                SettingType.Username -> {
                    ProfileSettingUsername(
                        initialUsername = "EchoUser",
                        onSave = { newName ->
                            // hier UserStore.updateName(newName) o.ä.
                        }
                    )
                }
                SettingType.TargetLanguage -> {
                    ProfileSettingLanguage(
                        initialLanguage = "English",
                        onSelect = {  lang ->
                            // hier die Auswahl speichern, z.B. ViewModel.updateLanguage(lang)
                        }
                    )
                }
                SettingType.ProfileInfo -> {
                    ProfileSettingInfo(
                        onDeleteProfile = {
                            // hier das Profil löschen, über das viewmodel
                        }
                    )
                }
                SettingType.Theme -> {
                    Text("Echo-Farbe wechseln:")
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