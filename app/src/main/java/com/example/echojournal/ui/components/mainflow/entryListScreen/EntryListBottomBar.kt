package com.example.echojournal.ui.components.mainflow.entryListScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.theme.ColorManager
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntryListBottomBar(
    showFavoritesOnly: Boolean,
    onToggleFavorites: () -> Unit,
    onAddClick: () -> Unit,
    onInspirationClick: () -> Unit
) {
    // PrefsViewModel holen, um das aktuelle Theme auszulesen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    BottomAppBar(
        containerColor = Color.Transparent,
        contentColor   = MaterialTheme.colorScheme.onBackground
    ) {
        IconButton(onClick = onToggleFavorites) {
            Icon(
                imageVector = if (showFavoritesOnly) Icons.Default.BookmarkRemove else Icons.Default.Bookmark,
                contentDescription = "Favoriten"
            )
        }
        Spacer(Modifier.weight(1f))
        Surface(
            modifier     = Modifier
                .height(48.dp)
                .width(88.dp),
            shape        = RoundedCornerShape(24.dp),              // Pill-Shape
            color        = MaterialTheme.colorScheme.primary,      // Schwarz im Light, Weiß im Dark
            contentColor = MaterialTheme.colorScheme.onPrimary     // Weiß im Light, Schwarz im Dark
        ) {
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Neuer Eintrag")
            }
        }
        Spacer(Modifier.weight(1f))
        Surface(
            modifier     = Modifier
                .padding(end = 16.dp)
                .size(40.dp),
            shape        = androidx.compose.foundation.shape.CircleShape,
            color        = echoColor,
            contentColor = Color.White
        ) {
            IconButton(
                onClick = onInspirationClick,
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    text = "e.",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}