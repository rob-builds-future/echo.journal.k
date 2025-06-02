package com.example.echojournal.ui.screens.mainflow

import LanguageViewModel
import ProfileSettingLanguage
import ProfileSettingTheme
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
import com.example.echojournal.ui.components.mainflow.settingsScreen.SettingType
import com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens.ProfileSettingReminder
import com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens.ProfileSettingTemplate
import com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens.ProfileSettingUsername
import com.example.echojournal.ui.components.settingsScreen.settingDetailScreens.ProfileSettingInfo
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDetailScreen(
    type: SettingType,
    onBack: () -> Unit
) {

    // AuthViewModel holen
    val authViewModel: AuthViewModel = koinViewModel()
    val user by authViewModel.user.collectAsState()

    // PrefsViewModel holen, um aktuelle Sprache aus DataStore zu lesen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val currentLanguageCode by prefsViewModel.currentLanguage.collectAsState()

    // LanguageViewModel für die Liste aller Sprachen
    val languageViewModel: LanguageViewModel = koinViewModel()
    val allLanguages by languageViewModel.languages.collectAsState()

    // Formatierer für Datum
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
                    ProfileSettingUsername()
                }

                SettingType.TargetLanguage -> {
                    ProfileSettingLanguage()
                }

                SettingType.ProfileInfo -> {
                    val languageName = allLanguages
                        .firstOrNull { it.code == currentLanguageCode }
                        ?.name
                    // Fall‐Back, falls kein Name gefunden:
                        ?: if (currentLanguageCode.isBlank()) "–" else currentLanguageCode

                    ProfileSettingInfo(
                        memberSince     = memberSince,
                        username        = user?.username ?: "–",
                        language        = languageName,
                        onDeleteProfile = { /* … */ }
                    )
                }

                SettingType.Theme -> {
                    ProfileSettingTheme()
                }

                SettingType.Templates -> {
                    ProfileSettingTemplate()
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