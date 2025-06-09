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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.LanguageViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ProfileSettingsCard(
    onNavigateToProfile: (SettingType) -> Unit
) {
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val languageViewModel: LanguageViewModel = koinViewModel()
    val username by prefsViewModel.username.collectAsState()
    val currentLanguageCode by prefsViewModel.currentLanguage.collectAsState()
    val allLanguages by languageViewModel.localizedLanguages.collectAsState()
    val languageName = allLanguages
        .firstOrNull { it.code == currentLanguageCode }
        ?.name
        ?: currentLanguageCode.ifBlank { "–" }

    val authViewModel: AuthViewModel = koinViewModel()
    val userFromAuth by authViewModel.user.collectAsState()
    val memberSince = userFromAuth
        ?.createdAt
        ?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDate()
        ?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))
        ?: "–"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor   = MaterialTheme.colorScheme.background
        )
    ) {
        Column {
            SettingItem(
                label = stringResource(R.string.label_username),
                value = username.ifBlank { "–" },
                onClick = { onNavigateToProfile(SettingType.Username) }
            )
            HorizontalDivider()
            SettingItem(
                label = stringResource(R.string.settings_profile_target_language),
                value = languageName,
                onClick = { onNavigateToProfile(SettingType.TargetLanguage) }
            )
            HorizontalDivider()
            SettingItem(
                label = stringResource(R.string.settings_profile_info),
                value = stringResource(R.string.settings_profile_info_value, memberSince),
                onClick = { onNavigateToProfile(SettingType.ProfileInfo) }
            )
        }
    }
}
