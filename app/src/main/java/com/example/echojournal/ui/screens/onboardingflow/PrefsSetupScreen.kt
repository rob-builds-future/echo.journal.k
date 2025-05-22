package com.example.echojournal.ui.screens.onboardingflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Einstellungsbildschirm im Onboarding für Grundoptionen.
 */
@Composable
fun PrefsSetupScreen(
    step: Int = 2,
    totalSteps: Int = 2,
    onComplete: () -> Unit
) {
    var language by remember { mutableStateOf("EN") }


    Scaffold(
        topBar = {
            LinearProgressIndicator(
                progress = { step / totalSteps.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = Color.Black,
                trackColor = Color.LightGray,
            )
        }
    ) { paddingValues ->
        // Painter oder Video Player
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Bevor es mit dem Schreiben und Lernen los geht, lass uns Dein Tagebuch auf Dich anpassen.\n\nBitte wähle die Sprache, in die echo Deine Tagebucheinträge übersetzt, die Farbe Deines echo und dein Schreibziel, falls du eines hast!",
                modifier = Modifier.padding(top = 32.dp),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text("Wähle deine Zielsprache:", Modifier.padding(bottom = 16.dp))
            OutlinedTextField(
                value = language,
                onValueChange = { language = it },
                label = { Text("Sprache-Code (z.B. EN, DE)") }
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text("Wähle deine Echo-Farbe:", Modifier.padding(bottom = 16.dp))
            Spacer(modifier = Modifier.height(32.dp))

            Text("Wähle dein Schreibziel:", Modifier.padding(bottom = 16.dp))
            Spacer(modifier = Modifier.height(32.dp))

            Text("Und keine Sorge: Du kannst Deine Einstellungen jederzeit im Einstellungsbereich der App anpassen!",
                Modifier.padding(bottom = 16.dp))
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Fertig")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrefsSetupScreenPreview() {
    PrefsSetupScreen(step = 2, totalSteps = 2, onComplete = { })
}
