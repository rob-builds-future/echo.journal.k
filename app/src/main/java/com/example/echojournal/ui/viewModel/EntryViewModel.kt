package com.example.echojournal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.data.remote.model.JournalEntry
import com.example.echojournal.data.repository.JournalRepo
import com.example.echojournal.data.repository.TranslationApiRepo
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class EntryViewModel(
    private val authViewModel: AuthViewModel,
    private val journalRepo: JournalRepo,
    private val translationApiRepo: TranslationApiRepo,
) : ViewModel() {

    private val userFlow = authViewModel.user
        .map { it?.id }            // liefert jetzt String? – also auch null
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    val entries: StateFlow<List<JournalEntry>> = userFlow
        .flatMapLatest { uid ->
            if (uid != null) {
                journalRepo.observeEntries(uid)
            } else {
                emptyFlow()
            }
        }
        .catch { e ->
            // Wenn Firestore uns die Berechtigung entzieht, ignoriere das
            if (e is FirebaseFirestoreException && e.code ==
                com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED
            ) {
                emit(emptyList())
            } else {
                throw e
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    private val _createResult = MutableStateFlow<Result<Unit>?>(null)
    val createResult: StateFlow<Result<Unit>?> = _createResult.asStateFlow()

    private val _updateResult = MutableStateFlow<Result<Unit>?>(null)
    val updateResult: StateFlow<Result<Unit>?> = _updateResult.asStateFlow()

    private val _deleteResult = MutableStateFlow<Result<Unit>?>(null)
    val deleteResult: StateFlow<Result<Unit>?> = _deleteResult.asStateFlow()

    private val _localEntries = MutableStateFlow<List<JournalEntry>>(emptyList())
    val localEntries: StateFlow<List<JournalEntry>> = _localEntries.asStateFlow()

    private val _justExtendedStreak = MutableStateFlow(false)
    val justExtendedStreak: StateFlow<Boolean> = _justExtendedStreak.asStateFlow()

    // Intern: letzter bekannter Streak
    private var lastStreak = 0

    init {
        // Nur laden, kein Dialog auslösen
        viewModelScope.launch {
            entries.collect { list ->
                _localEntries.value = list
            }
        }
    }

    /** Erstellt einen neuen Eintrag inkl. Übersetzung. */
    fun createEntry(
        rawContent: String,
        duration: Int,
        sourceLang: String,
        targetLang: String,
        createdAt: Timestamp
    ) {
        viewModelScope.launch {
            // Persistieren + Ergebnis speichern
            val result = runCatching {
                val userId = authViewModel.user.value?.id
                    ?: throw IllegalStateException("User nicht authentifiziert")
                val translated = translationApiRepo.translate(
                    text = rawContent, from = sourceLang, to = targetLang
                )
                val entry = JournalEntry(
                    userId = userId,
                    content = rawContent,
                    translatedContent = translated,
                    sourceLang = sourceLang,
                    targetLang = targetLang,
                    duration = duration,
                    favorite = false,
                    createdAt = createdAt
                )
                journalRepo.createEntry(userId, entry)
            }
            _createResult.value = result
            result.onSuccess {
                _justExtendedStreak.value = true
            }
        }
    }

    private fun calculateStreak(dates: Set<LocalDate>): Int {
        var streak = 0
        var day = LocalDate.now()
        while (dates.contains(day)) {
            streak++
            day = day.minusDays(1)
        }
        return streak
    }

    /**
     * Markiert, dass die Streak durch einen neuen Eintrag verlängert wurde.
     * Wird nach erfolgreichem Erstellen eines Eintrags vom UI ausgelöst.
     */
    fun extendStreak() {
        _justExtendedStreak.value = true
    }

    /**
     * Setzt das Create-Result zurück.
     */
    fun clearCreateResult() {
        _createResult.value = null
    }

    /**
     * Setzt das Extended-Streak-Flag zurück.
     */
    fun clearExtendedStreakFlag() {
        _justExtendedStreak.value = false
    }

    /** Aktualisiert einen bestehenden Eintrag. */
    fun updateEntry(entry: JournalEntry) {
        viewModelScope.launch {
            _updateResult.value = runCatching {
                journalRepo.updateEntry(entry.userId, entry)
            }
        }
    }

    /** Löscht einen Eintrag. */
    fun deleteEntry(entryId: String) {
        viewModelScope.launch {
            _deleteResult.value = runCatching {
                val userId = authViewModel.user.value?.id
                    ?: throw IllegalStateException("User nicht authentifiziert")
                journalRepo.deleteEntry(userId, entryId)
            }
        }
    }

    /** Reset-Methoden für die Ergebnisse. */

    fun clearUpdateResult() {
        _updateResult.value = null
    }

    fun clearDeleteResult() {
        _deleteResult.value = null
    }

    fun addDurationToEntry(entry: JournalEntry, extraMinutes: Int) {
        viewModelScope.launch {
            val updated = entry.copy(
                duration = entry.duration + extraMinutes,
                updatedAt = Timestamp.now()
            )
            journalRepo.updateEntry(entry.userId, updated)
        }
    }

    fun toggleFavorite(entry: JournalEntry) {
        // 1) Lokales “optimistic” Update für die UI
        val updatedEntry = entry.copy(favorite = !entry.favorite)
        _localEntries.value = _localEntries.value.map {
            if (it.id == updatedEntry.id) updatedEntry else it
        }

        // 2) Asynchrone Persistenz in Firestore
        viewModelScope.launch {
            runCatching {
                journalRepo.updateEntry(updatedEntry.userId, updatedEntry)
            }.onFailure {
                // Optional: Beim Fehler das lokale Flag wieder zurückrollen oder eine Fehlermeldung anzeigen
                _localEntries.value = _localEntries.value.map { current ->
                    if (current.id == entry.id) entry else current
                }
            }
        }
    }
}
