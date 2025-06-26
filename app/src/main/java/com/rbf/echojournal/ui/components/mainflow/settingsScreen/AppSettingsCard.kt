package com.rbf.echojournal.ui.components.mainflow.settingsScreen

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppSettingsCard(
    onNavigateToAppSetting: (SettingType) -> Unit
) {
    val context = LocalContext.current
    val toastText = stringResource(R.string.reminders_coming_soon)
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val currentKey by prefsViewModel.theme.collectAsState()
    val currentTemplate by prefsViewModel.currentTemplate.collectAsState()

    // Mapping von Key → String-Resource
    val displayMap = mapOf(
        "Smaragd" to R.string.theme_display_emerald,
        "Wolkenlos" to R.string.theme_display_cloudless,
        "Vintage" to R.string.theme_display_vintage,
        "Koralle" to R.string.theme_display_coral,
        "Bernstein" to R.string.theme_display_amber
    )

    // Hole dir hier IMMER den Resource-String, nie den rohen Key
    val themeLabel =
        stringResource(displayMap[currentKey] ?: R.string.theme_display_cloudless)

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
                label = stringResource(R.string.settings_app_theme),
                value = themeLabel,
                onClick = { onNavigateToAppSetting(SettingType.Theme) }
            )
            HorizontalDivider()
            SettingItem(
                label = stringResource(R.string.settings_app_templates),
                value = if (currentTemplate.isBlank())
                    stringResource(R.string.template_none)
                else
                    currentTemplate,
                onClick = { onNavigateToAppSetting(SettingType.Templates) }
            )
            HorizontalDivider()
            SettingItem(
                label = stringResource(R.string.settings_app_reminders),
                value = stringResource(R.string.settings_app_reminders_summary),
                enabled = false,
                onClick = {},
                onDisabledClick = {
                    Toast.makeText(
                        context,
                        toastText,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}
