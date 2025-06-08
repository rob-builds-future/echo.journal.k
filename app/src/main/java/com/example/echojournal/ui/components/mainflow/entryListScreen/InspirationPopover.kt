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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
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
import java.util.Locale

@Composable
fun InspirationPopover(
    onDismiss: () -> Unit
) {
    // 1) ViewModels holen
    val translationViewModel: TranslationViewModel = koinViewModel()
    val prefsViewModel: PrefsViewModel = koinViewModel()

    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)
    val isLightTheme = themeName.equals("Light", ignoreCase = true)

    // 2) Context + Locale
    val context = LocalContext.current
    // ab API 24: configuration.locales.get(0), sonst configuration.locale
    val locale: Locale = context.resources.configuration.locales.get(0)

    // 3) UI-State
    var isLoading by remember { mutableStateOf(true) }
    var inspirationOriginal by remember { mutableStateOf<String?>(null) }
    var inspirationTranslated by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showTranslated by remember { mutableStateOf(false) }

    // 4) Titel‐Text (wird per strings.xml lokalisiert)
    val defaultTitle = stringResource(R.string.title_inspiration)

    // 5) Firestore‐Ladevorgang
    LaunchedEffect(Unit) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val snapshot = firestore.collection("inspirations").get().await()
            if (snapshot.isEmpty) {
                errorMessage = context.getString(R.string.error_no_inspirations)
            } else {
                val randomDoc = snapshot.documents.random()
                val tag = locale.toString().lowercase() // z. B. "de", "pt_br", "en_us"
                inspirationOriginal = when {
                    tag.startsWith("de")    -> randomDoc.getString("text_de")
                    tag.startsWith("pt_br") -> randomDoc.getString("text_pt_BR")
                    else                     -> randomDoc.getString("text_en")
                }
                // Fallback auf Englisch, falls leer
                if (inspirationOriginal.isNullOrBlank()) {
                    inspirationOriginal = randomDoc.getString("text_en")
                }
            }
        } catch (e: Exception) {
            errorMessage = context.getString(
                R.string.error_loading_inspiration,
                e.localizedMessage ?: ""
            )
        } finally {
            isLoading = false
        }
    }

    // 6) On-Demand-Übersetzung: Nur starten, wenn User klickt
    //    => Wir binden diesen Code in den Button-OnClick statt in LaunchedEffect.

    // 7) AlertDialog-UI
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = if (isLightTheme) Color.White else Color.Black,
        title = {
            Text(
                text = defaultTitle,
                color = if (isLightTheme) Color.Black else Color.White,
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
                            text = stringResource(R.string.text_loading),
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
                inspirationOriginal != null -> {
                    // Hier entscheiden wir, ob Original oder bereits übersetzt angezeigt wird:
                    val displayText = if (showTranslated && inspirationTranslated != null) {
                        inspirationTranslated!!
                    } else {
                        inspirationOriginal!!
                    }
                    val displayColor = when {
                        showTranslated && inspirationTranslated != null -> echoColor
                        isLightTheme                                 -> Color.Black
                        else                                         -> Color.White
                    }
                    Text(
                        text = displayText,
                        color = displayColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    Text(
                        text = stringResource(R.string.error_unknown),
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
                // 8) Übersetzen-/Original-Toggle-Button
                if (!isLoading && inspirationOriginal != null && inspirationTranslated == null) {
                    // Erste Übersetzung noch nicht gemacht
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val tContent = translationViewModel.translateOnce(inspirationOriginal!!)
                                withContext(Dispatchers.Main) {
                                    inspirationTranslated = tContent
                                    showTranslated = true
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLightTheme) Color.Black else Color.White,
                            contentColor = if (isLightTheme) Color.White else Color.Black
                        )
                    ) {
                        Text(text = stringResource(R.string.button_translate))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                } else if (inspirationTranslated != null) {
                    // Bereits übersetzt – hier Toggle zwischen Original & Übersetzung erlauben
                    Button(
                        onClick = { showTranslated = !showTranslated },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLightTheme) Color.Black else Color.White,
                            contentColor = if (isLightTheme) Color.White else Color.Black
                        )
                    ) {
                        Text(
                            text = if (showTranslated)
                                stringResource(R.string.button_show_original)
                            else
                                stringResource(R.string.button_translate)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // 9) OK-Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLightTheme) Color.Black else Color.White,
                        contentColor = if (isLightTheme) Color.White else Color.Black
                    )
                ) {
                    Text(text = stringResource(R.string.button_ok))
                }
            }
        }
    )
}
