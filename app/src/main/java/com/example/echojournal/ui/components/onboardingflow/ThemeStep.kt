package com.example.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ThemeStep(
    onNext: (String) -> Unit,
    prefsViewModel: PrefsViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val themeOptions = listOf("Smaragd", "Wolkenlos", "Vintage", "Koralle", "Bernstein")
    val current by prefsViewModel.theme.collectAsState()
    var selectedTheme by remember { mutableStateOf(current) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.onboarding_theme_title))
        Spacer(Modifier.height(16.dp))

        ThemePickerOnboarding(
            options = themeOptions,
            selectedTheme = selectedTheme,
            onSelect = { theme ->
                selectedTheme = theme
                prefsViewModel.setTheme(theme)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomBarButton(
            text = stringResource(R.string.onboarding_button_next),
            onClick = { onNext(selectedTheme) }
        )
    }
}
