package com.example.echojournal.data.local

import java.time.LocalDateTime

data class JournalEntry(
    val id: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isFavorite: Boolean,
    val duration: Int
)