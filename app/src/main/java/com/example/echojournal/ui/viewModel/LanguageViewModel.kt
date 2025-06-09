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
            list.map { dto ->
                val tag = LanguageUtil.mapLibreToBcp47(dto.code)
                val locale = Locale.forLanguageTag(tag)

                // Statt nur den Sprachnamen, inkl. Region:
                val raw = locale.getDisplayName(uiLocale)

                // Fallback, falls getDisplayName unerwartet leer ist:
                val baseName = raw.takeUnless {
                    it.isBlank() || it.equals(dto.code, ignoreCase = true)
                } ?: dto.name

                // Ersten Buchstaben groß:
                val displayName = baseName.replaceFirstChar { ch ->
                    if (ch.isLowerCase()) ch.titlecase(uiLocale) else ch.toString()
                }

                dto.copy(name = displayName)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
