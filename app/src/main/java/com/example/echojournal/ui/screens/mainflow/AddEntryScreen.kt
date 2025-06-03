package com.example.echojournal.ui.screens.mainflow

import ColorManager
import android.app.DatePickerDialog
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echojournal.R
import com.example.echojournal.ui.components.mainflow.addEntryScreen.CombinedRowUnderEntry
import com.example.echojournal.ui.components.mainflow.addEntryScreen.EntrySection
import com.example.echojournal.ui.components.mainflow.addEntryScreen.SwapDivider
import com.example.echojournal.ui.components.mainflow.addEntryScreen.TranslationSection
import com.example.echojournal.ui.viewModel.EntryViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import com.example.echojournal.ui.viewModel.TranslationViewModel
import com.google.firebase.Timestamp
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/**
 * Hauptscreen zum Hinzufügen eines Journal-Eintrags.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    onDismiss: () -> Unit,
    //translationViewModel: TranslationViewModel = koinViewModel()
) {
    // ViewModels holen
    val entryViewModel: EntryViewModel = koinViewModel()
    val translationViewModel: TranslationViewModel = koinViewModel()
    val prefsViewModel: PrefsViewModel = koinViewModel()

    // Theme & Farbe
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    // UI-States
    val context = LocalContext.current
    var content by remember { mutableStateOf("") }
    val translationText by translationViewModel.translatedText.collectAsState()
    val createResult by entryViewModel.createResult.collectAsState()
    var showDiscardAlert by remember { mutableStateOf(false) }
    var showInstructionDialog by remember { mutableStateOf(false) }

    // Fokus-Handling
    val entryFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Entry - Translation Swap
    var isReversed by remember { mutableStateOf(true) }

    // Datum
    var entryDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val weekday = remember(entryDate) {
        entryDate.dayOfWeek
            .getDisplayName(java.time.format.TextStyle.FULL, Locale.GERMAN)
            .replaceFirstChar { it.uppercase() }
    }
    val startTimeMs = remember { System.currentTimeMillis() }

    // Template und Dropdown Guided Journaling
    val currentTemplate by prefsViewModel.currentTemplate.collectAsState()
    var templateMenuExpanded by remember { mutableStateOf(false) }
    // Liste aller Vorlagen-Namen (muss mit PrefsViewModel übereinstimmen)
    val templateOptions = listOf(
        "Keine Vorlage",
        "Produktiver Morgen",
        "Ziele im Blick",
        "Reflexion am Abend",
        "Dankbarkeits-Check"
    )

    // DatePicker-Dialog
    if (showDatePicker) {
        val dialog = DatePickerDialog(
            context,
            R.style.NeutralDatePickerDialogTheme,
            { _, year, month, dayOfMonth ->
                entryDate = LocalDate.of(year, month + 1, dayOfMonth)
                showDatePicker = false
            },
            entryDate.year,
            entryDate.monthValue - 1,
            entryDate.dayOfMonth
        )
        dialog.datePicker.maxDate = Instant.now().toEpochMilli()
        dialog.setOnDismissListener { showDatePicker = false }
        dialog.show()
    }

    // Fokus immer auf EntrySection
    LaunchedEffect(isReversed) { entryFocusRequester.requestFocus() }
    LaunchedEffect(Unit) { entryFocusRequester.requestFocus() }

    BackHandler(enabled = true) { showDiscardAlert = true }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "$weekday, ${entryDate.format(dateFormatter)}",
                        modifier = Modifier.clickable { showDatePicker = true },
                        fontSize = 16.sp,
                        color = echoColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { showDiscardAlert = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                },
                actions = {
                    // Save-Button
                    val isEnabled = content.isNotBlank()
                    val backgroundColor =
                        if (isEnabled) echoColor else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.12f
                        )
                    val iconTint =
                        if (isEnabled) Color.White else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.38f
                        )
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .height(30.dp)
                            .width(60.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(backgroundColor)
                            .clickable(enabled = isEnabled) {
                                val elapsedMs =
                                    System.currentTimeMillis() - startTimeMs
                                val durationMinutes = (elapsedMs / 1000 / 60).toInt()
                                val dateInstant =
                                    entryDate.atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                                val createdTimestamp =
                                    Timestamp(Date.from(dateInstant))
                                entryViewModel.createEntry(
                                    rawContent = content,
                                    duration = durationMinutes,  // oder /1000 für Sekunden
                                    sourceLang = "auto",
                                    targetLang = "en",
                                    createdAt = createdTimestamp
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Speichern",
                            tint = iconTint
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Entry - Translation Swap + Sections
                if (isReversed) {
                    // Wenn reversed: zuerst Translation, dann SwapDivider, dann Entry + CombinedRow
                    TranslationSection(
                        translationText = translationText,
                        echoColor = echoColor
                    )
                    SwapDivider { isReversed = !isReversed }

                    // EntrySection
                    EntrySection(
                        content = content,
                        onContentChange = {
                            content = it
                            Log.d("AddEntryScreen", "Entry content changed: $it")
                            translationViewModel.onTextChanged(it)
                        },
                        focusRequester = entryFocusRequester
                    )

                    // direkt darunter die kombinierte Zeile:
                    CombinedRowUnderEntry(
                        content = content,
                        currentTemplate = currentTemplate,
                        onTemplateSelected = { option ->
                            prefsViewModel.setTemplate(option)
                        },
                        templateOptions = templateOptions,
                        templateMenuExpanded = templateMenuExpanded,
                        onTemplateMenuToggle = { templateMenuExpanded = it },
                        onShowInstructions = { showInstructionDialog = true },
                        echoColor = echoColor
                    )

                } else {
                    // 1. Wenn nicht reversed: zuerst Entry + CombinedRow, dann SwapDivider, dann Translation
                    EntrySection(
                        content = content,
                        onContentChange = {
                            content = it
                            Log.d("AddEntryScreen", "Entry content changed: $it")
                            translationViewModel.onTextChanged(it)
                        },
                        focusRequester = entryFocusRequester
                    )

                    // 2. → Direkt darunter die kombinierte Zeile:
                    CombinedRowUnderEntry(
                        content = content,
                        currentTemplate = currentTemplate,
                        onTemplateSelected = { option ->
                            prefsViewModel.setTemplate(option)
                        },
                        templateOptions = templateOptions,
                        templateMenuExpanded = templateMenuExpanded,
                        onTemplateMenuToggle = { templateMenuExpanded = it },
                        onShowInstructions = { showInstructionDialog = true },
                        echoColor = echoColor
                    )

                    // 3. Dann SwapDivider + TranslationSection
                    SwapDivider { isReversed = !isReversed }
                    TranslationSection(
                        translationText = translationText,
                        echoColor = echoColor
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Discard-Alert
        if (showDiscardAlert) {
            AlertDialog(
                onDismissRequest = { showDiscardAlert = false },
                title = { Text("Änderungen verwerfen?") },
                text = { Text("Möchtest du die Änderungen wirklich verwerfen?") },
                confirmButton = {
                    TextButton(onClick = { showDiscardAlert = false }) {
                        Text(
                            "Abbrechen"
                        )
                    }
                },
                dismissButton = { TextButton(onClick = onDismiss) { Text("Verwerfen") } }
            )
        }

        // Instruction Dialog
        if (showInstructionDialog) {
            AlertDialog(
                onDismissRequest = { showInstructionDialog = false },
                title = { Text("Anleitung: $currentTemplate") },
                text = {
                    // Selection Container für Kopierbarkeit
                    SelectionContainer {
                        // Je nachdem, welche Vorlage aktuell ausgewählt ist, zeigen wir unterschiedliche Anleitungen
                        val instructionText = when (currentTemplate) {
                            "Produktiver Morgen" -> buildString {
                                append("1. Überlege dir, was heute dein Hauptziel ist.\n")
                                append("2. Lege drei Prioritäten fest, die dir am wichtigsten sind.\n")
                                append("3. Notiere mögliche Ablenkungsfaktoren und wie du sie minimierst.\n")
                                append("\nTipp: Versuche, jede Priorität in einem Satz zu beschreiben.")
                            }

                            "Ziele im Blick" -> buildString {
                                append("1. Definiere dein langfristiges Ziel (z. B. in den nächsten 6 Monaten).\n")
                                append("2. Schreibe auf, was du heute konkret dafür getan hast.\n")
                                append("3. Überlege, welche nächsten Schritte du morgen gehen kannst.\n")
                                append("\nTipp: Halte deine Gedanken in kurzen Stichpunkten fest.")
                            }

                            "Reflexion am Abend" -> buildString {
                                append("1. Beschreibe in wenigen Sätzen, was heute besonders war.\n")
                                append("2. Wie fühlst du dich jetzt? Notiere aktuelle Eindrücke.\n")
                                append("3. Was hast du aus den heutigen Erfahrungen gelernt?\n")
                                append("\nTipp: Versuche, ehrlich und ohne Bewertung zu schreiben.")
                            }

                            "Dankbarkeits-Check" -> buildString {
                                append("1. Denke an drei Dinge, für die du heute dankbar bist.\n")
                                append("2. Schreibe jeden Punkt kurz mit ein bis zwei Sätzen aus.\n")
                                append("3. Reflektiere, warum dir gerade diese Dinge wichtig sind.\n")
                                append("\nTipp: Dankbarkeit kann auch in kleinen Alltagsmomenten stecken.")
                            }
                            // Wenn "Keine Vorlage" oder leer, eine generische Anleitung:
                            else -> buildString {
                                append("Du hast aktuell keine Vorlage ausgewählt.\n")
                                append("Hier kannst du frei schreiben, was dir gerade wichtig ist.\n")
                                append("Wenn du später gezieltere Fragen möchtest, wähle oben eine Vorlage aus.\n")
                            }
                        }
                        Text(text = instructionText)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showInstructionDialog = false }) {
                        Text("Schließen")
                    }
                }
            )
        }
    }
    // Create-Result-Handling
    LaunchedEffect(createResult) {
        createResult?.onSuccess {
            entryViewModel.clearCreateResult()
            onDismiss()
        }
        createResult?.onFailure {
            // show error logik ergänzen
            entryViewModel.clearCreateResult()
        }
    }
}