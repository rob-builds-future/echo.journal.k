package com.example.echojournal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.R
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneId

class StatisticsViewModel(
    entryViewModel: EntryViewModel
) : ViewModel() {

    // Basis: Flow aller Einträge
    private val entriesFlow = entryViewModel.entries

    // entryDates = Set aller LocalDate, an denen ein Journal-Eintrag existiert
    val entryDates: StateFlow<Set<LocalDate>> =
        entriesFlow
            .map { listOfEntries ->
                listOfEntries.mapNotNull { entry ->
                    entry.createdAt?.toDate()
                        ?.toInstant()
                        ?.atZone(ZoneId.systemDefault())
                        ?.toLocalDate()
                }.toSet()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptySet()
            )

    // daysWithEntries = Anzahl der eindeutigen Tage mit mindestens einem Eintrag
    val daysWithEntries: StateFlow<Int> =
        entryDates
            .map { it.size }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = 0
            )

    // totalWords = Summe aller Worte in allen Einträgen
    val totalWords: StateFlow<Int> =
        entriesFlow
            .map { listOfEntries ->
                listOfEntries.sumOf { entry ->
                    entry.content
                        .trim()
                        .split("\\s+".toRegex())
                        .count { it.isNotBlank() }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = 0
            )

    // totalDuration = Summe aller durations (in Minuten)
    val totalDuration: StateFlow<Int> =
        entriesFlow
            .map { listOfEntries ->
                listOfEntries.sumOf { it.duration }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = 0
            )


    // 1) Streak inklusive heute
    private val streakInclToday = entryDates
        .map { dates ->
            var streak = 0
            var day = LocalDate.now()
            while (dates.contains(day)) {
                streak++
                day = day.minusDays(1)
            }
            streak
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    // 2) Streak bis gestern
    private val streakUntilYesterday = entryDates
        .map { dates ->
            var streak = 0
            var day = LocalDate.now().minusDays(1)
            while (dates.contains(day)) {
                streak++
                day = day.minusDays(1)
            }
            streak
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    // 3) Flag: heute schon geschrieben?
    val hasEntryToday = entryDates
        .map { it.contains(LocalDate.now()) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // 4) Sichtbarer Streak: je nach Fall
    val visibleStreak = combine(hasEntryToday, streakInclToday, streakUntilYesterday) { today, incl, until ->
        if (today) incl else until
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val streakMessageRes: StateFlow<Int> = combine(hasEntryToday, visibleStreak) { today, streak ->
        when {
            today -> R.string.statistics_streak_message_today
            streak > 0 -> R.string.statistics_streak_message_not_today
            else -> R.string.statistics_streak_message_first
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, R.string.statistics_streak_message_first)
}