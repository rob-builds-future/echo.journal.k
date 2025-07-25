package com.rbf.echojournal.ui.components.mainflow.statisticsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rbf.echojournal.R
import com.rbf.echojournal.ui.theme.ColorManager
import com.rbf.echojournal.ui.viewModel.PrefsViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle

@Composable
fun CalendarView(
    entryDates: Set<LocalDate>,
    minDate: LocalDate? = null,
    onDayClick: (LocalDate) -> Unit = {},
    cellSize: Dp = 36.dp
) {
    // 1.) Das heutige Datum
    val today = LocalDate.now()

    // 2.) Echo-Farbe aus den Preferences holen
    val prefsViewModel: PrefsViewModel = koinViewModel()
    val themeName by prefsViewModel.theme.collectAsState()
    val echoColor = ColorManager.getColor(themeName)

    val context = LocalContext.current
    // Aktuelle Locale aus den System-Einstellungen
    val locale = context.resources.configuration.locales.get(0)

    // 3.) Zustand für den aktuell angezeigten Monat (YearMonth)
    var displayedMonth by remember { mutableStateOf(YearMonth.of(today.year, today.month)) }

    // 4.) Den YearMonth des minDate ermitteln (falls vorhanden)
    val minEntryMonth: YearMonth? = minDate?.let { YearMonth.from(it) }

    // 5.) Wenn minEntryMonth sich ändert, stelle sicher, dass displayedMonth ≥ minEntryMonth
    LaunchedEffect(minEntryMonth) {
        minEntryMonth?.let { minYM ->
            if (displayedMonth.isBefore(minYM)) {
                displayedMonth = minYM
            }
        }
    }

    Column {
        // ─── Header mit Monatsnavigation ──────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // „Zurück“-Pfeil
            IconButton(
                onClick = { displayedMonth = displayedMonth.minusMonths(1) },
                enabled = minEntryMonth?.let { !displayedMonth.minusMonths(1).isBefore(it) } ?: true
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = stringResource(R.string.statistics_calendar_prev),
                    tint = if (minEntryMonth?.let { displayedMonth.minusMonths(1) < it } == true)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }

            // Monatsname + Jahr, z. B. „Mai 2025“
            Text(
                text = "${displayedMonth.month.getDisplayName(TextStyle.FULL, locale)} ${displayedMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // „Vorwärts“-Pfeil
            IconButton(
                onClick = { displayedMonth = displayedMonth.plusMonths(1) },
                enabled = displayedMonth.isBefore(YearMonth.of(today.year, today.month))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = stringResource(R.string.statistics_calendar_next),
                    tint = if (displayedMonth.isBefore(YearMonth.of(today.year, today.month)))
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        }

        // ─── Wochentags-Leiste (Mo, Di, Mi, …) ────────────────────────────
        val daysOfWeek = remember(locale) {
            DayOfWeek.values().map { dow ->
                dow.getDisplayName(TextStyle.SHORT, locale)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEach { weekdayLabel ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(cellSize)
                ) {
                    Text(
                        text = weekdayLabel,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // ─── Kalendertage im 7-Spalten-Raster ─────────────────────────────
        val firstOfMonth: LocalDate = displayedMonth.atDay(1)
        val firstWeekdayIndex = (firstOfMonth.dayOfWeek.value + 6) % 7
        val daysInMonth = displayedMonth.lengthOfMonth()
        val totalCells = firstWeekdayIndex + daysInMonth
        val rows = (totalCells + 6) / 7
        var dayCounter = 1

        repeat(rows) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(7) { colIndex ->
                    val cellIndex = rowIndex * 7 + colIndex
                    if (cellIndex < firstWeekdayIndex || dayCounter > daysInMonth) {
                        // Leere Zelle
                        Box(modifier = Modifier.size(cellSize))
                    } else {
                        val date = displayedMonth.atDay(dayCounter)
                        val isBeforeMin = minDate?.let { date.isBefore(it) } ?: false
                        val isMinDate = (minDate != null && date == minDate)
                        val isMarked = entryDates.contains(date)

                        // Hintergrundstyling (unverändert)
                        val bgModifier = when {
                            isBeforeMin -> Modifier.background(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                                shape = MaterialTheme.shapes.small
                            )
                            isMinDate -> Modifier.background(
                                color = Color(0xFFFFD700),
                                shape = MaterialTheme.shapes.small
                            )
                            isMarked -> Modifier.background(
                                color = echoColor,
                                shape = MaterialTheme.shapes.small
                            )
                            else -> Modifier
                        }

                        Box(
                            modifier = Modifier
                                .size(cellSize),
                            contentAlignment = Alignment.Center
                        ) {
                            // 1dp Padding, dann Hintergrund zeichnen
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 1.dp)
                                    .then(bgModifier)
                            )
                            // Text darüber
                            Text(
                                text = dayCounter.toString(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isMinDate) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = when {
                                    isBeforeMin -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                                    isMinDate   -> Color.Black
                                    isMarked    -> Color.White
                                    else        -> MaterialTheme.colorScheme.onBackground
                                }
                            )
                            // Klick-Overlay, falls nicht vor minDate
                            if (!isBeforeMin) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable { onDayClick(date) }
                                )
                            }
                        }
                        dayCounter++
                    }
                }
            }
        }
    }
}
