package com.example.echojournal.ui.components.mainflow.entryListScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.theme.ColorManager
import com.example.echojournal.ui.viewModel.PrefsViewModel
import com.example.echojournal.ui.viewModel.TranslationViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun InspirationPopover(
    onDismiss: () -> Unit
) {
    // 1) Fetch TranslationViewModel via Koin
    val translationViewModel: TranslationViewModel = koinViewModel()
    // 2) Fetch PrefsViewModel via Koin to read current theme
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    // Get the “echo” color from ColorManager
    val echoColor = ColorManager.getColor(themeName)

    // Determine whether we’re in “light mode” (you can adjust the string check if you use a different naming convention)
    val isLightTheme = themeName.equals("Light", ignoreCase = true)

    // UI state: loading / result / error
    var isLoading by remember { mutableStateOf(true) }
    var inspirationText by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Title state (original vs. translated)
    val originalTitle = "Inspiration"
    var translatedTitle by remember { mutableStateOf<String?>(null) }

    // Content state (translated vs. original)
    var translatedContent by remember { mutableStateOf<String?>(null) }

    // Toggle: show original or translated?
    var showTranslated by remember { mutableStateOf(false) }

    // 3) Load a random “inspiration” from Firestore exactly once
    LaunchedEffect(Unit) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val snapshot = firestore
                .collection("inspirations")
                .get()
                .await()

            if (snapshot.isEmpty) {
                errorMessage = "Keine Inspirationen gefunden."
            } else {
                val textes = snapshot.documents.mapNotNull { doc ->
                    doc.getString("text")
                }
                if (textes.isEmpty()) {
                    errorMessage = "Keine gültigen Texte."
                } else {
                    val randomIndex = Random.nextInt(textes.size)
                    inspirationText = textes[randomIndex]
                }
            }
        } catch (e: Exception) {
            errorMessage = "Fehler beim Laden: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    // 4) Whenever the user toggles “Übersetzen”, trigger one‐time translation for both title & content
    LaunchedEffect(showTranslated) {
        if (showTranslated && !isLoading && errorMessage == null) {
            // Translate title
            CoroutineScope(Dispatchers.IO).launch {
                val tTitle = translationViewModel.translateOnce(originalTitle)
                withContext(Dispatchers.Main) {
                    translatedTitle = tTitle
                }
            }
            // Translate content, if present
            inspirationText?.let { text ->
                CoroutineScope(Dispatchers.IO).launch {
                    val tContent = translationViewModel.translateOnce(text)
                    withContext(Dispatchers.Main) {
                        translatedContent = tContent
                    }
                }
            }
        } else if (!showTranslated) {
            translatedTitle = null
            translatedContent = null
        }
    }

    // 5) Show the AlertDialog with customized colors
    AlertDialog(
        onDismissRequest = onDismiss,
        // Force background to white in light mode, black in dark mode:
        containerColor = if (isLightTheme) Color.White else Color.Black,
        title = {
            // Decide which title to show, and which color:
            val titleText = if (showTranslated && translatedTitle != null) translatedTitle!! else originalTitle
            val titleColor = if (showTranslated) {
                // Translated → always echoColor
                echoColor
            } else {
                // Original → black in light, white in dark
                if (isLightTheme) Color.Black else Color.White
            }
            Text(
                text = titleText,
                color = titleColor,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            when {
                isLoading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Lade…",
                            color = if (isLightTheme) Color.Black else Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = if (isLightTheme) Color.Black else Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                inspirationText != null -> {
                    if (showTranslated) {
                        // If translation is still in flight, show a placeholder
                        val out = translatedContent ?: "Übersetzung wird geladen…"
                        Text(
                            text = out,
                            color = echoColor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        Text(
                            text = inspirationText!!,
                            color = if (isLightTheme) Color.Black else Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    Text(
                        text = "Unbekannter Fehler.",
                        color = if (isLightTheme) Color.Black else Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 6) “Übersetzen” / “Original anzeigen” toggle button (only if we have text and no error)
                if (inspirationText != null && !isLoading && errorMessage == null) {
                    Button(
                        onClick = { showTranslated = !showTranslated },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLightTheme) Color.Black else Color.White,
                            contentColor = if (isLightTheme) Color.White else Color.Black
                        )
                    ) {
                        Text(
                            text = if (showTranslated) "Original anzeigen" else "Übersetzen"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                // 7) “OK”‐Button to close
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLightTheme) Color.Black else Color.White,
                        contentColor = if (isLightTheme) Color.White else Color.Black
                    )
                ) {
                    Text(text = "OK")
                }
            }
        }
    )
}
