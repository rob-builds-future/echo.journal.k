package com.example.echojournal.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.data.repository.TranslationApiRepo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@OptIn(FlowPreview::class)
class TranslationViewModel(
    private val translationRepository: TranslationApiRepo,
    private val prefsViewModel: PrefsViewModel
) : ViewModel() {

    // State für die übersetzte Ausgabe
    private val _translatedText = MutableStateFlow("")
    val translatedText: StateFlow<String> = _translatedText.asStateFlow()

    // SharedFlow für Texteingaben zum Debouncen
    private val textInput = MutableSharedFlow<String>(replay = 1)

    init {
        // hier später fetchUserPreferredLanguage() aufrufen, wenn UserAuthRepo verfügbar ist

        // Collector für debounced Input
        viewModelScope.launch {
            textInput
                .debounce(500)              // 500 ms warten nach letzter Eingabe
                .filter { it.isNotBlank() } // leere Eingaben ignorieren
                .collectLatest { txt ->
                    Log.d("TranslationVM", "debounced txt = $txt")
                    translateText(txt)
                }
        }
    }

    /**
     * Muss von der UI aufgerufen werden, wenn sich der Eingabetext ändert.
     */
    fun onTextChanged(newText: String) {
        viewModelScope.launch {
            Log.d("TranslationVM", "onTextChanged: $newText")
            textInput.emit(newText)
        }
    }

    /**
     * Führt die API-Übersetzung durch und aktualisiert `_translatedText`.
     */
    private suspend fun translateText(text: String) {
        Log.d("TranslationVM", "Translating text: $text")
        val targetLang = prefsViewModel.currentLanguage.value.lowercase()
        val sourceLang = prefsViewModel.sourceLanguage.value.lowercase()
        runCatching {
            translationRepository.translate(
                text = text,
                from = sourceLang,
                to = targetLang
            )
        }.onSuccess { result ->
            Log.d("TranslationVM", "Translated result = $result")
            _translatedText.value = result
        }.onFailure { err ->
            if (err is CancellationException) throw err
            Log.e("TranslationVM", "Error translating text", err)
        }
    }

    suspend fun translateOnce(text: String): String {
        val targetLang = prefsViewModel.currentLanguage.value.lowercase()
        val sourceLang = prefsViewModel.sourceLanguage.value.lowercase()
        return runCatching {
            translationRepository.translate(
                text = text,
                from = sourceLang,
                to = targetLang
            )
        }.getOrElse { err ->
            if (err is CancellationException) throw err
            Log.e("TranslationVM", "Error in translateOnce", err)
            // Bei Fehler einfach den Originaltext zurückgeben
            text
        }
    }
}
