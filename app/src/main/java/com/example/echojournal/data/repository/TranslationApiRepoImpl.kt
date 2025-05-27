package com.example.echojournal.data.repository

import com.example.echojournal.data.remote.LibreTranslateService
import com.example.echojournal.data.remote.model.TranslateRequest
import com.example.echojournal.data.remote.model.util.LanguageDto

class TranslationApiRepoImpl(
    private val api: LibreTranslateService
): TranslationApiRepo {
    override suspend fun translate(text: String, from: String, to: String): String {
        val request = TranslateRequest(
            q      = text,
            source = from,
            target = to
        )
        val response = api.translate(request)
        return response.translatedText
    }

    override suspend fun getSupportedLanguages(): List<LanguageDto> {
        return api.getLanguages()
    }
}