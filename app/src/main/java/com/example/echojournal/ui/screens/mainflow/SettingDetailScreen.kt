package com.example.echojournal.ui.screens.mainflow

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens.ProfileSettingLanguage
import com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens.ProfileSettingReminder
import com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens.ProfileSettingTemplate
import com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens.ProfileSettingTheme
import com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens.ProfileSettingUsername
import com.example.echojournal.ui.components.settingsScreen.settingDetailScreens.ProfileSettingInfo
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.util.SettingType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDetailScreen(
    type: SettingType,
    onBack: () -> Unit
) {

    // 1) AuthViewModel holen
    val authViewModel: AuthViewModel = koinViewModel()
    val user by authViewModel.user.collectAsState()

    // 2) Formatierer für Datum
    val dateFormatter = remember {
        java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")
    }
    val memberSince = user
        ?.createdAt
        ?.toInstant()
        ?.atZone(java.time.ZoneId.systemDefault())
        ?.toLocalDate()
        ?.format(dateFormatter)
        ?: "–"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (type) {
                            SettingType.Username -> "Benutzername ändern"
                            SettingType.TargetLanguage -> "Zielsprache wählen"
                            SettingType.ProfileInfo -> "Profil Info"
                            SettingType.Theme -> "Echo-Farbe"
                            SettingType.Templates -> "Geführtes Tagebuchschreiben"
                            SettingType.Reminders -> "Erinnerungen"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Zurück"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (type) {
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
                        onSelect = { lang ->
                            // hier die Auswahl speichern, z.B. ViewModel.updateLanguage(lang)
                        }
                    )
                }

                SettingType.ProfileInfo -> {
                    ProfileSettingInfo(
                        memberSince     = memberSince,
                        username        = user?.username ?: "–",
                        language        = user?.preferredLanguage?.name ?: "–",
                        onDeleteProfile = { /* … */ }
                    )
                }

                SettingType.Theme -> {
                    ProfileSettingTheme(
                        initialColorName = "Wolkenlos",
                        onSelect = { theme ->
                            // hier die Auswahl speichern
                        }
                    )
                }

                SettingType.Templates -> {
                    ProfileSettingTemplate(
                        initialTemplateName = "Reflexion am Abend",
                        onSelect = { template ->
                            // hier die Vorlage speichern
                        }
                    )
                }

                SettingType.Reminders -> {
                    ProfileSettingReminder(
                        initialReminders = emptyMap(), // oder dein gespeichertes Map aus dem ViewModel
                        onChange = { label, enabled, time ->
                            // hier die Reminder speichern: label, enabled, time
                        }
                    )
                }
            }
        }
    }
}