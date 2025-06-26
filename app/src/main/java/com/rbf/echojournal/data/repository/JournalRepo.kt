package com.rbf.echojournal.data.repository

import com.rbf.echojournal.data.remote.model.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalRepo {
    /** Liefert alle Einträge des angegebenen Nutzers (Live-Updates). */
    fun observeEntries(userId: String): Flow<List<JournalEntry>>

    /** Legt einen neuen Eintrag für den Nutzer an. */
    suspend fun createEntry(userId: String, entry: JournalEntry)

    /** Aktualisiert einen bestehenden Eintrag des Nutzers. */
    suspend fun updateEntry(userId: String, entry: JournalEntry)

    /** Löscht den Eintrag mit entryId für den Nutzer. */
    suspend fun deleteEntry(userId: String, entryId: String)
}