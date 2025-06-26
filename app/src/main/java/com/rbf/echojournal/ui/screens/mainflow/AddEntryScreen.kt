package com.rbf.echojournal.ui.screens.mainflow

import android.app.DatePickerDialog
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.components.mainflow.addEntryScreen.CombinedRowUnderEntry
import com.rbf.echojournal.ui.components.mainflow.addEntryScreen.EntrySection
import com.rbf.echojournal.ui.components.mainflow.addEntryScreen.SwapDivider
import com.rbf.echojournal.ui.components.mainflow.addEntryScreen.TranslationSection
import com.rbf.echojournal.ui.theme.ColorManager
import com.rbf.echojournal.ui.viewModel.EntryViewModel
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import com.rbf.echojournal.ui.viewModel.TranslationViewModel
import com.google.firebase.Timestamp
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    onDismiss: () -> Unit,
    navController: NavHostController
) {
    // ViewModels holen
    val entryViewModel: EntryViewModel = koinViewModel()
    val translationViewModel: TranslationViewModel = koinViewModel()
    val prefsViewModel: PrefsViewModel = koinViewModel()

    // Theme & Farbe
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    val userTargetLang = prefsViewModel.currentLanguage.collectAsState().value

    // UI-States
    val context = LocalContext.current
    var content by remember { mutableStateOf("") }
    val translationText by translationViewModel.translatedText.collectAsState()
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
    val locale = context.resources.configuration.locales.get(0)
    val weekday = remember(entryDate) {
        entryDate.dayOfWeek
            .getDisplayName(TextStyle.FULL, locale)
            .replaceFirstChar { it.uppercase(locale) }
    }
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(locale)
    val startTimeMs = remember { System.currentTimeMillis() }

    // Template und Dropdown Guided Journaling
    val currentTemplate by prefsViewModel.currentTemplate.collectAsState()
    var templateMenuExpanded by remember { mutableStateOf(false) }
    // Liste aller Vorlagen-Namen (muss mit PrefsViewModel übereinstimmen)
    // 1) Liste der Res-IDs
    val templateResIds = listOf(
        R.string.template_none,
        R.string.template_productive_morning,
        R.string.template_goals,
        R.string.template_evening_reflection,
        R.string.template_gratitude
    )
    // 2) Lokalisierte Strings draus bauen
    val templateOptions = templateResIds.map { id ->
        stringResource(id)
    }

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

    val createResult by entryViewModel.createResult.collectAsState()
    LaunchedEffect(createResult) {
        createResult?.onSuccess {
            entryViewModel.extendStreak()
            entryViewModel.clearCreateResult()
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("congrats_date", LocalDate.now().toString())
            onDismiss()   // PopBackStack sofort
        }
        createResult?.onFailure {
            entryViewModel.clearCreateResult()
        }
    }

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
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.contentdesc_close))
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
                                    targetLang = userTargetLang.lowercase(),
                                    createdAt = createdTimestamp
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.contentdesc_save),
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
                    // Wenn nicht reversed: zuerst Entry + CombinedRow, dann SwapDivider, dann Translation
                    EntrySection(
                        content = content,
                        onContentChange = {
                            content = it
                            Log.d("AddEntryScreen", "Entry content changed: $it")
                            translationViewModel.onTextChanged(it)
                        },
                        focusRequester = entryFocusRequester
                    )

                    // Direkt darunter die kombinierte Zeile:
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

                    // Dann SwapDivider + TranslationSection
                    SwapDivider { isReversed = !isReversed }
                    TranslationSection(
                        translationText = translationText,
                        echoColor = echoColor
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Discard-Alert (Änderungen verwerfen?)
        if (showDiscardAlert) {
            AlertDialog(
                onDismissRequest = { showDiscardAlert = false },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = AlertDialogDefaults.shape
                    ),
                containerColor = MaterialTheme.colorScheme.surface,
                title = {
                    Text(
                        text = (stringResource(R.string.discard_changes_title)),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.discard_changes_message),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { showDiscardAlert = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(stringResource(R.string.button_cancel))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(stringResource(R.string.button_discard))
                    }
                }
            )
        }

        // Instruction Dialog (Anleitung)
        if (showInstructionDialog) {
            // Ermittel das richtige Instruction‐String-Res anhand des aktuellen Templates
            val instructionsRes = when (currentTemplate) {
                stringResource(R.string.template_productive_morning)      -> R.string.instructions_productive_morning
                stringResource(R.string.template_goals)                   -> R.string.instructions_goals
                stringResource(R.string.template_evening_reflection)      -> R.string.instructions_evening_reflection
                stringResource(R.string.template_gratitude)               -> R.string.instructions_gratitude
                else                                                      -> R.string.instructions_none
            }

            AlertDialog(
                onDismissRequest = { showInstructionDialog = false },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = AlertDialogDefaults.shape
                    ),
                containerColor = MaterialTheme.colorScheme.surface,
                title = {
                    Text(
                        text = stringResource(R.string.instructions_title, currentTemplate),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                text = {
                    SelectionContainer {
                        Text(
                            text = stringResource(instructionsRes),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showInstructionDialog = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(text = stringResource(R.string.button_close))
                    }
                }
            )
        }
    }
}