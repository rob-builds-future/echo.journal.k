package com.example.echojournal.ui.components.mainflow.statisticsScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.echojournal.R
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

    // Streak-Werte aus VM
    val hasEntryToday by statsViewModel.hasEntryToday.collectAsState()
    val visibleStreak  by statsViewModel.visibleStreak.collectAsState()
    val streakRes      by statsViewModel.streakMessageRes.collectAsState()

    var showInfoDialog by remember { mutableStateOf(false) }

    ShadowCard(
        onClick = { /* optional */ },
        elevation = 4.dp,
        modifier = modifier,
        fillHeight = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Titel
            Text(
                text = stringResource(R.string.statistics_streak_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            // Streak-Anzeige
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Whatshot,
                        contentDescription = stringResource(R.string.statistics_streak_contentdesc),
                        tint = echoColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.statistics_streak_value, visibleStreak),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                        ),
                        textAlign = TextAlign.Center,
                        color = if (hasEntryToday) echoColor else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    )

                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.statistics_streak_info_contentdesc)
                        )
                    }
                }
            }
        }
    }

    // Info-Popup
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = AlertDialogDefaults.shape
                ),
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text(text = stringResource(R.string.statistics_streak_title)) },
            text = { Text(text = stringResource(id = streakRes, visibleStreak)) },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text(text = stringResource(android.R.string.ok))
                }
            }
        )
    }
}
