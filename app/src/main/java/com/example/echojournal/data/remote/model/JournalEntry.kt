package com.example.echojournal.data.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable

data class JournalEntry(
    val id: String = "",
    val userId: String = "",
    val content: String = "",
    val translatedContent: String = "",
    val sourceLang: String = "auto",
    val targetLang: String = "en",
    val duration: Int = 0,
    val isFavorite: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) : Serializable