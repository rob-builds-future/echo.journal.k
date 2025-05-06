package com.example.echojournal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.SettingsScreen.SettingItem
import com.example.echojournal.util.SettingType

// Dummy user data
private const val dummyUsername = "EchoUser"
private const val dummyMemberSince = "01.01.2023"
private const val dummyTargetLanguage = "Deutsch"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToProfile: (SettingType) -> Unit = {},
    onNavigateToAppSetting: (SettingType) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Einstellungen", style = MaterialTheme.typography.titleMedium) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profileinstellungen Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column {
                    SettingItem(
                        label = "Benutzername",
                        value = dummyUsername,
                        onClick = { onNavigateToProfile(SettingType.Username) }
                    )
                    HorizontalDivider()
                    SettingItem(
                        label = "Zielsprache",
                        value = dummyTargetLanguage,
                        onClick = { onNavigateToProfile(SettingType.TargetLanguage) }
                    )
                    HorizontalDivider()
                    SettingItem(
                        label = "Mitglied seit",
                        value = dummyMemberSince,
                        onClick = { onNavigateToProfile(SettingType.MemberSince) }
                    )
                }
            }

            // App Einstellungen Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column {
                    SettingItem(
                        label = "Farbschema",
                        value = "Schwarz & Weiß",
                        onClick = { onNavigateToAppSetting(SettingType.Theme) }
                    )
                    HorizontalDivider()
                    SettingItem(
                        label = "Journaling Vorlagen",
                        value = "3 Vorlagen",
                        onClick = { onNavigateToAppSetting(SettingType.Templates) }
                    )
                    HorizontalDivider()
                    SettingItem(
                        label = "Erinnerungen",
                        value = "Aus",
                        onClick = { onNavigateToAppSetting(SettingType.Reminders) }
                    )
                }
            }
        }
    }
}