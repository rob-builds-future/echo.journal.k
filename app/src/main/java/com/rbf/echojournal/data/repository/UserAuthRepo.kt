package com.rbf.echojournal.data.repository

import com.rbf.echojournal.data.remote.model.JournalEntry
import com.rbf.echojournal.data.remote.model.User

/**
 * Abstraktion für Firebase-Auth & User-Profile-Management.
 */
interface UserAuthRepo {
    suspend fun signUp(
        email: String,
        password: String,
        username: String,
        preferredLanguage: String
    ): Result<User>

    suspend fun signIn(
        email: String,
        password: String
    ): Result<User>

    suspend fun updatePreferredLanguage(userId: String, newLang: String)

    suspend fun updateUsername(userId: String, newUsername: String)

    fun signOut()

    suspend fun getCurrentUser(): User?

    suspend fun signInWithGoogleOneTap(activity: android.app.Activity): Result<User>

    suspend fun getJournalEntries(userId: String): List<JournalEntry>

}