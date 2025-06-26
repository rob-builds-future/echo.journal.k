package com.rbf.echojournal.data.repository

import com.rbf.echojournal.data.remote.model.util.LanguageDto

interface TranslationApiRepo {
    suspend fun translate(text: String, from: String, to: String): String

    suspend fun getSupportedLanguages(): List<LanguageDto>
}