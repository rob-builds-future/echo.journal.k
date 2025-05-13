package com.example.echojournal.ui.screens

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.addEntryScreen.EntrySection
import com.example.echojournal.ui.components.addEntryScreen.TranslationSection
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Hauptscreen zum Hinzufügen eines Journal-Eintrags.
 * Tauscht zwischen EntrySection und TranslationSection.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    onDismiss: () -> Unit
) {
    // Lokale UI-Stati
    // Lokale UI-Stati
    var content by remember { mutableStateOf("") }
    var translationText by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    var isReversed by remember { mutableStateOf(false) }
    var entryDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    BackHandler(enabled = true) { showAlert = true }

    // Zeige DatePickerDialog bei Bedarf
    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                entryDate = LocalDate.of(year, month + 1, dayOfMonth)
                showDatePicker = false
            },
            entryDate.year,
            entryDate.monthValue - 1,
            entryDate.dayOfMonth
        ).show()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = entryDate.format(dateFormatter),
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { showAlert = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* TODO: Speichern */ onDismiss() },
                        enabled = content.isNotBlank()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Speichern")
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
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                if (isReversed) {
                    TranslationSection(translationText = translationText)
                    SwapDivider { isReversed = !isReversed }
                    EntrySection(
                        content = content,
                        onContentChange = { content = it }
                    )
                } else {
                    EntrySection(
                        content = content,
                        onContentChange = { content = it }
                    )
                    SwapDivider { isReversed = !isReversed }
                    TranslationSection(translationText = translationText)
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
                    TextButton(onClick = { showAlert = false }) { Text("Abbrechen") }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) { Text("Verwerfen") }
                }
            )
        }
    }
}

@Composable
private fun SwapDivider(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
        Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = "Swap"
        )
    }
}