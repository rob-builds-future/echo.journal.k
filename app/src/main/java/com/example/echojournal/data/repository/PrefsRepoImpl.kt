package com.example.echojournal.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.echojournal.R
import com.example.echojournal.dataStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


// Datenklasse für einen einzelnen Reminder‐Eintrag (serialisierbar via Moshi).

data class ReminderConfig(
    val enabled: Boolean,
    val time: String
)

class PrefsRepoImpl(
    private val context: Context,
    private val moshi: Moshi
) : PrefsRepo {

    private object Keys {
        val ONBOARDED = booleanPreferencesKey("onboarded")
        val THEME     = stringPreferencesKey("theme")
        val LANGUAGE  = stringPreferencesKey("language")
        val SOURCE_LANGUAGE = stringPreferencesKey("source_language")
        val USERNAME  = stringPreferencesKey("username")
        val TEMPLATE_NAME = stringPreferencesKey("template_name")
        val REMINDERS_JSON = stringPreferencesKey("key_reminders_json")
        val KEY_LAST_CONGRATS_DATE = stringPreferencesKey("last_congrats_date")
    }

    // 1. DataStore verwenden
    private val ds = context.dataStore

    // 2) Moshi-Adapter für Map<String, ReminderConfig>
    //    Typ: Map<String, ReminderConfig>
    private val mapType = Types.newParameterizedType(
        Map::class.java,
        String::class.java,
        ReminderConfig::class.java
    )
    private val mapAdapter = moshi.adapter<Map<String, ReminderConfig>>(mapType)

    // Helper: parse JSON (String) → Map<String, ReminderConfig>
    private fun parseJsonToMap(json: String): Map<String, ReminderConfig> {
        return try {
            mapAdapter.fromJson(json) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    // Helper: Map<String, ReminderConfig> → JSON-String
    private fun toJsonString(map: Map<String, ReminderConfig>): String {
        return try {
            mapAdapter.toJson(map)
        } catch (e: Exception) {
            "{}"
        }
    }

    // ─── Onboarding ─────────────────────────────────────────────────────────────
    override val onboarded: Flow<Boolean> = ds.data
        .map { it[Keys.ONBOARDED] ?: false }

    override suspend fun setOnboarded(value: Boolean) {
        ds.edit { it[Keys.ONBOARDED] = value }
    }

    // ─── Theme ─────────────────────────────────────────────────────────────────
    override val theme: Flow<String> = ds.data
        .map { it[Keys.THEME] ?: context.getString(R.string.default_theme) }

    override suspend fun setTheme(value: String) {
        ds.edit { it[Keys.THEME] = value }
    }

    // ─── Sprache (Zielsprache) ──────────────────────────────────────────────────
    override val currentLanguageCode: Flow<String> = ds.data
        .map { it[Keys.LANGUAGE] ?: "en" }

    override suspend fun setLanguageCode(code: String) {
        ds.edit { it[Keys.LANGUAGE] = code }
    }

    override val sourceLanguageCode: Flow<String> = ds.data
        .map { it[Keys.SOURCE_LANGUAGE] ?: "auto" }

    override suspend fun setSourceLanguageCode(value: String) {
        ds.edit { it[Keys.SOURCE_LANGUAGE] = value }
    }

    // ─── Username ────────────────────────────────────────────────────────────────
    override val username: Flow<String> = ds.data
        .map { it[Keys.USERNAME] ?: "" }

    override suspend fun setUsername(name: String) {
        ds.edit { it[Keys.USERNAME] = name }
    }

    // ─── Template-Name ───────────────────────────────────────────────────────────
    override val currentTemplateName: Flow<String> = ds.data
        .map { it[Keys.TEMPLATE_NAME] ?: context.getString(R.string.template_none) }

    override suspend fun setTemplateName(name: String) {
        ds.edit { it[Keys.TEMPLATE_NAME] = name }
    }

    // ─── Reminder‐Einstellungen ──────────────────────────────────────────────────
    override fun getReminderSettings(): Flow<Map<String, Pair<Boolean, String>>> =
        ds.data
            .catch { emit(emptyPreferences()) }
            .map { prefs ->
                val json = prefs[Keys.REMINDERS_JSON].orEmpty()
                val parsed: Map<String, ReminderConfig> = parseJsonToMap(json)
                // Konvertiere ReminderConfig → Pair(enabled, time)
                parsed.mapValues { (_, cfg) -> cfg.enabled to cfg.time }
            }

    override suspend fun updateReminderEnabled(label: String, enabled: Boolean) {
        ds.edit { prefs ->
            val currentJson = prefs[Keys.REMINDERS_JSON].orEmpty()
            val currentMap: MutableMap<String, ReminderConfig> =
                parseJsonToMap(currentJson).toMutableMap()

            val oldTime = currentMap[label]?.time ?: "09:00"
            currentMap[label] = ReminderConfig(enabled = enabled, time = oldTime)
            prefs[Keys.REMINDERS_JSON] = toJsonString(currentMap)
        }
    }

    override suspend fun updateReminderTime(label: String, time: String) {
        ds.edit { prefs ->
            val currentJson = prefs[Keys.REMINDERS_JSON].orEmpty()
            val currentMap: MutableMap<String, ReminderConfig> =
                parseJsonToMap(currentJson).toMutableMap()

            val oldEnabled = currentMap[label]?.enabled ?: false
            currentMap[label] = ReminderConfig(enabled = oldEnabled, time = time)
            prefs[Keys.REMINDERS_JSON] = toJsonString(currentMap)
        }
    }

    override suspend fun getLastCongratsDate(): String? {
        val prefs = ds.data.first()
        return prefs[Keys.KEY_LAST_CONGRATS_DATE]
    }

    override suspend fun setLastCongratsDate(date: String) {
        ds.edit { it[Keys.KEY_LAST_CONGRATS_DATE] = date }
    }
}