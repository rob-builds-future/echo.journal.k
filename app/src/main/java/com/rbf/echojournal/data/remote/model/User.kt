package com.rbf.echojournal.data.remote.model

import java.util.Date

data class User(
    val id: String,
    val email: String,
    val username: String,
    val preferredLanguage: String,
    val createdAt: Date
)