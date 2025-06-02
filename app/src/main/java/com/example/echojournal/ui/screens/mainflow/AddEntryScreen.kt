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
 * Tauscht zwischen EntrySection und TranslationSection.
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
    // PrefsViewModel holen, um das aktuelle Theme auszulesen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    // Lokale UI-States
    val context = LocalContext.current
    var content by remember { mutableStateOf("") }
    val translationText by translationViewModel.translatedText.collectAsState()
    val createResult by entryViewModel.createResult.collectAsState()
    var showAlert by remember { mutableStateOf(false) }
    val startTimeMs = remember { System.currentTimeMillis() }

    val entryFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isReversed by remember { mutableStateOf(false) }

    var entryDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val weekday = remember(entryDate) {
        entryDate.dayOfWeek
            .getDisplayName(java.time.format.TextStyle.FULL, Locale.GERMAN)
            .replaceFirstChar { it.uppercase() }
    }
    // DatePicker kein Datum > heute
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

        // OnDismissListener setzt showDatePicker = false, wenn der Nutzer „Abbrechen“ klickt
        dialog.setOnDismissListener {
            showDatePicker = false
        }

        dialog.show()
    }

    LaunchedEffect(isReversed) { entryFocusRequester.requestFocus() }
    LaunchedEffect(Unit) { entryFocusRequester.requestFocus() }
    BackHandler(enabled = true) { showAlert = true }

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
                    IconButton(onClick = { showAlert = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                },
                actions = {
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
                                    sourceLang = "de",
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
                modifier = Modifier
                    .fillMaxSize(),
                //verticalArrangement = Arrangement.Top
            ) {
                if (isReversed) {
                    TranslationSection(
                        translationText = translationText,
                        echoColor = echoColor
                    )
                    SwapDivider { isReversed = !isReversed }
                    EntrySection(
                        content = content,
                        onContentChange = {
                            content = it
                            Log.d("AddEntryScreen", "Entry content changed: $it")
                            translationViewModel.onTextChanged(it)
                        },
                        focusRequester = entryFocusRequester
                    )
                } else {
                    EntrySection(
                        content = content,
                        onContentChange = {
                            content = it
                            Log.d("AddEntryScreen", "Entry content changed: $it")
                            translationViewModel.onTextChanged(it)
                        },
                        focusRequester = entryFocusRequester
                    )
                    SwapDivider { isReversed = !isReversed }
                    TranslationSection(
                        translationText = translationText,
                        echoColor = echoColor
                    )
                }
                Spacer(Modifier.weight(1f))
            }
        }

        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("Änderungen verwerfen?") },
                text = { Text("Möchtest du die Änderungen wirklich verwerfen?") },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text(
                            "Abbrechen"
                        )
                    }
                },
                dismissButton = { TextButton(onClick = onDismiss) { Text("Verwerfen") } }
            )
        }

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
}