package com.example.echojournal.data.repository

interface TranslationApiRepo {
    suspend fun translate(text: String, from: String, to: String): String
}