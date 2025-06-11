package com.example.echojournal.ui.components.onboardingflow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Wähle ein Schreibziel, wenn du möchtest")
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
            "Du kannst diese Einstellungen jederzeit im Einstellungsbereich der App ändern!",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Spacer(Modifier.weight(1f))

        BottomBarButton(
            text = "Abschließen",
            onClick = { onNext(selected) }
        )
    }
}
