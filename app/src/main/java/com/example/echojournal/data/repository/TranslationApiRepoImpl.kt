package com.example.echojournal.data.repository

import com.example.echojournal.data.remote.LibreTranslateService
import com.example.echojournal.data.remote.TranslateRequest

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
}