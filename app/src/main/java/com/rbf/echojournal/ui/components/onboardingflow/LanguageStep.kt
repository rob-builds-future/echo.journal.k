package com.rbf.echojournal.ui.components.onboardingflow

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
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.viewModel.LanguageViewModel
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
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
        Text(stringResource(R.string.onboarding_language_title))
        Spacer(Modifier.height(16.dp))

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
            text = stringResource(R.string.onboarding_button_next),
            onClick = { if (selectedCode.isNotBlank()) onNext(selectedCode) },
            enabled = selectedCode.isNotBlank()
        )
    }
}
