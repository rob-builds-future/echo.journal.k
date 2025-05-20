package com.example.echojournal.data.remote.model

import com.example.echojournal.data.remote.model.util.ApiLanguage
import java.util.Date

data class User(
    val id: String,
    val email: String,
    val username: String,
    val preferredLanguage: ApiLanguage,
    val createdAt: Date
)