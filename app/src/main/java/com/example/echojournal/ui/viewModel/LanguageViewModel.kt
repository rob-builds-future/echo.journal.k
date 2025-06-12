package com.example.echojournal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.data.repository.TranslationApiRepo
import com.example.echojournal.util.LanguageUtil
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

class LanguageViewModel(
    private val translationApi: TranslationApiRepo
) : ViewModel() {

    private val _languages = flow {
        emit(translationApi.getSupportedLanguages())
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val localizedLanguages = _languages
        .map { list ->
            val uiLocale = Locale.getDefault()

            // 1. Filter ungültige/unsinnige Codes raus:
            val filtered = list.filter { dto ->
                val tag = LanguageUtil.mapLibreToBcp47(dto.code)
                val locale = Locale.forLanguageTag(tag)
                val languageName = locale.getDisplayLanguage(uiLocale)
                languageName.isNotBlank() &&
                        dto.code.length in 2..7 &&
                        !languageName.equals(dto.code, ignoreCase = true)
            }

            // 2. Jetzt die Sprache zählen (nur im gefilterten!)
            val languageCount = filtered
                .map { Locale.forLanguageTag(LanguageUtil.mapLibreToBcp47(it.code)).language }
                .groupingBy { it }
                .eachCount()

            // 3. Mapping auf displayName
            filtered.map { dto ->
                val tag = LanguageUtil.mapLibreToBcp47(dto.code)
                val locale = Locale.forLanguageTag(tag)
                val language = locale.language
                val languageName = locale.getDisplayLanguage(uiLocale)
                val regionName = locale.getDisplayCountry(uiLocale)

                val displayName = if ((languageCount[language]
                        ?: 0) > 1 && regionName.isNotBlank()
                ) {
                    "$languageName ($regionName)"
                } else {
                    languageName
                }.replaceFirstChar { ch ->
                    if (ch.isLowerCase()) ch.titlecase(uiLocale) else ch.toString()
                }

                dto.copy(name = displayName)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}
