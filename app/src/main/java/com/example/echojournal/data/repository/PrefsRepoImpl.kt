package com.example.echojournal.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.echojournal.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PrefsRepoImpl(
    private val context: Context
) : PrefsRepo {

    private object Keys {
        val ONBOARDED = booleanPreferencesKey("onboarded")
        val THEME     = stringPreferencesKey("theme")
        val LANGUAGE  = stringPreferencesKey("language")
        val SOURCE_LANGUAGE = stringPreferencesKey("source_language")
    }

    // 1. DataStore verwenden
    private val ds = context.dataStore

    override val onboarded: Flow<Boolean> = ds.data
        .map { it[Keys.ONBOARDED] ?: false }
    override suspend fun setOnboarded(value: Boolean) {
        ds.edit { it[Keys.ONBOARDED] = value }
    }

    override val theme: Flow<String> = ds.data
        .map { it[Keys.THEME] ?: "Wolkenlos" }  // Default-Theme
    override suspend fun setTheme(value: String) {
        ds.edit { it[Keys.THEME] = value }
    }

    override val currentLanguageCode: Flow<String> = ds.data
        .map { it[Keys.LANGUAGE] ?: "en" }

    override suspend fun setLanguageCode(code: String) {
        ds.edit { it[Keys.LANGUAGE] = code }
    }

    override val sourceLanguageCode: Flow<String>
        get() = ds.data.map { it[Keys.SOURCE_LANGUAGE] ?: "de" }

    override suspend fun setSourceLanguageCode(value: String) {
        ds.edit { prefs -> prefs[Keys.SOURCE_LANGUAGE] = value }
    }
}