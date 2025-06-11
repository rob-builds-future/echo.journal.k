package com.example.echojournal.ui.screens.onboardingflow

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.onboardingflow.LanguageStep
import com.example.echojournal.ui.components.onboardingflow.TemplateStep
import com.example.echojournal.ui.components.onboardingflow.ThemeStep
import com.example.echojournal.ui.components.onboardingflow.UsernameStep
import com.example.echojournal.ui.components.onboardingflow.WelcomeStep
import com.example.echojournal.ui.theme.ColorManager
import com.example.echojournal.ui.viewModel.PrefsViewModel

// -- OnboardingStep Enum für Übersichtlichkeit --
enum class OnboardingStep { Welcome, Username, Language, Theme, Template }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingFlow(
    prefsViewModel: PrefsViewModel,
    onComplete: () -> Unit
) {
    var step by remember { mutableStateOf(OnboardingStep.Welcome) }
    val steps = OnboardingStep.entries.toTypedArray()
    val currentIndex = steps.indexOf(step)
    val totalSteps = steps.size

    // Progressbar/Theme-Logik
    val selectedTheme by prefsViewModel.theme.collectAsState()
    val defaultTheme = "Wolkenlos"
    val activeColor = if (currentIndex >= 3)
        ColorManager.getColor(selectedTheme)
    else
        ColorManager.getColor(defaultTheme)
    val animatedColor by animateColorAsState(targetValue = activeColor)

    // System Back-Button-Handling (kein Zurück auf Welcome)
    BackHandler(enabled = currentIndex > 0) {
        step = steps[currentIndex - 1]
    }

    Scaffold(
        topBar = {
            Column {
                // Progressbar immer GANZ OBEN (vor AppBar)
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / totalSteps },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = animatedColor,
                    trackColor = MaterialTheme.colorScheme.primary,
                )
                // AppBar NUR wenn Arrow angezeigt werden soll
                if (currentIndex > 0) {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = { step = steps[currentIndex - 1] }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                            }
                        }
                    )
                }
            }
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
            when (step) {
                OnboardingStep.Welcome -> WelcomeStep(
                    onNext = { step = OnboardingStep.Username }
                )
                OnboardingStep.Username -> UsernameStep(
                    onNext = { username ->
                        prefsViewModel.setUsername(username)
                        step = OnboardingStep.Language
                    },
                    onBack = { step = OnboardingStep.Welcome }
                )
                OnboardingStep.Language -> LanguageStep(
                    onNext = { languageCode ->
                        prefsViewModel.setLanguage(languageCode)
                        step = OnboardingStep.Theme
                    },
                    onBack = { step = OnboardingStep.Username }
                )
                OnboardingStep.Theme -> ThemeStep(
                    onNext = { theme ->
                        prefsViewModel.setTheme(theme)
                        step = OnboardingStep.Template
                    },
                    onBack = { step = OnboardingStep.Language }
                )
                OnboardingStep.Template -> TemplateStep(
                    onNext = { template ->
                        prefsViewModel.setTemplate(template)
                        prefsViewModel.setOnboarded(true)
                        onComplete()
                    },
                    onBack = { step = OnboardingStep.Theme }
                )
            }
        }
    }
}
