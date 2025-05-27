package com.example.echojournal.ui.screens.mainflow

import ColorManager
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
import com.example.echojournal.ui.components.mainflow.entryListScreen.InspirationPopoverPlaceholder
import com.example.echojournal.ui.components.mainflow.entryListScreen.StatisticsHeader
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.EntryViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntryListScreen(
    onEntryClick: (JournalEntry) -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    // AuthViewModel und EntryViewModel holen
    val authViewModel: AuthViewModel = koinViewModel()
    val user by authViewModel.user.collectAsState()
    val viewModel: EntryViewModel = koinViewModel()
    val allEntries by viewModel.entries.collectAsState()
    // PrefsViewModel holen, um das aktuelle Theme auszulesen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    // Lokale UI-States
    var showFavoritesOnly by remember { mutableStateOf(false) }
    var showInspirationPopover by remember { mutableStateOf(false) }

    val title = buildAnnotatedString {
        // alles vor "echo"
        val userName = user?.username ?: "Dein"
        append("$userName’s ")
        // jetzt "echo" mit Farbe und bold
        withStyle(style = SpanStyle(color = echoColor, fontWeight = FontWeight.Bold)) {
            append("echo")
        }
    }

    Scaffold(
        topBar = {
            EntryListTopBar(
                title = title,
                onSettingsClick = onSettingsClick,
                onStatsClick = {}
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
            val displayed = remember(allEntries, showFavoritesOnly) {
                allEntries.filter { !showFavoritesOnly || it.isFavorite }
            }

            LazyColumn {
                item { StatisticsHeader() }

                items(displayed) { entry ->
                    EntryRow(
                        entry            = entry,
                        onClick          = { onEntryClick(entry) },
                        onToggleFavorite = { viewModel.updateEntry(entry.copy(isFavorite = !entry.isFavorite)) },
                        onDelete         = { viewModel.deleteEntry(entry.id) }
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
