package com.example.echojournal.ui.screens.mainflow

import android.app.DatePickerDialog
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
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echojournal.R
import com.example.echojournal.data.remote.model.JournalEntry
import com.example.echojournal.ui.components.mainflow.addEntryScreen.EntrySection
import com.example.echojournal.ui.components.mainflow.addEntryScreen.SwapDivider
import com.example.echojournal.ui.components.mainflow.addEntryScreen.TranslationSection
import com.example.echojournal.ui.theme.ColorManager
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryDetailScreen(
    entry: JournalEntry,
    onDismiss: () -> Unit = {}
) {
    val entryViewModel: EntryViewModel = koinViewModel()
    val translationViewModel: TranslationViewModel = koinViewModel()
    // Theme aus DataStore holen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    // — UI-State —
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    var isReversed by remember { mutableStateOf(false) }
    var content by remember { mutableStateOf(entry.content) }

    // Live‐State aus dem TranslationViewModel; wird nur im Edit‐Modus genutzt
    val translatedText by translationViewModel.translatedText.collectAsState()

    // Dates
    var entryDate by remember {
        mutableStateOf(
            entry.createdAt
                ?.toDate()
                ?.toInstant()
                ?.atZone(ZoneId.systemDefault())
                ?.toLocalDate()
                ?: LocalDate.now()
        )
    }
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val weekday = remember(entryDate) {
        entryDate.dayOfWeek
            .getDisplayName(java.time.format.TextStyle.FULL, Locale.GERMAN)
            .replaceFirstChar { it.uppercase() }
    }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    // Sobald showDatePicker true wird, erzeugen wir einen einzelnen DatePickerDialog:
    if (showDatePicker) {
        // 1) Dialog anlegen mit Listener für ON_DATE_SET
        val dialog = DatePickerDialog(
            context,
            R.style.NeutralDatePickerDialogTheme, // falls Du einen eigenen Style nutzt
            { _, year, month, dayOfMonth ->
                // Nutzer hat aktiv ein Datum bestätigt → State updaten:
                entryDate = LocalDate.of(year, month + 1, dayOfMonth)
                // showDatePicker false reicht, weil der Listener danach geschlossen wird:
                showDatePicker = false
            },
            entryDate.year,
            entryDate.monthValue - 1,
            entryDate.dayOfMonth
        )
        // 2) MaxDate setzen, damit keine Zukunft gewählt werden kann:
        dialog.datePicker.maxDate = Instant.now().toEpochMilli()
        // 3) HIER setze ich OnDismissListener, um showDatePicker
        //    auch dann auf false zu setzen, wenn der User den Dialog
        //    per Back-Taste oder außerhalb tippen schließt:
        dialog.setOnDismissListener {
            // Dialog wurde geschlossen, ohne dass Datum bestätigt wurde
            showDatePicker = false
        }
        // 4) Zeige den Dialog:
        dialog.show()
    }

    // Timer:
    var editStartMs by remember { mutableStateOf<Long?>(null) }

    // initiale Übersetzung vorladen
    LaunchedEffect(entry.id) {
        translationViewModel.onTextChanged(content)
    }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            editStartMs = System.currentTimeMillis()
        }
    }

    // sobald Edit-Modus gehen, Stoppuhr starten
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "$weekday, ${entryDate.format(dateFormatter)}",
                        color = if (isEditing) echoColor else MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            if (isEditing) showDatePicker = true
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isEditing) showDiscardDialog = true else onDismiss()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Zurück")
                    }
                },
                actions = {
                    val isEnabled = content.isNotBlank()
                    if (isEditing) {
                        // Pill-Shape mit Echo-Farbe, weißem Icon
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .height(30.dp)
                                .width(60.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(
                                    if (isEnabled) echoColor
                                    else MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.12f
                                    )
                                )
                                .clickable(enabled = isEnabled) {
                                    // 1) Dauer berechnen
                                    val extraMin = editStartMs?.let { start ->
                                        ((System.currentTimeMillis() - start) / 1000 / 60).toInt()
                                    } ?: 0

                                    // 2) LocalDate → Timestamp
                                    val dateInstant = entryDate
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                                    val newCreatedAt = Timestamp(Date.from(dateInstant))

                                    // 3) Ein einziges updated-Objekt inklusive createdAt
                                    val updated = entry.copy(
                                        content = content,
                                        translatedContent = translatedText,
                                        duration = entry.duration + extraMin,
                                        createdAt = newCreatedAt,  // <- hier überschreiben wir den alten Timestamp
                                        updatedAt = Timestamp.now()
                                    )

                                    // 4) Einmalig in die ViewModel-Methode rufen:
                                    entryViewModel.updateEntry(updated)

                                    // 5) aufräumen & zurück
                                    editStartMs = null
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Speichern",
                                tint = if (isEnabled) Color.White
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Bearbeiten",
                                tint = echoColor
                            )
                        }
                    }
                }

            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures { /* clear focus */ } }
        ) {
            Column(Modifier.fillMaxSize()) {
                // Edit Modus
                if (isEditing) {
                    if (isReversed) {
                        TranslationSection(
                            translationText = translatedText,
                            echoColor = echoColor
                        )
                        SwapDivider { isReversed = !isReversed }
                        EntrySection(
                            content = content,
                            onContentChange = { new ->
                                content = new
                                translationViewModel.onTextChanged(new)
                            },
                            focusRequester = remember { FocusRequester() }
                        )
                    } else {
                        EntrySection(
                            content = content,
                            onContentChange = { new ->
                                content = new
                                translationViewModel.onTextChanged(new)
                            },
                            focusRequester = remember { FocusRequester() }
                        )
                        SwapDivider { isReversed = !isReversed }
                        TranslationSection(
                            translationText = translatedText,
                            echoColor = echoColor
                        )
                    }
                } else {
                    // Lese Modus
                    if (isReversed) {
                        DisableSelection {
                            entry.translatedContent
                                .split("\n")
                                .forEachIndexed { idx, line ->
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        color = echoColor
                                    )
                                    if (idx < entry.translatedContent.split("\n").lastIndex) Spacer(
                                        modifier = Modifier.height(6.dp)
                                    )
                                }
                            SwapDivider { isReversed = !isReversed }
                            entry.content
                                .split("\n")
                                .forEachIndexed { idx, line ->
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 3.dp
                                        )
                                    )
                                    if (idx < entry.content.split("\n").lastIndex) Spacer(
                                        modifier = Modifier.height(3.dp)
                                    )
                                }
                        }
                    } else {
                        DisableSelection {
                            entry.content
                                .split("\n")
                                .forEachIndexed { idx, line ->
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 3.dp
                                        )
                                    )
                                    if (idx < entry.content.split("\n").lastIndex) Spacer(
                                        modifier = Modifier.height(3.dp)
                                    )
                                }
                            SwapDivider { isReversed = !isReversed }
                            entry.translatedContent
                                .split("\n")
                                .forEachIndexed { idx, line ->
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        color = echoColor
                                    )
                                    if (idx < entry.translatedContent.split("\n").lastIndex) Spacer(
                                        modifier = Modifier.height(6.dp)
                                    )
                                }
                        }
                    }
                    Spacer(Modifier.weight(1f))
                }

                // Verwerfen-Dialog
                if (showDiscardDialog) {
                    AlertDialog(
                        onDismissRequest = { showDiscardDialog = false },
                        title = { Text("Bearbeitung verwerfen?") },
                        text = { Text("Möchtest du deine Änderungen verwerfen?") },
                        confirmButton = {
                            TextButton(onClick = {
                                // Abbrechen: nur Dialog schließen, im Edit-Modus bleiben
                                showDiscardDialog = false
                            }) { Text("Abbrechen") }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                // Verwerfen: Modus verlassen, Timer verwerfen
                                showDiscardDialog = false
                                isEditing = false
                                editStartMs = null
                            }) {
                                Text("Verwerfen")
                            }
                        }
                    )
                }
            }
        }
    }
}