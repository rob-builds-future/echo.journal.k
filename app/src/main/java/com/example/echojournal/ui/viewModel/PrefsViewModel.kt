package com.example.echojournal.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.data.repository.PrefsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel für Onboarding-Status.
 */
class PrefsViewModel(
    private val prefsRepo: PrefsRepo
) : ViewModel() {
    private val _onboarded = MutableStateFlow(false)
    val onboarded: StateFlow<Boolean> = _onboarded.asStateFlow()

    init {
        viewModelScope.launch {
            _onboarded.value = prefsRepo.isOnboarded()
        }
    }

    fun setOnboarded(value: Boolean) {
        viewModelScope.launch {
            prefsRepo.setOnboarded(value)
            _onboarded.value = value
        }
    }
}