package com.rbf.echojournal.ui.components.mainflow.settingsScreen.settingDetailScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.theme.ColorManager
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSettingTheme(
    prefsViewModel: PrefsViewModel = koinViewModel()
) {
    // Interne Theme-Keys (werden gespeichert und von ColorManager genutzt)
    val options = listOf("Smaragd","Wolkenlos","Vintage","Koralle","Bernstein")
    // Mapping auf Resource-IDs für die Anzeige
    val displayRes = mapOf(
        "Smaragd"    to R.string.theme_display_emerald,
        "Wolkenlos"  to R.string.theme_display_cloudless,
        "Vintage"    to R.string.theme_display_vintage,
        "Koralle"    to R.string.theme_display_coral,
        "Bernstein"  to R.string.theme_display_amber
    )

    val currentTheme by prefsViewModel.theme.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        // Überschrift
        Text(
            text = stringResource(R.string.profile_theme_title),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        options.forEach { key ->
            // Farbe via internen Key
            val color = ColorManager.getColor(key)
            // übersetzter Display-Name
            val displayName = stringResource(displayRes[key]!!)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { prefsViewModel.setTheme(key) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Farb-Box
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = color, shape = RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(16.dp))
                // Label
                Text(
                    text = displayName,
                    modifier = Modifier.weight(1f)
                )
                // RadioButton
                RadioButton(
                    selected = key == currentTheme,
                    onClick  = { prefsViewModel.setTheme(key) }
                )
            }
        }
    }
}
