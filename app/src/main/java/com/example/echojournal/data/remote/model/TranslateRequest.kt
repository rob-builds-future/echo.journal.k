package com.example.echojournal.data.remote.model

// Request-Body als JSON
data class TranslateRequest(
    val q: String,
    val source: String,
    val target: String,
    val format: String = "text"
)