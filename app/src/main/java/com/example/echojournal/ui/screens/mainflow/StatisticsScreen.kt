package com.example.echojournal.ui.screens.mainflow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.mainflow.statisticsScreen.CalendarView
import com.example.echojournal.ui.components.mainflow.statisticsScreen.TotalEntriesStatistic
import com.example.echojournal.ui.viewModel.AuthViewModel
import com.example.echojournal.ui.viewModel.StatisticsViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onBack: () -> Unit,
    statsViewModel: StatisticsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    // 1. Alle Journal-Eintrag-Dates
    val entryDates by statsViewModel.entryDates.collectAsState()

    // 2. Aktuellen User vom AuthViewModel
    val user by authViewModel.user.collectAsState()

    // 3. Wenn user != null, wandeln wir user.createdAt (Firebase-Timestamp) zu LocalDate um
    val minDate: LocalDate? = user
        ?.createdAt           // com.google.firebase.Timestamp
        ?.toInstant()         // → Instant
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDate()       // → LocalDate

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Deine Statistiken") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Zurück")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TotalEntriesStatistic()

                // 4. CalendarView erhält jetzt das tatsächliche „Mitgliedsdatum“ als minDate
                CalendarView(
                    entryDates = entryDates,
                    minDate = minDate,
                    onDayClick = { date ->
                        // Optional: reagiert, wenn der Nutzer auf einen Datumstag klickt
                    },
                    cellSize = 36.dp
                )
            }
        }
    )
}