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
import com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens.ReminderState
import com.example.echojournal.ui.components.settingsScreen.settingDetailScreens.ProfileSettingInfo
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalTime

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

    val savedRemindersMap by prefsViewModel.savedReminders.collectAsState()

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
                        ?: currentLanguageCode.ifBlank { "–" }

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
                    // Hier wandeln wir savedRemindersMap in Map<String, ReminderState> um:
                    val initialReminders: Map<String, ReminderState> =
                        savedRemindersMap
                            .map { (label, pair) ->
                                val (enabled, timeString) = pair
                                // parse Zeit-String in LocalTime
                                val lt = try {
                                    LocalTime.parse(timeString)
                                } catch (e: Exception) {
                                    LocalTime.of(9, 0)
                                }
                                // Default-Wochentag: nur, wenn es die Wochenzusammenfassung ist und enabled == true
                                val defaultDay = if (label == "Wochenzusammenfassung" && enabled) {
                                    DayOfWeek.MONDAY.value
                                } else {
                                    0
                                }
                                // Baue das ReminderState-Objekt zusammen
                                label to ReminderState(
                                    enabled   = enabled,
                                    time      = lt,
                                    dayOfWeek = defaultDay
                                )
                            }
                            .toMap()

                    ProfileSettingReminder(
                        initialReminders = initialReminders,
                        onChange = { label, enabled, time, dayOfWeek ->
                            // 3) Bei Änderung speichern wir sofort ins ViewModel
                            prefsViewModel.setReminderEnabled(label, enabled)
                            // LocalTime → String (HH:mm)
                            prefsViewModel.setReminderTime(label, time.toString())
                            // Wenn du dayOfWeek ebenfalls persistieren willst, müsstest du PrefsRepo erweitern
                            // um updateReminderDay(label, dayOfWeek).
                        }
                    )
                }
            }
        }
    }
}