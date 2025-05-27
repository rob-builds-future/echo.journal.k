package com.example.echojournal.data.repository

import kotlinx.coroutines.flow.Flow

interface PrefsRepo {
    val onboarded: Flow<Boolean>
    suspend fun setOnboarded(value: Boolean)

    val theme: Flow<String>
    suspend fun setTheme(value: String)
}