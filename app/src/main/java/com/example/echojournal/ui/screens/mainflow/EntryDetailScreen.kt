
import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.remote.model.JournalEntry
import com.example.echojournal.ui.components.mainflow.addEntryScreen.EntrySection
import com.example.echojournal.ui.components.mainflow.addEntryScreen.SwapDivider
import com.example.echojournal.ui.components.mainflow.addEntryScreen.TranslationSection
import com.example.echojournal.ui.viewModel.EntryViewModel
import com.example.echojournal.ui.viewModel.TranslationViewModel
import com.google.firebase.Timestamp
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryDetailScreen(
    entry: JournalEntry,
    onDismiss: () -> Unit = {}
) {
    val entryViewModel: EntryViewModel = koinViewModel()
    val translationViewModel: TranslationViewModel = koinViewModel()

    // — UI-State —
    var isEditing by remember { mutableStateOf(false) }
    var isReversed by remember { mutableStateOf(false) }
    var content by remember { mutableStateOf(entry.content) }
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
    var showDatePicker by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    val translatedText by translationViewModel.translatedText.collectAsState()

    // initiale Übersetzung vorladen
    LaunchedEffect(entry.id) {
        translationViewModel.onTextChanged(content)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = entryDate.format(dateFormatter),
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
                    if (isEditing) {
                        IconButton(
                            enabled = content.isNotBlank(),
                            onClick = {
                                // Update auslösen
                                val updated = entry.copy(
                                    content = content,
                                    translatedContent = translatedText,
                                    updatedAt = Timestamp.now()
                                )
                                entryViewModel.updateEntry(updated)
                                onDismiss()
                            }
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Speichern"
                            )
                        }
                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Bearbeiten"
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
                if (isEditing) {
                    if (isReversed) {
                        TranslationSection(translationText = translatedText)
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
                        TranslationSection(translationText = translatedText)
                    }
                } else {
                    if (isReversed) {
                        DisableSelection {
                            translatedText
                                .split("\n")
                                .forEachIndexed { idx, line ->
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    if (idx < translatedText.split("\n").lastIndex) Spacer(modifier = Modifier.height(6.dp))
                                }
                            SwapDivider { isReversed = !isReversed }
                            content
                                .split("\n")
                                .forEachIndexed { idx, line ->
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                    if (idx < content.split("\n").lastIndex) Spacer(modifier = Modifier.height(6.dp))
                                }
                        }
                    } else {
                        DisableSelection {
                            content
                                .split("\n")
                                .forEachIndexed { idx, line ->
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                    if (idx < content.split("\n").lastIndex) Spacer(modifier = Modifier.height(6.dp))
                                }
                            SwapDivider { isReversed = !isReversed }
                            translatedText
                                .split("\n")
                                .forEachIndexed { idx, line ->
                                    Text(
                                        text = line,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    if (idx < translatedText.split("\n").lastIndex) Spacer(modifier = Modifier.height(6.dp))
                                }
                        }
                    }
                    Spacer(Modifier.weight(1f))
                }

// Datum-Picker
                if (showDatePicker) {
                    DatePickerDialog(
                        LocalContext.current,
                        { _, y, m, d ->
                            entryDate = LocalDate.of(y, m + 1, d)
                            showDatePicker = false
                        },
                        entryDate.year,
                        entryDate.monthValue - 1,
                        entryDate.dayOfMonth
                    ).show()
                }

// Verwerfen-Dialog
                if (showDiscardDialog) {
                    AlertDialog(
                        onDismissRequest = { showDiscardDialog = false },
                        title = { Text("Bearbeitung verwerfen?") },
                        text = { Text("Möchtest du deine Änderungen verwerfen?") },
                        confirmButton = {
                            TextButton(onClick = {
                                showDiscardDialog = false
                                isEditing = false
                            }) { Text("Abbrechen") }
                        },
                        dismissButton = {
                            TextButton(onClick = onDismiss) { Text("Verwerfen") }
                        }
                    )
                }
            }
        }
    }
}