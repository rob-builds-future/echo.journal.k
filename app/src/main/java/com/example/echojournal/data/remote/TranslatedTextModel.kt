package com.example.echojournal.data.remote

import com.squareup.moshi.Json

// Request-Body als JSON
data class TranslateRequest(
    val q: String,
    val source: String,
    val target: String,
    val format: String = "text"
)

// Response-Model, das genau dem JSON-Key "translatedText" entspricht
data class TranslateResponse(
    @Json(name = "translatedText")
    val translatedText: String
)