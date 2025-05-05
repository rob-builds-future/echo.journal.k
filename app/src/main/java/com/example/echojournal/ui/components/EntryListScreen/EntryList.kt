package com.example.echojournal.ui.components.EntryListScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.local.JournalEntry
import java.time.LocalDateTime

@Composable
fun EntryList(
    entries: List<JournalEntry>,
    filterFavorites: Boolean,
    onEntryClick: (JournalEntry) -> Unit,
    onToggleFavorite: (JournalEntry) -> Unit,
    onDelete: (JournalEntry) -> Unit
) {
    // Filter und Sortierung
    val filtered = remember(entries, filterFavorites) {
        entries
            .filter { !filterFavorites || it.isFavorite }
            .sortedByDescending { it.createdAt }
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(filtered, key = { it.id }) { entry ->
            EntryRow(
                entry = entry,
                onClick = { onEntryClick(entry) },
                onToggleFavorite = { onToggleFavorite(entry) },
                onDelete = { onDelete(entry) }
            )
        }
        // Extra Spacer unten
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// Preview mit Dummy-Daten
@Preview(showBackground = true)
@Composable
fun PreviewEntryList() {
    val now = LocalDateTime.now()
    val sampleEntries = listOf(
        JournalEntry("1", "Beispiel Eintrag 1", now.minusDays(1), false, 5),
        JournalEntry("2", "Beispiel Eintrag 2", now, true, 10)
    )
    EntryList(
        entries = sampleEntries,
        filterFavorites = false,
        onEntryClick = {},
        onToggleFavorite = {},
        onDelete = {}
    )
}
