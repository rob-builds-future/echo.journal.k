package com.example.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
import com.example.echojournal.ui.components.mainflow.settingsScreen.LanguagePickerList
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.LanguageViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSettingLanguage(
    prefsViewModel: PrefsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    languageViewModel: LanguageViewModel = koinViewModel()
) {
    // Aktuelle Codes & Listen
    val currentCode by prefsViewModel.currentLanguage.collectAsState()
    val languages by languageViewModel.languages.collectAsState()

    // Anzeigename der aktuellen Sprache ermitteln
    val currentName = languages
        .firstOrNull { it.code == currentCode }
        ?.name
        .orEmpty()
        .ifBlank { stringResource(R.string.none_selected) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.profile_language_current, currentName),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 2) Picker-Liste
        LanguagePickerList(
            label = stringResource(R.string.settings_profile_target_language),
            prefsViewModel = prefsViewModel,
            languageViewModel = languageViewModel,
            placeholder = stringResource(R.string.placeholder_search_language),
            onSelect = { dto ->
                // 1) lokal speichern
                prefsViewModel.setLanguage(dto.code)
                // 2) remote updaten
                authViewModel.updatePreferredLanguage(dto.code)
            }
        )
    }
}
