package com.example.echojournal.ui.components.mainflow.statisticsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
import com.example.echojournal.ui.components.mainflow.entryListScreen.ShadowCard
import com.example.echojournal.ui.theme.ColorManager
import com.example.echojournal.ui.viewModel.PrefsViewModel
import com.example.echojournal.ui.viewModel.StatisticsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TotalEntriesStatistic(
    modifier: Modifier = Modifier,
    statsViewModel: StatisticsViewModel = koinViewModel()
) {
    // Echo-Farbe holen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    // Werte aus dem ViewModel holen
    val daysWithEntries by statsViewModel.daysWithEntries.collectAsState()
    val totalWords by statsViewModel.totalWords.collectAsState()
    val totalDuration by statsViewModel.totalDuration.collectAsState()

    // Hier landet der Weight-Modifier aus StatisticsScreen:
    ShadowCard(
        onClick = { /* ggf. Klick-Aktion */ },
        elevation = 4.dp,
        modifier = modifier,
        fillHeight = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Überschrift
            Text(
                text = stringResource(R.string.statistics_total_title),
                style = MaterialTheme.typography.titleMedium
            )

            // Einträge KPI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = stringResource(R.string.statistics_total_days_desc),
                        tint = echoColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.statistics_total_days, daysWithEntries),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Wörter und Dauer KPI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                // --- KPI 2: Gesamt-Wörter ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = stringResource(R.string.statistics_total_words_desc),
                        tint = echoColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.statistics_total_words, totalWords),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                        )
                    )
                }

                // --- KPI 3: Gesamt-Dauer ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = stringResource(R.string.statistics_total_minutes_desc),
                        tint = echoColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.statistics_total_minutes, totalDuration),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                        )
                    )
                }
            }
        }
    }
}
