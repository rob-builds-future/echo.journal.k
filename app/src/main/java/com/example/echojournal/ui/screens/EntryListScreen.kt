package com.example.echojournal.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.local.JournalEntry
import com.example.echojournal.ui.components.EntryListScreen.AddEntryDialogPlaceholder
import com.example.echojournal.ui.components.EntryListScreen.EntryList
import com.example.echojournal.ui.components.EntryListScreen.EntryListBottomBar
import com.example.echojournal.ui.components.EntryListScreen.EntryListTopBar
import com.example.echojournal.ui.components.EntryListScreen.GradientOverlay
import com.example.echojournal.ui.components.EntryListScreen.InspirationPopoverPlaceholder
import com.example.echojournal.ui.components.EntryListScreen.StatisticsHeaderPlaceholder
import java.time.LocalDateTime








@Composable
fun EntryListScreen() {
    // Lokale UI-States
    var showAddEntry by remember { mutableStateOf(false) }
    var showFavoritesOnly by remember { mutableStateOf(false) }
    var showInspirationPopover by remember { mutableStateOf(false) }

    // Dummy-Daten für Preview / Entwicklung
    val now = remember { LocalDateTime.now() }
    val sampleEntries = remember {
        listOf(
            // id, content, createdAt, isFavorite, duration
            JournalEntry("5", "Beispiel Eintrag 5", now.minusDays(4), true, 6),
            JournalEntry("4", "Beispiel Eintrag 4", now.minusDays(3), true, 6),
            JournalEntry("1", "Beispiel Eintrag 1", now.minusDays(2), false, 5),
            JournalEntry("2", "Beispiel Eintrag 2", now.minusDays(1), true, 8),
            JournalEntry("3", "Beispiel Eintrag 3", now, false, 12)
        )
    }

    Scaffold(
        topBar = { EntryListTopBar() },
        bottomBar = {
            EntryListBottomBar(
                showFavoritesOnly = showFavoritesOnly,
                onToggleFavorites = { showFavoritesOnly = !showFavoritesOnly },
                onAddClick = { showAddEntry = true },
                onInspirationClick = { showInspirationPopover = true }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            Column {
                StatisticsHeaderPlaceholder()
                EntryList(
                    entries = sampleEntries,
                    filterFavorites = showFavoritesOnly,
                    onEntryClick = { /* TODO: Navigation oder Aktion */ },
                    onToggleFavorite = { /* TODO: Favorit umschalten */ },
                    onDelete = { /* TODO: Eintrag löschen */ }
                )
            }
            GradientOverlay(
                height = 140.dp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (showAddEntry) {
            AddEntryDialogPlaceholder(onDismiss = { showAddEntry = false })
        }
        if (showInspirationPopover) {
            InspirationPopoverPlaceholder(onDismiss = { showInspirationPopover = false })
        }
    }
}
