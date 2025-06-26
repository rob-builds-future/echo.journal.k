package com.rbf.echojournal.ui.screens.mainflow

import android.app.DatePickerDialog
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rbf.echojournal.R
import com.rbf.echojournal.data.remote.model.JournalEntry
import com.rbf.echojournal.ui.components.mainflow.addEntryScreen.EntrySection
import com.rbf.echojournal.ui.components.mainflow.addEntryScreen.SwapDivider
import com.rbf.echojournal.ui.components.mainflow.addEntryScreen.TranslationSection
import com.rbf.echojournal.ui.theme.ColorManager
import com.rbf.echojournal.ui.viewModel.EntryViewModel
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import com.rbf.echojournal.ui.viewModel.TranslationViewModel
import com.rbf.echojournal.util.LanguageUtil
import com.google.firebase.Timestamp
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
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
    val prefsViewModel: PrefsViewModel = koinViewModel()

    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    // Preferred Language (ISO‐Code, z.B. "de", "en", "fr")
    val preferredLang by prefsViewModel.currentLanguage.collectAsState()

    // — UI-State —
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    var isReversed by remember { mutableStateOf(false) }
    var content by remember { mutableStateOf(entry.content) }
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
    val locale = context.resources.configuration.locales.get(0)
    val weekday = remember(entryDate) {
        entryDate.dayOfWeek
            .getDisplayName(TextStyle.FULL, locale)
            .replaceFirstChar { it.uppercase(locale) }
    }
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(locale)
    var showDatePicker by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    // Sobald showDatePicker true wird, erzeugen wir einen einzelnen DatePickerDialog:
    if (showDatePicker) {
        // Dialog anlegen mit Listener für ON_DATE_SET
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
        // MaxDate setzen, damit keine Zukunft gewählt werden kann:
        dialog.datePicker.maxDate = Instant.now().toEpochMilli()
        //    OnDismissListener setzen, um showDatePicker
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

    // 2) Halte die TTS-Instanz und ob sie initialisiert ist
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsInitialized by remember { mutableStateOf(false) }    // erst true, wenn onInit kam
    var ttsLocale by remember { mutableStateOf<Locale?>(null) }

    var hasShownUnsupportedToast by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    // --- Schritt A: TTS-Engine anlegen, OnInit setzen ---
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInitialized = true
                Log.d("EntryDetailScreen", "TTS-Engine initialisiert")
            } else {
                Log.d("EntryDetailScreen", "TTS-Init fehlgeschlagen, status=$status")
            }
        }
    }

    // --- Schritt B: Sobald TTS initialisiert ist UND preferredLang sich ändert,
    //                 prüfen wir Verfügbarkeit und setzen die Sprache ---
    LaunchedEffect(ttsInitialized, preferredLang) {
        if (!ttsInitialized) return@LaunchedEffect
        val engine = tts ?: return@LaunchedEffect

        // a) Bestimme BCP-47-Tag per Utility (z.B. "pb" → "pt-BR")
        val desiredTag = LanguageUtil.mapLibreToBcp47(preferredLang)
        Log.d("EntryDetailScreen", "mapped $preferredLang → $desiredTag")

        // b) Erstelle Locale und prüfe Verfügbarkeit
        val candidateLocale = Locale.forLanguageTag(desiredTag)
        when (engine.isLanguageAvailable(candidateLocale)) {
            TextToSpeech.LANG_AVAILABLE,
            TextToSpeech.LANG_COUNTRY_AVAILABLE,
            TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE -> {
                engine.setLanguage(candidateLocale)
                ttsLocale = candidateLocale
                hasShownUnsupportedToast = false
                Log.d("EntryDetailScreen", "TTS-Locale gesetzt auf $desiredTag")
                return@LaunchedEffect
            }
        }

        // c) Fallback: wenn desiredTag "pt-BR", probiere generisches "pt"
        if (desiredTag.startsWith("pt", ignoreCase = true)) {
            val genericPt = Locale.forLanguageTag("pt")
            if (engine.isLanguageAvailable(genericPt) in listOf(
                    TextToSpeech.LANG_AVAILABLE,
                    TextToSpeech.LANG_COUNTRY_AVAILABLE,
                    TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE
                )
            ) {
                engine.setLanguage(genericPt)
                ttsLocale = genericPt
                hasShownUnsupportedToast = false
                Log.d("EntryDetailScreen", "TTS-Fallback auf generisches pt")
                return@LaunchedEffect
            }
        }

        // d) Wenn gar nichts passte: ttsLocale = null, Toast (nur einmal)
        ttsLocale = null
        if (!hasShownUnsupportedToast) {
            Toast.makeText(
                context,
                "Keine passende TTS-Sprachdatei für $desiredTag gefunden",
                Toast.LENGTH_SHORT
            ).show()
            hasShownUnsupportedToast = true
            Log.d(
                "EntryDetailScreen",
                "Toast: keine passende Sprachdatei für $desiredTag"
            )
        }
    }

    // --- Schritt C: TTS-Engine wieder freigeben, wenn der Composable verschwindet ---
    DisposableEffect(Unit) {
        onDispose {
            Log.d("EntryDetailScreen", "TTS-Engine herunterfahren")
            tts?.stop()
            tts?.shutdown()
        }
    }

    // --- TextToSpeech-Playback-Logik (Listener, um isSpeaking zu steuern) ---
    LaunchedEffect(tts) {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                // Sobald TTS wirklich startet, setzen wir isSpeaking = true
                isSpeaking = true
            }

            override fun onDone(utteranceId: String?) {
                // Wiedergabe beendet – isSpeaking wieder false
                isSpeaking = false
            }

            @Deprecated("Deprecated in Java", ReplaceWith("isSpeaking = false"))
            override fun onError(utteranceId: String?) {
                // Im Fehlerfall ebenfalls zurücksetzen
                isSpeaking = false
            }
        })
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
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.contentdesc_close)
                        )
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
                                    val newCreatedAt =
                                        Timestamp(Date.from(dateInstant))

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
                                contentDescription = stringResource(R.string.contentdesc_save),
                                tint = if (isEnabled) Color.White
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = stringResource(R.string.contentdesc_edit),
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
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
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
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        color = echoColor
                                    )
                                    if (idx < entry.translatedContent.split("\n").lastIndex) Spacer(
                                        modifier = Modifier.height(6.dp)
                                    )
                                }
                        }
                    }
                    Spacer(Modifier.height(32.dp))

                    // ── Button-Reihe: Vorlesen + Info ──
                    if (entry.translatedContent.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            // 1) Vorlesen/Stop-Button
                            Button(
                                onClick = {
                                    if (isSpeaking) {
                                        // Wenn aktuell abgespielt wird: Stoppen
                                        tts?.stop()
                                        isSpeaking = false
                                    } else {
                                        // Ansonsten Vorlesen starten
                                        tts?.speak(
                                            entry.translatedContent,
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            "ENTRY_TTS_ID"
                                        )
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = echoColor,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                Icon(
                                    imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = stringResource(
                                        if (isSpeaking) R.string.contentdesc_stop else R.string.contentdesc_read
                                    ),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = stringResource(
                                        if (isSpeaking) R.string.button_stop else R.string.button_read
                                    ),
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // 2) Info-Button für Hinweise
                            IconButton(onClick = { showInfoDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = stringResource(R.string.contentdesc_tts_info),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // ── Info-Dialog anzeigen, wenn showInfoDialog == true ──
                    if (showInfoDialog) {
                        AlertDialog(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = AlertDialogDefaults.shape
                                ),
                            containerColor = MaterialTheme.colorScheme.surface,
                            onDismissRequest = { showInfoDialog = false },
                            title = {
                                Text(
                                    stringResource(R.string.tts_info_title),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            text = {
                                Text(
                                    stringResource(R.string.tts_info_message),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = { showInfoDialog = false },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Text(stringResource(R.string.button_understood))
                                }
                            }
                        )
                    }
                }


                // Verwerfen-Dialog
                if (showDiscardDialog) {
                    AlertDialog(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = AlertDialogDefaults.shape
                            ),
                        containerColor = MaterialTheme.colorScheme.surface,
                        onDismissRequest = { showDiscardDialog = false },
                        title = {
                            Text(
                                stringResource(R.string.discard_changes_title),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        text = {
                            Text(
                                stringResource(R.string.discard_changes_message),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    // Abbrechen: nur Dialog schließen, im Edit-Modus bleiben
                                    showDiscardDialog = false
                                }, colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) { Text(stringResource(R.string.button_cancel)) }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                // Verwerfen: Modus verlassen, Timer verwerfen
                                showDiscardDialog = false
                                isEditing = false
                                editStartMs = null
                            }, colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                            ) {
                                Text(stringResource(R.string.button_discard))
                            }
                        }
                    )
                }
            }
        }
    }
}