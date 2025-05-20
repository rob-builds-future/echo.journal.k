package com.example.echojournal.data.remote.model

import java.io.Serializable
import java.time.LocalDateTime

data class JournalEntry(
    val id: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isFavorite: Boolean,
    val duration: Int
) : Serializable