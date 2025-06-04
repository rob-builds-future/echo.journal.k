package com.example.echojournal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneId

/**
 * StatisticsViewModel liest aus EntryViewModel die Liste aller JournalEntry und stellt daraus
 * ein Set<LocalDate> bereit, das an den CalendarView übergeben werden kann.
 */
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

    // Aktueller Streak (Tage in Folge bis heute)
    val currentStreak: StateFlow<Int> =
        entryDates
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
}