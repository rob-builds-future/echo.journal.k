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
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.viewModel.LanguageViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LanguageStep(
    onNext: (String) -> Unit,
    languageViewModel: LanguageViewModel = koinViewModel(),
    prefsViewModel: PrefsViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val languages by languageViewModel.localizedLanguages.collectAsState()
    val current by prefsViewModel.currentLanguage.collectAsState()
    var selectedCode by remember { mutableStateOf(current) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Wähle deine echo-Sprache")
        Spacer(Modifier.height(16.dp))

        // Picker füllt den verfügbaren Platz aus (siehe weight)
        LanguagePickerListOnboarding(
            languages = languages,
            selectedCode = selectedCode,
            onSelect = { selectedCode = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(Modifier.height(24.dp))

        BottomBarButton(
            text = "Weiter",
            onClick = { if (selectedCode.isNotBlank()) onNext(selectedCode) },
            enabled = selectedCode.isNotBlank()
        )
    }
}