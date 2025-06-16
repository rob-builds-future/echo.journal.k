package com.example.echojournal.ui.screens.mainflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.echojournal.R
import com.example.echojournal.ui.components.mainflow.entryListScreen.ShadowCard
import com.example.echojournal.ui.components.mainflow.statisticsScreen.CalendarView
import com.example.echojournal.ui.components.mainflow.statisticsScreen.StreakStatistic
import com.example.echojournal.ui.components.mainflow.statisticsScreen.TopWordsStatistic
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

    // 2. Aktuellen User
    val user by authViewModel.user.collectAsState()

    // 3. Mitgliedsdatum zu LocalDate konvertieren
    val minDate: LocalDate? = user
        ?.createdAt
        ?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDate()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.statistics_title),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.statistics_nav_back))
                    }
                }
            )
        },
        content = { paddingValues ->
            // Wir verwenden ein Box-Layout, um alles zu stapeln
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Abstand einhalten, den Scaffold vorgibt
            ) {
                // 1. Der scrollbare Content im Hintergrund
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1/3: Streak
                        StreakStatistic(
                            statsViewModel = statsViewModel,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        // 2/3: Gesamterfolge
                        TotalEntriesStatistic(
                            statsViewModel = statsViewModel,
                            modifier = Modifier.weight(2f)
                        )
                    }

                    // Kalender‐Card
                    ShadowCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        onClick = { /* nicht klickbar */ },
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 8.dp, horizontal = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.statistics_tracker_title),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            CalendarView(
                                entryDates = entryDates,
                                minDate = minDate,
                                onDayClick = { /* … */ },
                                cellSize = 36.dp
                            )
                        }
                    }

                    TopWordsStatistic()
                    // (Falls euer Content noch länger ist, wird er hier automatisch scrollen)
                }

                // Gradient Overlay oben
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .align(Alignment.TopCenter)
                        .zIndex(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.background,
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                // Gradient Overlay unten
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(32.dp)
                            .zIndex(1f)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    )
                }
            }
        }
    )
}