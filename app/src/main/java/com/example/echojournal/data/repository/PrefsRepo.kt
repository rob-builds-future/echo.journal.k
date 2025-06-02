package com.example.echojournal.data.repository

import kotlinx.coroutines.flow.Flow

interface PrefsRepo {
    val onboarded: Flow<Boolean>
    suspend fun setOnboarded(value: Boolean)

    val theme: Flow<String>
    suspend fun setTheme(value: String)

    val currentLanguageCode: Flow<String>
    suspend fun setLanguageCode(code: String)

    val sourceLanguageCode: Flow<String>
    suspend fun setSourceLanguageCode(code: String)

    val username: Flow<String>
    suspend fun setUsername(name: String)

    val currentTemplateName: Flow<String>
    suspend fun setTemplateName(name: String)

}