package com.example.echojournal.ui.viewModel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echojournal.R
import com.example.echojournal.data.remote.model.JournalEntry
import com.example.echojournal.data.remote.model.User
import com.example.echojournal.data.repository.UserAuthRepo
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: UserAuthRepo,
    private val context: Context,
    private val prefsViewModel: PrefsViewModel
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val username = MutableStateFlow("")
    val preferredLanguage = MutableStateFlow("EN")

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _journalEntries = MutableStateFlow<List<JournalEntry>>(emptyList())
    val journalEntries: StateFlow<List<JournalEntry>> = _journalEntries.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /** löscht die aktuelle Fehlermeldung */
    fun clearError() {
        _error.value = null
    }

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            _loading.value = true

            // Debug: Loggen, ob zur Startzeit schon ein User drin ist
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser == null) {
                Log.d("AuthViewModel", "Init: Kein Firebase-User beim Start.")
            } else {
                Log.d("AuthViewModel", "Init: Firebase-User vorhanden: UID = ${firebaseUser.uid}")
            }

            // Dann lädst du den Firestore-User
            _user.value = repo.getCurrentUser().also {
                it?.let { u -> Log.d("AuthViewModel", "getCurrentUser() gibt User zurück mit UID ${u.id}") }
            }
            _user.value?.username?.let { prefsViewModel.setUsername(it) }

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

    fun updateUsername(newName: String) {
        val currentUser = _user.value ?: return
        viewModelScope.launch {
            try {
                // 1) Firestore updaten
                repo.updateUsername(currentUser.id, newName)
                // 2) lokalen StateFlow updaten
                _user.value = currentUser.copy(username = newName)
                // 3) PrefsViewModel aktualisieren – damit alle Composables, die nur PrefsViewModel.username
                //    lesen, sofort recomposen.
                prefsViewModel.setUsername(newName)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "UpdateUsername fehlgeschlagen", e)
            }
        }
    }

    fun signOut() {
        repo.signOut()
        _user.value = null
    }

    fun signInWithGoogleOneTap(activity: Activity) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            repo.signInWithGoogleOneTap(activity)
                .onSuccess { newUser ->
                    _user.value = newUser
                    Log.d("AuthViewModel", "signInWithGoogleOneTap.onSuccess: UID = ${newUser.id}")

                    loadJournalEntriesForCurrentUser()
                    prefsViewModel.setUsername(newUser.username)
                }
                .onFailure { ex ->
                    _error.value = ex.message
                    Log.e("AuthViewModel", "signInWithGoogleOneTap fehlgeschlagen", ex)
                }

            _loading.value = false
        }
    }

    fun loadJournalEntriesForCurrentUser() {
        // DIREKTEN Zugriff nehmen – kein suspend nötig:
        val firebaseAuthUser = FirebaseAuth.getInstance().currentUser
        if (firebaseAuthUser == null) {
            Log.w("AuthViewModel", "Kein angemeldeter User: journalEntries werden NICHT geladen.")
            return
        }

        // Jetzt haben wir eine echte FirebaseUser-UID, kein suspend-Aufruf:
        val uid = firebaseAuthUser.uid
        Log.d("AuthViewModel", "User ist angemeldet. Lade journalEntries für UID = $uid")

        // Ab hier in einer Coroutine die eigentliche Firestore-Abfrage machen:
        viewModelScope.launch {
            try {
                val entries = repo.getJournalEntries(uid)
                _journalEntries.value = entries
                Log.d("AuthViewModel", "JournalEntries erfolgreich geladen: ${entries.size} Einträge.")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Fehler beim Laden der journalEntries", e)
                _error.value = "Konnte Journal-Einträge nicht laden."
            }
        }
    }
}