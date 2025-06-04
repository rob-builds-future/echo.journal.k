package com.example.echojournal.ui.components.mainflow.statisticsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.mainflow.entryListScreen.ShadowCard
import com.example.echojournal.ui.theme.ColorManager
import com.example.echojournal.ui.viewModel.PrefsViewModel
import com.example.echojournal.ui.viewModel.StatisticsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StreakStatistic(
    modifier: Modifier = Modifier,
    statsViewModel: StatisticsViewModel = koinViewModel()
) {
    // Echo-Farbe holen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    // Streak aus VM
    val currentStreak by statsViewModel.currentStreak.collectAsState()

    ShadowCard(
        onClick = { /* optional */ },
        elevation = 4.dp,
        modifier = modifier,
        fillHeight = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Überschrift
            Text(
                text = "Aktuell",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // In der Column werden Icon und Text vertikal gestapelt und zentriert
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Whatshot,
                        contentDescription = "Aktuelle Streak",
                        tint = echoColor,
                        modifier = Modifier.size(24.dp)
                    )

                    // Vertikaler Abstand zwischen Icon und Text
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$currentStreak-Tages\nStreak",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}