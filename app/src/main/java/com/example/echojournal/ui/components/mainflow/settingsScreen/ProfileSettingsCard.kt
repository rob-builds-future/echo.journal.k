package com.example.echojournal.ui.components.mainflow.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.settingsScreen.SettingItem

// Dummy user data
private const val dummyUsername = "EchoUser"
private const val dummyInfo = "Deine Profil Infos"
private const val dummyTargetLanguage = "Deutsch"

@Composable
fun ProfileSettingsCard(
    onNavigateToProfile: (SettingType) -> Unit
) {
// Profil Einstellungen Card
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = MaterialTheme.colorScheme.background
        )
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
                label = "Profil Info",
                value = dummyInfo,
                onClick = { onNavigateToProfile(SettingType.ProfileInfo) }
            )
        }
    }
}