package com.example.echojournal.data.repository

import android.content.Context
import com.example.echojournal.data.remote.model.User

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

    fun signOut()

    suspend fun getCurrentUser(): User?

    suspend fun signInWithGoogleOneTap(context: Context): Result<User>
}