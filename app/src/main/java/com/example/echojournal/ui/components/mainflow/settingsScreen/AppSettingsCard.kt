package com.example.echojournal.ui.components.mainflow.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.settingsScreen.SettingItem
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppSettingsCard(
    onNavigateToAppSetting: (SettingType) -> Unit
) {
    // Preferences holen, um Theme dynamisch anzuzeigen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val currentTheme by prefsViewModel.theme.collectAsState()
    val currentTemplate by prefsViewModel.currentTemplate.collectAsState()

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
                value = currentTheme,
                onClick = { onNavigateToAppSetting(SettingType.Theme) }
            )
            HorizontalDivider()
            SettingItem(
                label = "Geführtes\nTagebuchschreiben",
                value = currentTemplate.ifBlank { "Keine Vorlage" },
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