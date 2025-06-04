package com.example.echojournal.ui.components.mainflow.statisticsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.echojournal.ui.components.mainflow.entryListScreen.ShadowCard
import com.example.echojournal.ui.theme.ColorManager
import com.example.echojournal.ui.viewModel.EntryViewModel
import com.example.echojournal.ui.viewModel.PrefsViewModel
import com.example.echojournal.ui.viewModel.TranslationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TopWordsStatistic(
    entryViewModel: EntryViewModel = koinViewModel(),
    translationViewModel: TranslationViewModel = koinViewModel(),
    prefsViewModel: PrefsViewModel = koinViewModel()
) {
    // Echo-Farbe aus den Preferences holen
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    // Alle Einträge sammeln
    val entries by entryViewModel.entries.collectAsState()

    //val stemmer = remember { PorterStemmerEn() }

    // Worthäufigkeiten berechnen (Wörter mit mindestens 4 Buchstaben)
    val topWordEntries = remember(entries) {
        entries
            .flatMap { entry ->
                entry.content
                    .split("\\s+".toRegex())
                    .map { raw ->
                        raw
                            .trim()
                            .lowercase()
                            // Entfernt führende und abschließende Nicht-Buchstaben
                            .replace(Regex("^[^\\p{L}]+|[^\\p{L}]+\$"), "")
                    }
                    .filter { it.length >= 4 }
                    //.map { raw -> stemmer.stem(raw) }
            }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(30)
    }


    // 3. Seitenanzahl (10 Wörter pro Seite)
    val pageCount = (topWordEntries.size + 9) / 10
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pageCount }
    )

    // 4. In ShadowCard verpacken
    ShadowCard(
        onClick = { /* nicht klickbar */ },
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Überschrift
            Text(
                text = "Top Wörter",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 5. HorizontalPager für Seiten
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val startIndex = page * 10
                val endIndex = minOf(startIndex + 10, topWordEntries.size)
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    for (i in startIndex until endIndex) {
                        val (rawWord, count) = topWordEntries[i]
                        // Großschreiben: nur erster Buchstabe
                        val displayWord = rawWord.replaceFirstChar { it.uppercaseChar() }

                        // Übersetzung holen
                        var translation by remember { mutableStateOf<String?>(null) }
                        LaunchedEffect(rawWord) {
                            translation = translationViewModel.translateOnce(rawWord)
                        }

                        // Zeige Original und Übersetzung in einer Zeile
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$displayWord ($count)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            translation?.let {
                                Text(
                                    text = it.replaceFirstChar { c -> c.uppercaseChar() },
                                    color = echoColor,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pageCount) { index ->
                    val color = if (pagerState.currentPage == index)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(8.dp)
                            .background(color, shape = CircleShape)
                    )
                }
            }
        }
    }
}
