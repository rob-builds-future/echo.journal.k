package com.example.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echojournal.R
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TemplateStep(
    onNext: (String) -> Unit,
    prefsViewModel: PrefsViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val current by prefsViewModel.currentTemplate.collectAsState()
    var selected by remember { mutableStateOf(current) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            Text(stringResource(R.string.onboarding_template_title))
            Spacer(Modifier.height(16.dp))

            TemplatePickerOnboarding(
                selected = selected,
                onSelect = { selected = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(Modifier.weight(1f))

            Text(
                stringResource(R.string.onboarding_template_info),
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(Modifier.weight(1f))
        }
        BottomBarButton(
            text = stringResource(R.string.onboarding_button_finish),
            onClick = { onNext(selected) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}