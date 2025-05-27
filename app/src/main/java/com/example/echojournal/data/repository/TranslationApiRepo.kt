package com.example.echojournal.data.repository

import com.example.echojournal.data.remote.model.util.LanguageDto

interface TranslationApiRepo {
    suspend fun translate(text: String, from: String, to: String): String

    suspend fun getSupportedLanguages(): List<LanguageDto>
}