package com.example.echojournal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.data.repository.TranslationApiRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class LanguageViewModel(
    private val translationApi: TranslationApiRepo
): ViewModel() {

    val languages = flow {
        emit(translationApi.getSupportedLanguages())  // you already implemented this
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}

