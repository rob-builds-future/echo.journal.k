package com.rbf.echojournal.data.repository

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

    /** Gibt das Datum zurück, an dem zuletzt der Congrats-Dialog gezeigt wurde (Format: "yyyy-MM-dd"), oder null. */
    suspend fun getLastCongratsDate(): String?

    /** Setzt das Datum, an dem der Dialog zuletzt gezeigt wurde. */
    suspend fun setLastCongratsDate(date: String)

    // ─── Reminder‐Einstellungen ───────────


}
