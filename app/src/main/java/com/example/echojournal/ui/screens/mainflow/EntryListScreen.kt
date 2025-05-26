package com.example.echojournal.ui.screens.mainflow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.data.remote.model.JournalEntry
import com.example.echojournal.ui.components.mainflow.entryListScreen.EntryList
import com.example.echojournal.ui.components.mainflow.entryListScreen.EntryListBottomBar
import com.example.echojournal.ui.components.mainflow.entryListScreen.EntryListTopBar
import com.example.echojournal.ui.components.mainflow.entryListScreen.GradientOverlay
import com.example.echojournal.ui.components.mainflow.entryListScreen.InspirationPopoverPlaceholder
import com.example.echojournal.ui.components.mainflow.entryListScreen.StatisticsHeader
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.EntryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntryListScreen(
    onEntryClick: (JournalEntry) -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    // AuthViewModel und EntryViewModel holen
    val authViewModel: AuthViewModel = koinViewModel()
    val user by authViewModel.user.collectAsState()
    val viewModel: EntryViewModel = koinViewModel()
    val entries by viewModel.entries.collectAsState()

    // Lokale UI-States
    var showAddEntry by remember { mutableStateOf(false) }
    var showFavoritesOnly by remember { mutableStateOf(false) }
    var showInspirationPopover by remember { mutableStateOf(false) }

    // Wenn AddEntry aktiv ist, ersetze kompletten Inhalt
    if (showAddEntry) {
        AddEntryScreen(
            onDismiss = { showAddEntry = false }
        )
        return
    }

    val titleText = user?.let { "${it.username}’s Journal" } ?: "Dein Journal"
    val filtered = entries.filter { !showFavoritesOnly || it.isFavorite }

    Scaffold(
        topBar = {
            EntryListTopBar(
                title = titleText,
                onSettingsClick = onSettingsClick,
                onStatsClick = {}
            )
        },
        bottomBar = {
            EntryListBottomBar(
                showFavoritesOnly = showFavoritesOnly,
                onToggleFavorites = { showFavoritesOnly = !showFavoritesOnly },
                onAddClick = { showAddEntry = true },
                onInspirationClick = { showInspirationPopover = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item { StatisticsHeader() }
                item {
                    this@LazyColumn.EntryList(
                        entries = filtered,
                        filterFavorites = showFavoritesOnly,
                        onEntryClick = onEntryClick,
                        onToggleFavorite = { entry ->
                            viewModel.updateEntry(entry.copy(isFavorite = !entry.isFavorite))
                        },
                        onDelete = { entry ->
                            viewModel.deleteEntry(entry.id)
                        }
                    )
                }
            }
            GradientOverlay(
                height = 80.dp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (showInspirationPopover) {
            InspirationPopoverPlaceholder(onDismiss = {
                showInspirationPopover = false
            })
        }
    }
}
