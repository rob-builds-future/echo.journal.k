package com.example.echojournal.ui.screens.onboardingflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Begrüßungsbildschirm im Onboarding.
 */
@Composable
fun WelcomeScreen(
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Willkommen bei EchoJournal!", Modifier.padding(bottom = 24.dp))
        Text(
            "Diese App hilft dir, deine Gedanken festzuhalten und automatisch zu übersetzen.",
            Modifier.padding(bottom = 24.dp)
        )
        Button(onClick = onNext) {
            Text("Weiter")
        }
    }
}