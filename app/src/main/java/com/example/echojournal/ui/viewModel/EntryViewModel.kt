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

class EntryViewModel(
    private val authViewModel: AuthViewModel,
    private val journalRepo: JournalRepo,
    private val translationApiRepo: TranslationApiRepo
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
                com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED) {
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

    /** Erstellt einen neuen Eintrag inkl. Übersetzung. */
    fun createEntry(
        rawContent: String,
        duration: Int,
        sourceLang: String,
        targetLang: String,
        createdAt: Timestamp
    ) {
        viewModelScope.launch {
            _createResult.value = runCatching {
                val userId = authViewModel.user.value?.id
                    ?: throw IllegalStateException("User nicht authentifiziert")
                val translatedText = translationApiRepo.translate(
                    text = rawContent,
                    from = sourceLang,
                    to = targetLang
                )
                val entry = JournalEntry(
                    userId = userId,
                    content = rawContent,
                    translatedContent = translatedText,
                    sourceLang = sourceLang,
                    targetLang = targetLang,
                    duration = duration,
                    favorite = false,
                    createdAt = createdAt
                )
                journalRepo.createEntry(userId, entry)
            }
        }
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
    fun clearCreateResult() {
        _createResult.value = null
    }

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
    init {
        viewModelScope.launch {
            entries.collect { updatedEntries ->
                // Logge vorab, welche Values hier ankommen
                updatedEntries.forEach { entry ->
                }
                _localEntries.value = updatedEntries
            }
        }
    }
}
