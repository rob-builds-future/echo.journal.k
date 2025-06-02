package com.example.echojournal.ui.components.mainflow.settingsScreen

import LanguageViewModel
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
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ProfileSettingsCard(
    onNavigateToProfile: (SettingType) -> Unit
) {
    // Die ViewModels holen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val languageViewModel: LanguageViewModel = koinViewModel()

    // 1) Username jetzt aus PrefsViewModel lesen
    val username by prefsViewModel.username.collectAsState()

    // 2) Sprache etc. (unverändert)
    val currentLanguageCode by prefsViewModel.currentLanguage.collectAsState()
    val allLanguages by languageViewModel.languages.collectAsState()
    val languageName = allLanguages
        .firstOrNull { it.code == currentLanguageCode }
        ?.name
    // Falls kein Name gefunden oder Code leer, zeige Platzhalter
        ?: currentLanguageCode.ifBlank { "–" }

    val authViewModel: AuthViewModel = koinViewModel()
    val userFromAuth by authViewModel.user.collectAsState()
    val memberSince = userFromAuth
        ?.createdAt
        ?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDate()
        ?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN))
        ?: "–"

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
            // 6a. Benutzername
            SettingItem(
                label = "Benutzername",
                value = username.ifBlank { "–" },
                onClick = { onNavigateToProfile(SettingType.Username) }
            )
            HorizontalDivider()

            // 6b. Zielsprache (aus PrefsViewModel)
            SettingItem(
                label = "Zielsprache",
                value = languageName,
                onClick = { onNavigateToProfile(SettingType.TargetLanguage) }
            )
            HorizontalDivider()

            // 6c. Profil-Info (z.B. „Member since“)
            SettingItem(
                label = "Profil Info",
                value = "Member seit $memberSince",
                onClick = { onNavigateToProfile(SettingType.ProfileInfo) }
            )
        }
    }
}