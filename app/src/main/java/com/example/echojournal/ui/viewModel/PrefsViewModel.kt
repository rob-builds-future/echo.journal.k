package com.example.echojournal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.data.repository.PrefsRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel für Onboarding-Prefs, persistent gespeichert via DataStore.
 */
class PrefsViewModel(
    private val prefsRepo: PrefsRepo
) : ViewModel() {

    // onboarded als StateFlow
    val onboarded: StateFlow<Boolean> = prefsRepo.onboarded
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // theme als StateFlow
    val theme: StateFlow<String> = prefsRepo.theme
        .stateIn(viewModelScope, SharingStarted.Eagerly, "Wolkenlos")

    val currentLanguage = prefsRepo.currentLanguageCode
        .stateIn(viewModelScope, SharingStarted.Eagerly, "en")
    val sourceLanguage: StateFlow<String> = prefsRepo.sourceLanguageCode
        .stateIn(viewModelScope, SharingStarted.Eagerly, "de")

    fun setOnboarded(value: Boolean) {
        viewModelScope.launch { prefsRepo.setOnboarded(value) }
    }

    fun setTheme(value: String) {
        viewModelScope.launch { prefsRepo.setTheme(value) }
    }

    fun setLanguage(code: String) = viewModelScope.launch {
        prefsRepo.setLanguageCode(code)
    }
}
