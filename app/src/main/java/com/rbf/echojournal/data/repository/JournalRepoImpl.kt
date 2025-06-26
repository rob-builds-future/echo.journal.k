package com.rbf.echojournal.data.repository

import com.rbf.echojournal.data.remote.model.JournalEntry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class JournalRepoImpl(
    private val db: FirebaseFirestore
) : JournalRepo {

    private fun entriesCollection(userId: String) =
        db.collection("users")
            .document(userId)
            .collection("journalEntries")

    override fun observeEntries(userId: String): Flow<List<JournalEntry>> = callbackFlow {
        val sub = entriesCollection(userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                val list = snap!!.documents.mapNotNull {
                    it.toObject(JournalEntry::class.java)
                }
                trySend(list)
            }
        awaitClose { sub.remove() }
    }

    override suspend fun createEntry(userId: String, entry: JournalEntry) {
        val docRef = entriesCollection(userId).document()
        docRef.set(entry.copy(id = docRef.id)).await()
    }

    override suspend fun updateEntry(userId: String, entry: JournalEntry) {
        entriesCollection(userId).document(entry.id).set(entry).await()
    }

    override suspend fun deleteEntry(userId: String, entryId: String) {
        entriesCollection(userId).document(entryId).delete().await()
    }
}