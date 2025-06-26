package com.rbf.echojournal.data.remote.model

import com.squareup.moshi.Json

// Response-Model, das genau dem JSON-Key "translatedText" entspricht
data class TranslateResponse(
    @Json(name = "translatedText")
    val translatedText: String
)