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

@Composable
fun AppSettingsCard(
    onNavigateToAppSetting: (SettingType) -> Unit
) {
// App Einstellungen Card
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
                label = "Echo-Farbe",
                value = "Wolkenlos",
                onClick = { onNavigateToAppSetting(SettingType.Theme) }
            )
            HorizontalDivider()
            SettingItem(
                label = "Geführtes Tagebuchschreiben",
                value = "Reflexion am Abend",
                onClick = { onNavigateToAppSetting(SettingType.Templates) }
            )
            HorizontalDivider()
            SettingItem(
                label = "Erinnerungen",
                value = "Deine Erinnerungen",
                onClick = { onNavigateToAppSetting(SettingType.Reminders) }
            )
        }
    }
}