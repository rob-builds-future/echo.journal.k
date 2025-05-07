package com.example.echojournal.ui.components.entryListScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.local.JournalEntry

fun LazyListScope.EntryList(
    entries: List<JournalEntry>,
    filterFavorites: Boolean,
    onEntryClick: (JournalEntry) -> Unit,
    onToggleFavorite: (JournalEntry) -> Unit,
    onDelete: (JournalEntry) -> Unit
) {
    // Filter und Sortierung
    val filtered = entries
        .filter { !filterFavorites || it.isFavorite }
        .sortedByDescending { it.createdAt }

    items(filtered, key = { it.id }) { entry ->
        EntryRow(
            entry = entry,
            onClick = { onEntryClick(entry) },
            onToggleFavorite = { onToggleFavorite(entry) },
            onDelete = { onDelete(entry) }
        )
    }

    item {
        Spacer(modifier = Modifier.height(100.dp))
    }
}
