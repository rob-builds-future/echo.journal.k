package com.example.echojournal.ui.components.EntryListScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EntryListBottomBar(
    showFavoritesOnly: Boolean,
    onToggleFavorites: () -> Unit,
    onAddClick: () -> Unit,
    onInspirationClick: () -> Unit
) {
    BottomAppBar(
        // macht den Hintergrund durchsichtig
        containerColor = Color.Transparent,
        // hebt die Erhöhung auf
        tonalElevation = 0.dp,
        // optional: falls du die Icon-Farbe anpassen willst
        contentColor = Color.Black
    ) {
        IconButton(onClick = onToggleFavorites) {
            Icon(
                imageVector = if (showFavoritesOnly) Icons.Default.BookmarkRemove else Icons.Default.Bookmark,
                contentDescription = "Favoriten"
            )
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onAddClick) {
            Icon(Icons.Default.Add, contentDescription = "Neuer Eintrag")
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onInspirationClick) {
            Text("e.", fontWeight = FontWeight.Bold)
        }
    }
}