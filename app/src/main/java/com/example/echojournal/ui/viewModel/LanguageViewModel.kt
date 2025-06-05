package com.example.echojournal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.data.remote.model.util.LanguageDto
import com.example.echojournal.data.repository.TranslationApiRepo
import com.example.echojournal.util.LanguageUtil
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LanguageViewModel(
    private val translationApi: TranslationApiRepo
) : ViewModel() {

    /**
     * Liste aller Sprachen, wie sie die API liefert:
     * z.B. [ { code="en", name="English" }, { code="de", name="Deutsch" }, { code="pb", name="Português (Brasil)" }, … ]
     */
    private val _languages: StateFlow<List<LanguageDto>> = flow {
        emit(translationApi.getSupportedLanguages())
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Öffentlich zugänglich: die Rohdaten
    val languages: StateFlow<List<LanguageDto>>
        get() = _languages

    /**
     * Map von Libre-Code → BCP-47-Tag, automatisch erzeugt
     * aus allen Codes in `_languages`.
     *
     * Beispiel:
     *   { "en" -> "en-US", "de" -> "de-DE", "pb" -> "pt-BR", … }
     */
    val codeToBcp47: StateFlow<Map<String, String>> = _languages
        .map { list ->
            list.associate { dto ->
                // Für jeden Eintrag in "list" wenden wir unsere Utility an:
                dto.code to LanguageUtil.mapLibreToBcp47(dto.code)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())
}
