package com.example.echojournal.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Vorläufige In-Memory-Implementierung (für Tests).
 */
class PrefsRepoImpl : PrefsRepo {
    private val mutex = Mutex()
    private var onboarded = false

    override suspend fun isOnboarded(): Boolean = mutex.withLock {
        onboarded
    }

    override suspend fun setOnboarded(onboarded: Boolean) = mutex.withLock {
        this.onboarded = onboarded
    }
}