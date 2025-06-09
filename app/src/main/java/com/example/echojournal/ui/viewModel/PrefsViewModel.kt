package com.example.echojournal.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.R
import com.example.echojournal.data.repository.PrefsRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrefsViewModel(
    application: Context,
    private val prefsRepo: PrefsRepo
) : ViewModel() {

    val onboarded: StateFlow<Boolean> = prefsRepo.onboarded
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val theme: StateFlow<String> = prefsRepo.theme
        .stateIn(viewModelScope, SharingStarted.Eagerly, application.getString(R.string.default_theme))
    val currentLanguage = prefsRepo.currentLanguageCode
        .stateIn(viewModelScope, SharingStarted.Eagerly, "en")
    val sourceLanguage: StateFlow<String> = prefsRepo.sourceLanguageCode
        .stateIn(viewModelScope, SharingStarted.Eagerly, "auto")
    val username: StateFlow<String> = prefsRepo.username
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val currentTemplate: StateFlow<String> = prefsRepo.currentTemplateName
        .stateIn(viewModelScope, SharingStarted.Eagerly, application.getString(R.string.template_none))
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

    // Für das Abrufen (wird von einem suspend-Scope aus benutzt, z.B. LaunchedEffect)
    suspend fun getLastCongratsDate(): String? {
        return prefsRepo.getLastCongratsDate()
    }

    // Für das Setzen, wie die anderen Einstellungen (übernimmt selbst das launch)
    fun setLastCongratsDate(date: String) {
        viewModelScope.launch {
            prefsRepo.setLastCongratsDate(date)
        }
    }
}
