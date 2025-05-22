package com.example.echojournal.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.data.remote.model.User
import com.example.echojournal.data.repository.UserAuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel für Sign In / Sign Up.
 */
class AuthViewModel(
    private val repo: UserAuthRepo,
    private val context: Context
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val username = MutableStateFlow("")
    val preferredLanguage = MutableStateFlow("EN")

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        // Versuch, beim Start einen eingeloggten Nutzer zu laden
        viewModelScope.launch {
            _loading.value = true
            _user.value = repo.getCurrentUser()
            _loading.value = false
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            repo.signUp(
                email = email.value,
                password = password.value,
                username = username.value,
                preferredLanguage = preferredLanguage.value
            ).onSuccess {
                _user.value = it
            }.onFailure {
                _error.value = it.message
            }
            _loading.value = false
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            repo.signIn(
                email = email.value,
                password = password.value
            ).onSuccess {
                _user.value = it
            }.onFailure {
                _error.value = it.message
            }
            _loading.value = false
        }
    }

    fun signOut() {
        repo.signOut()
        _user.value = null
    }

    /** startet den One-Tap-Flow */
    fun signInWithGoogleOneTap() {
        viewModelScope.launch {
            _loading.value = true
            _error.value   = null

            repo.signInWithGoogleOneTap(context)
                .onSuccess { _user.value = it }
                .onFailure { _error.value = it.message }

            _loading.value = false
        }
    }
}