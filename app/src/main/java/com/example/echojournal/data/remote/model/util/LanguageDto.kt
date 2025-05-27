package com.example.echojournal.data.remote.model.util

import com.squareup.moshi.Json

data class LanguageDto(
    @Json(name="code") val code: String,
    @Json(name="name") val name: String
)