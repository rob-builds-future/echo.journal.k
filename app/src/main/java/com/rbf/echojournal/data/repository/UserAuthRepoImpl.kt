package com.rbf.echojournal.data.repository

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.rbf.echojournal.R
import com.rbf.echojournal.data.remote.model.JournalEntry
import com.rbf.echojournal.data.remote.model.User
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.Date
import java.util.UUID

class UserAuthRepoImpl(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserAuthRepo {

    override suspend fun signUp(
        email: String,
        password: String,
        username: String,
        preferredLanguage: String
    ): Result<User> = runCatching {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user!!.uid

        val now = Date()
        // store the raw string
        val userMap = mapOf(
            "email"             to email,
            "username"          to username,
            "preferredLanguage" to preferredLanguage,
            "createdAt"         to now
        )
        db.collection("users").document(uid).set(userMap).await()

        // return it unchanged
        User(
            id                = uid,
            email             = email,
            username          = username,
            preferredLanguage = preferredLanguage,
            createdAt         = now
        )
    }


    override suspend fun signIn(email: String, password: String): Result<User> =
        runCatching {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user!!.uid

            // Profil aus Firestore laden
            val doc = db.collection("users").document(uid).get().await()
            val emailStored = doc.getString("email")!!
            val username = doc.getString("username")!!
            val langCode = doc.getString("preferredLanguage")!!
            val ts = doc.getTimestamp("createdAt")!!.toDate()

            User(
                id = uid,
                email = emailStored,
                username = username,
                preferredLanguage = langCode,
                createdAt = ts
            )
        }

    override suspend fun updatePreferredLanguage(userId: String, newLang: String) {
        db.collection("users")
            .document(userId)
            .update("preferredLanguage", newLang)
            .await()
    }

    override suspend fun updateUsername(userId: String, newUsername: String) {
        db.collection("users")
            .document(userId)
            .update("username", newUsername)
            .await()
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        // Prüfe, ob Firebase einen User hat
        val firebaseUser = auth.currentUser ?: return null
        // Profil aus Firestore laden
        val uid = firebaseUser.uid
        val doc = db.collection("users").document(uid).get().await()
        val emailStored = doc.getString("email") ?: firebaseUser.email.orEmpty()
        val username = doc.getString("username") ?: ""
        val langCode = doc.getString("preferredLanguage") ?: "en"
        val ts = doc.getTimestamp("createdAt")?.toDate() ?: Date()

        return User(
            id = uid,
            email = emailStored,
            username = username,
            preferredLanguage = langCode,
            createdAt = ts
        )
    }

    override suspend fun getJournalEntries(userId: String): List<JournalEntry> {
        val snapshot = db
            .collection("users")
            .document(userId)
            .collection("journalEntries")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        return snapshot.documents.map { doc ->
            doc.toObject(JournalEntry::class.java)!!.copy(id = doc.id)
        }
    }

    override suspend fun signInWithGoogleOneTap(activity: Activity): Result<User> = runCatching {
        // 1) One-Tap-Option bauen (Benötigt keine Activity-speziﬁschen Aufrufe, nur das String-Resource)
        val rawNonce = UUID.randomUUID().toString()
        val hashedNonce = MessageDigest
            .getInstance("SHA-256")
            .digest(rawNonce.toByteArray())
            .joinToString("") { "%02x".format(it) }

        val oneTapOption = GetSignInWithGoogleOption.Builder(
            // Zugriff auf String-Resource via activity.getString(…)
            activity.getString(R.string.default_web_client_id)
        )
            .setNonce(hashedNonce)
            .build()

        // 2) CredentialManager-Request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(oneTapOption)
            .build()

        // 3) Credentials abholen – hier **Activity** statt „Context”
        //    CredentialManager.create(...) braucht ebenfalls einen Context, aber für das One-Tap-UI
        //    reicht es, dieselbe Activity zu übergeben.
        val credentialManager = CredentialManager.create(activity)
        val response = credentialManager.getCredential(activity, request)

        // 4) ID-Token extrahieren
        val custom = (response.credential as? CustomCredential)
            ?: throw IllegalStateException("Unexpected credential type")
        val idToken = GoogleIdTokenCredential.createFrom(custom.data).idToken

        // 5) Mit Firebase anmelden
        val firebaseCred = GoogleAuthProvider.getCredential(idToken, null)
        val authResult   = auth.signInWithCredential(firebaseCred).await()
        val userFb       = authResult.user!!

        Log.d("AuthRepo", "FirebaseAuth: signInWithCredential erfolgreich, UID = ${userFb.uid}")

        // 6) Firestore-Profil laden/erstellen (wie gehabt)
        val userDoc  = db.collection("users").document(userFb.uid)
        val snapshot = userDoc.get().await()

        val user = if (snapshot.exists()) {
            User(
                id                = userFb.uid,
                email             = snapshot.getString("email")!!,
                username          = snapshot.getString("username")!!,
                preferredLanguage = snapshot.getString("preferredLanguage")!!,
                createdAt         = snapshot.getTimestamp("createdAt")!!.toDate()
            )
        } else {
            val now = Date()
            val info = mapOf(
                "email"             to (userFb.email ?: ""),
                "username"          to (userFb.displayName ?: ""),
                "preferredLanguage" to "en",
                "createdAt"         to now
            )
            userDoc.set(info).await()
            User(
                id                = userFb.uid,
                email             = info["email"] as String,
                username          = info["username"] as String,
                preferredLanguage = "en",
                createdAt         = now
            )
        }
        user
    }
}
