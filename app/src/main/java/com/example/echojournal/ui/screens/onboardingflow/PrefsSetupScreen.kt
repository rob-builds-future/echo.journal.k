package com.example.echojournal.ui.screens.onboardingflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Einstellungsbildschirm im Onboarding für Grundoptionen.
 */
@Composable
fun PrefsSetupScreen(
    onComplete: () -> Unit
) {
    var language by remember { mutableStateOf("EN") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Wähle deine Zielsprache:", Modifier.padding(bottom = 16.dp))
        OutlinedTextField(
            value = language,
            onValueChange = { language = it },
            label = { Text("Sprache-Code (z.B. EN, DE)") }
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onComplete) {
            Text("Fertig")
        }
    }
}