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
    }

    // 1. DataStore verwenden
    private val ds = context.dataStore

    // 2. Flow, das den letzten Wert aus dem Store liefert (default = false)
    override val onboarded: Flow<Boolean> = ds.data
        .map { it[Keys.ONBOARDED] ?: false }

    // 3. Setzen des Flags
    override suspend fun setOnboarded(value: Boolean) {
        ds.edit { it[Keys.ONBOARDED] = value }
    }

    override val theme: Flow<String> = ds.data
        .map { it[Keys.THEME] ?: "Wolkenlos" }  // Default-Theme

    override suspend fun setTheme(value: String) {
        ds.edit { it[Keys.THEME] = value }
    }
}