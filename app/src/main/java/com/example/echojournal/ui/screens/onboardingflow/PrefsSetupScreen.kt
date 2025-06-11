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
import androidx.compose.runtime.collectAsState
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
import com.example.echojournal.ui.components.onboardingflow.LanguagePickerListOnboarding
import com.example.echojournal.ui.components.onboardingflow.TemplatePickerOnboarding
import com.example.echojournal.ui.components.onboardingflow.ThemePickerOnboarding
import com.example.echojournal.ui.viewModel.LanguageViewModel
import org.koin.androidx.compose.koinViewModel

//@Composable
//fun PrefsSetupScreen(
//    step: Int = 2,
//    totalSteps: Int = 2,
//    onComplete: () -> Unit
//) {
//    var language by remember { mutableStateOf("EN") }
//
//
//    Scaffold(
//        topBar = {
//            LinearProgressIndicator(
//                progress = { step / totalSteps.toFloat() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(4.dp),
//                color = Color.Black,
//                trackColor = Color.LightGray,
//            )
//        }
//    ) { paddingValues ->
//        // Painter oder Video Player
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(8.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.Start
//        ) {
//
//            Text(
//                text = "Bevor es mit dem Schreiben und Lernen los geht, lass uns Dein Tagebuch auf Dich anpassen.\n\nBitte wähle die Sprache, in die Dein echo Tagebucheinträge übersetzt, die Farbe Deines echo und dein Schreibziel, falls du eines hast!",
//                modifier = Modifier.padding(top = 32.dp),
//                fontSize = 16.sp
//            )
//            Spacer(modifier = Modifier.height(32.dp))
//            Text("Wähle Deine echo-Sprache:", Modifier.padding(bottom = 16.dp))
//            OutlinedTextField(
//                value = language,
//                onValueChange = { language = it },
//                label = { Text("Sprache-Code (z.B. EN, DE)") }
//            )
//            Text("Hinweis: Das ist die Sprache, in die echo für Dich übersetzt.", Modifier.padding(top = 4.dp, bottom = 16.dp))
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Text("Wähle Deine echo-Farbe:", Modifier.padding(bottom = 16.dp))
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Text("Wähle ein Schreibziel, wenn Du möchtest:", Modifier.padding(bottom = 16.dp))
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Text("Natürlich kannst Du deine Einstellungen jederzeit im Einstellungsbereich der App anpassen!",
//                Modifier.padding(bottom = 16.dp))
//            Spacer(modifier = Modifier.weight(1f))
//            Button(
//                onClick = onComplete,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Black,
//                    contentColor = Color.White
//                ),
//                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
//            ) {
//                Text("Fertig")
//            }
//        }
//    }
//}

@Composable
fun PrefsSetupScreen(
    step: Int = 2,
    totalSteps: Int = 2,
    onComplete: (
        username: String,
        languageCode: String,
        theme: String,
        template: String
    ) -> Unit
) {
    // Lokale States
    var username by remember { mutableStateOf("") }
    var languageCode by remember { mutableStateOf("en") }
    var theme by remember { mutableStateOf("Smaragd") }
    var template by remember { mutableStateOf("") }

    // Deine LanguageViewModel für die Liste
    val languageViewModel: LanguageViewModel = koinViewModel()
    val languages by languageViewModel.localizedLanguages.collectAsState()

    // Optionen für Theme
    val themeOptions =
        listOf("Smaragd", "Wolkenlos", "Vintage", "Koralle", "Bernstein")

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // EINLEITUNG
            Text(
                text = "Bevor es mit dem Schreiben und Lernen losgeht, lass uns Dein Tagebuch auf Dich anpassen.\n\nBitte wähle die Sprache, in die Deine echo-Tagebucheinträge übersetzt werden, die Farbe Deines echo und Dein Schreibziel, falls Du eines hast!",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )

            Text("Wie möchtest du genannt werden?")
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Anzeigename") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))

            Text("Wähle Deine echo-Sprache:", Modifier.padding(bottom = 8.dp))
            LanguagePickerListOnboarding(
                languages = languages,
                selectedCode = languageCode,
                onSelect = { languageCode = it }
            )
            Text(
                "Hinweis: Das ist die Sprache, in die echo für Dich übersetzt.",
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
            Spacer(Modifier.height(12.dp))

            Text("Wähle Deine echo-Farbe:", Modifier.padding(bottom = 8.dp))
            ThemePickerOnboarding(
                options = themeOptions,
                selectedTheme = theme,
                onSelect = { theme = it }
            )
            Spacer(Modifier.height(16.dp))

            Text("Wähle ein Schreibziel, wenn Du möchtest:", Modifier.padding(bottom = 8.dp))
            TemplatePickerOnboarding(
                selected = template,
                onSelect = { template = it }
            )
            Spacer(Modifier.height(24.dp))

            // ABSCHLUSSINFO
            Text(
                "Natürlich kannst Du Deine Einstellungen jederzeit im Einstellungsbereich der App anpassen!",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Spacer(Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    onComplete(username, languageCode, theme, template)
                },
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

// Preview mit Dummy-Lambda (verhindert Crash)
@Preview(showBackground = true)
@Composable
private fun PrefsSetupScreenPreview() {
    PrefsSetupScreen(
        step = 2,
        totalSteps = 2,
        onComplete = { _, _, _, _ -> /* Do nothing in preview */ }
    )
}