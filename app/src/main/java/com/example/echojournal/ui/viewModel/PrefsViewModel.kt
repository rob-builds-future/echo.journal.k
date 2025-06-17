package com.example.echojournal.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.R
import com.example.echojournal.data.repository.PrefsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrefsViewModel(
    application: Context,
    private val prefsRepo: PrefsRepo
) : ViewModel() {

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

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> get() = _loading

    private val _onboarded = MutableStateFlow<Boolean?>(null)
    val onboarded: StateFlow<Boolean?> = _onboarded

    init {
        viewModelScope.launch {
            prefsRepo.onboarded.collect { value ->
                _onboarded.value = value
                _loading.value = false
            }
        }
    }

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
}
