package com.example.echojournal.data.repository

import kotlinx.coroutines.flow.Flow

interface PrefsRepo {
    // ─── Onboarding-Flag ─────────────────
    val onboarded: Flow<Boolean>
    suspend fun setOnboarded(value: Boolean)

    // ─── Theme ────────────────────────────
    val theme: Flow<String>
    suspend fun setTheme(value: String)

    // ─── Spracheinstellungen ──────────────
    val currentLanguageCode: Flow<String>
    suspend fun setLanguageCode(code: String)

    val sourceLanguageCode: Flow<String>
    suspend fun setSourceLanguageCode(value: String)

    // ─── Username ─────────────────────────
    val username: Flow<String>
    suspend fun setUsername(name: String)

    // ─── Vorlagen-Name (Template) ─────────
    val currentTemplateName: Flow<String>
    suspend fun setTemplateName(name: String)

    // ─── Reminder‐Einstellungen ───────────
    /**
     * Liefert eine Map mit Label → Pair(enabled:Boolean, time:String im Format "HH:mm").
     */
    fun getReminderSettings(): Flow<Map<String, Pair<Boolean, String>>>

    /**
     * Setzt nur das „enabled“-Feld für das gegebene Label (Zeit bleibt erhalten).
     */
    suspend fun updateReminderEnabled(label: String, enabled: Boolean)

    /**
     * Setzt nur das „time“-Feld (als String, z. B. "09:00") für das gegebene Label (enabled bleibt erhalten).
     */
    suspend fun updateReminderTime(label: String, time: String)
}