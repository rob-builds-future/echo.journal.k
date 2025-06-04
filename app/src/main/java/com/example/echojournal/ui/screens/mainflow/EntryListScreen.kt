package com.example.echojournal.ui.screens.mainflow

import ColorManager
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.remote.model.JournalEntry
import com.example.echojournal.ui.components.mainflow.entryListScreen.EntryListBottomBar
import com.example.echojournal.ui.components.mainflow.entryListScreen.EntryListTopBar
import com.example.echojournal.ui.components.mainflow.entryListScreen.EntryRow
import com.example.echojournal.ui.components.mainflow.entryListScreen.GradientOverlay
import com.example.echojournal.ui.components.mainflow.entryListScreen.InspirationPopover
import com.example.echojournal.ui.components.mainflow.entryListScreen.StatisticsHeader
import com.example.echojournal.ui.viewModel.EntryViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import com.example.echojournal.util.formatDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntryListScreen(
    onEntryClick: (JournalEntry) -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onStatsClick: () -> Unit
) {
    // ViewModels und Eigenschaften holen
    val viewModel: EntryViewModel = koinViewModel()
    val allEntries by viewModel.localEntries.collectAsState()
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)
    val username by prefsViewModel.username.collectAsState()


    // Favoriten Filter State und Filterung
    var showFavoritesOnly by remember { mutableStateOf(false) }
    val displayed = remember(allEntries, showFavoritesOnly) {
        allEntries.filter { !showFavoritesOnly || it.favorite }
    }

    // Inspiration Popover State
    var showInspirationPopover by remember { mutableStateOf(false) }

    // Gesamt-Wortzahl und -Minuten berechnen
    val totalWords = remember(displayed) {
        displayed.sumOf { entry ->
            entry.content
                .trim()
                .split("\\s+".toRegex())
                .filter { it.isNotBlank() }
                .size
        }
    }
    val totalMinutes = remember(displayed) {
        displayed.sumOf { it.duration }
    }

    // Titel nun aus PrefsViewModel.username statt aus AuthViewModel.user
    val title = buildAnnotatedString {
        val userName = if (username.isNotBlank()) username else "Dein"
        append("$userName’s ")
        withStyle(style = SpanStyle(color = echoColor, fontWeight = FontWeight.Bold)) {
            append("echo")
        }
    }

    // Context für date
    val context = LocalContext.current

    Scaffold(
        topBar = {
            EntryListTopBar(
                title = title,
                onSettingsClick = onSettingsClick,
                onStatsClick = onStatsClick
            )
        },
        bottomBar = {
            EntryListBottomBar(
                showFavoritesOnly = showFavoritesOnly,
                onToggleFavorites = { showFavoritesOnly = !showFavoritesOnly },
                onAddClick = onAddClick,
                onInspirationClick = { showInspirationPopover = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn {
                item {
                    StatisticsHeader(
                        totalWords = totalWords,
                        totalMinutes = totalMinutes
                    )
                }

                items(displayed) { entry ->
                    EntryRow(
                        entry = entry,
                        onClick = { onEntryClick(entry) },
                        onToggleFavorite = {
                            viewModel.toggleFavorite(entry)

                            val dateString = formatDate(entry.createdAt)
                            val action = if (!entry.favorite) "favorisiert" else "aus Favoriten entfernt"
                            Toast.makeText(context, "$dateString $action.", Toast.LENGTH_SHORT).show()
                        },
                        onDelete = { viewModel.deleteEntry(entry.id) }
                    )
                }
            }

            GradientOverlay(
                height = 80.dp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (showInspirationPopover) {
            InspirationPopover(onDismiss = {
                showInspirationPopover = false
            })
        }
    }
}
