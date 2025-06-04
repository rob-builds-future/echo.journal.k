package com.example.echojournal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.data.repository.PrefsRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrefsViewModel(
    private val prefsRepo: PrefsRepo
) : ViewModel() {

    val onboarded: StateFlow<Boolean> = prefsRepo.onboarded
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val theme: StateFlow<String> = prefsRepo.theme
        .stateIn(viewModelScope, SharingStarted.Eagerly, "Wolkenlos")
    val currentLanguage = prefsRepo.currentLanguageCode
        .stateIn(viewModelScope, SharingStarted.Eagerly, "en")
    val sourceLanguage: StateFlow<String> = prefsRepo.sourceLanguageCode
        .stateIn(viewModelScope, SharingStarted.Eagerly, "de")
    val username: StateFlow<String> = prefsRepo.username
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val currentTemplate: StateFlow<String> = prefsRepo.currentTemplateName
        .stateIn(viewModelScope, SharingStarted.Eagerly, "Keine Vorlage")
    val savedReminders: StateFlow<Map<String, Pair<Boolean, String>>> =
        prefsRepo.getReminderSettings()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())


    fun setOnboarded(value: Boolean) {
        viewModelScope.launch { prefsRepo.setOnboarded(value) }
    }

    fun setTheme(value: String) {
        viewModelScope.launch { prefsRepo.setTheme(value) }
    }

    fun setLanguage(code: String) = viewModelScope.launch {
        prefsRepo.setLanguageCode(code)
    }

    fun setUsername(name: String) = viewModelScope.launch {
        prefsRepo.setUsername(name)
    }

    fun setTemplate(name: String) {
        viewModelScope.launch {
            prefsRepo.setTemplateName(name)
        }
    }

    fun setReminderEnabled(label: String, enabled: Boolean) {
        viewModelScope.launch {
            prefsRepo.updateReminderEnabled(label, enabled)
        }
    }

    fun setReminderTime(label: String, time: String) {
        viewModelScope.launch {
            prefsRepo.updateReminderTime(label, time)
        }
    }
}
