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

    // 1. Basis: Flow aller Einträge
    private val entriesFlow = entryViewModel.entries

    // 2. entryDates = Set aller LocalDate, an denen ein Journal-Eintrag existiert
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

    // 3. minEntryDate = das früheste Datum aus entryDates (oder null, wenn keine Einträge)
    val minEntryDate: StateFlow<LocalDate?> =
        entryDates
            .map { dates ->
                dates.minOrNull()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )
}
