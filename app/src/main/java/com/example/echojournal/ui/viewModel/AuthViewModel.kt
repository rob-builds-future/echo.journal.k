package com.example.echojournal.ui.viewModel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.R
import com.example.echojournal.data.remote.model.User
import com.example.echojournal.data.repository.UserAuthRepo
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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

    /** löscht die aktuelle Fehlermeldung */
    fun clearError() {
        _error.value = null
    }

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

            // ─── Vorab-Validierung ────────────────────────
            if (username.value.isBlank()) {
                _error.value = context.getString(R.string.error_username_required)
                _loading.value = false
                return@launch
            }
            if (email.value.isBlank()) {
                _error.value = context.getString(R.string.error_email_required)
                _loading.value = false
                return@launch
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                _error.value = context.getString(R.string.error_invalid_email_format)
                _loading.value = false
                return@launch
            }
            if (password.value.isBlank()) {
                _error.value = context.getString(R.string.error_password_required)
                _loading.value = false
                return@launch
            }
            // Dein Regex: mindestens 8+ Zeichen, 1 Großbuchstabe, 1 Zahl
            val pwdOk = Regex("""^(?=.*[A-Z])(?=.*\d).{8,}$""")
                .matches(password.value)
            if (!pwdOk) {
                _error.value = context.getString(R.string.error_password_weak)
                _loading.value = false
                return@launch
            }

            repo.signUp(
                email = email.value,
                password = password.value,
                username = username.value,
                preferredLanguage = preferredLanguage.value
            ).onSuccess {
                _user.value = it
            }.onFailure { ex ->
                // Mappe die wichtigsten Firebase-Exceptions
                _error.value = when (ex) {
                    is FirebaseAuthUserCollisionException ->
                        context.getString(R.string.error_email_already_in_use)
                    is FirebaseAuthWeakPasswordException ->
                        context.getString(R.string.error_password_weak)
                    is FirebaseNetworkException ->
                        context.getString(R.string.error_no_network)
                    else ->
                        context.getString(R.string.error_generic)
                }
            }
            _loading.value = false
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            // 1) Vorab-Validierung
            if (email.value.isBlank()) {
                _error.value = context.getString(R.string.error_email_required)
                _loading.value = false
                return@launch
            }
            if (password.value.isBlank()) {
                _error.value = context.getString(R.string.error_password_required)
                _loading.value = false
                return@launch
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                _error.value = context.getString(R.string.error_invalid_email_format)
                _loading.value = false
                return@launch
            }

            // 2) Firebase-Call erst wenn die Felder valid sind
            repo.signIn(
                email = email.value,
                password = password.value
            ).onSuccess {
                _user.value = it
            }.onFailure { ex ->
                _error.value = when (ex) {
                    is FirebaseAuthInvalidUserException ->
                        context.getString(R.string.error_user_not_found)

                    is FirebaseAuthInvalidCredentialsException ->
                        context.getString(R.string.error_invalid_password)

                    is FirebaseAuthUserCollisionException ->
                        context.getString(R.string.error_email_already_in_use)

                    is FirebaseNetworkException ->
                        context.getString(R.string.error_no_network)

                    else ->
                        context.getString(R.string.error_generic)  // z.B. "Unbekannter Fehler, bitte erneut versuchen"
                }
            }
            _loading.value = false
        }
    }

    fun updatePreferredLanguage(newLang: String) {
        val current = _user.value ?: return
        viewModelScope.launch {
            // 1) Firestore updaten
            repo.updatePreferredLanguage(current.id, newLang)
            // 2) lokalen State aktualisieren
            _user.value = current.copy(preferredLanguage = newLang)
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
            _error.value = null

            repo.signInWithGoogleOneTap(context)
                .onSuccess { _user.value = it }
                .onFailure { _error.value = it.message }

            _loading.value = false
        }
    }
}