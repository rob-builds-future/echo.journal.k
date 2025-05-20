package com.example.echojournal.data.repository

interface PrefsRepo {
    suspend fun isOnboarded(): Boolean
    suspend fun setOnboarded(onboarded: Boolean)
}