package com.example.echojournal.ui.components.mainflow.entryListScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    // TranslationViewModel via Koin holen
    val translationViewModel: TranslationViewModel = koinViewModel()

    // UI-State: Laden / Ergebnis / Fehler
    var isLoading by remember { mutableStateOf(true) }
    var inspirationText by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Titel-State
    val originalTitle = "Inspiration"
    var translatedTitle by remember { mutableStateOf<String?>(null) }

    // Inhalt-State (übersetzte Version)
    var translatedContent by remember { mutableStateOf<String?>(null) }

    // Toggle-State: Original oder Übersetzt anzeigen?
    var showTranslated by remember { mutableStateOf(false) }

    // Einmaliges Laden aus Firestore
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

    // Wenn der Nutzer auf „Übersetzen“ wechselt, rufe translateOnce für Titel & Inhalt auf
    LaunchedEffect(showTranslated) {
        if (showTranslated && !isLoading && errorMessage == null) {
            // Titel übersetzen
            CoroutineScope(Dispatchers.IO).launch {
                val tTitle = translationViewModel.translateOnce(originalTitle)
                withContext(Dispatchers.Main) {
                    translatedTitle = tTitle
                }
            }
            // Inhalt übersetzen (sofern vorhanden)
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

    // Dialog anzeigen
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (showTranslated && translatedTitle != null) translatedTitle!! else originalTitle,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            when {
                isLoading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Lade…", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                errorMessage != null -> {
                    Text(text = errorMessage!!, style = MaterialTheme.typography.bodyMedium)
                }
                inspirationText != null -> {
                    if (showTranslated) {
                        val out = translatedContent ?: "Übersetzung wird geladen…"
                        Text(text = out, style = MaterialTheme.typography.bodyLarge)
                    } else {
                        Text(text = inspirationText!!, style = MaterialTheme.typography.bodyLarge)
                    }
                }
                else -> {
                    Text(text = "Unbekannter Fehler.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button „Übersetzen“ / „Original anzeigen“
                if (inspirationText != null && !isLoading && errorMessage == null) {
                    Button(onClick = { showTranslated = !showTranslated }) {
                        Text(text = if (showTranslated) "Original anzeigen" else "Übersetzen")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // „OK“-Button zum Schließen
                Button(onClick = onDismiss) {
                    Text(text = "OK")
                }
            }
        }
    )
}
