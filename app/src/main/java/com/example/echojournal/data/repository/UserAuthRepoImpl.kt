package com.example.echojournal.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.echojournal.R
import com.example.echojournal.data.remote.model.User
import com.example.echojournal.data.remote.model.util.ApiLanguage
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
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
        // 1) Auth mit E-Mail/Passwort
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user!!.uid

        // 2) Profil ergänzen im Firestore
        val now = Date()
        val userMap = mapOf(
            "email" to email,
            "username" to username,
            "preferredLanguage" to preferredLanguage,
            "createdAt" to now
        )
        db.collection("users")
            .document(uid)
            .set(userMap)
            .await()

        // 3) User-Objekt zurückgeben
        User(
            id = uid,
            email = email,
            username = username,
            preferredLanguage = ApiLanguage.valueOf(preferredLanguage.uppercase()),
            createdAt = now
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
                preferredLanguage = ApiLanguage.valueOf(langCode.uppercase()),
                createdAt = ts
            )
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
        val langCode = doc.getString("preferredLanguage") ?: ApiLanguage.EN.name
        val ts = doc.getTimestamp("createdAt")?.toDate() ?: Date()

        return User(
            id = uid,
            email = emailStored,
            username = username,
            preferredLanguage = ApiLanguage.valueOf(langCode.uppercase()),
            createdAt = ts
        )
    }

    override suspend fun signInWithGoogleOneTap(context: Context): Result<User> = runCatching {
        // 1) GetSignInWithGoogleOption bauen (One-Tap)
        val rawNonce = UUID.randomUUID().toString()
        val hashedNonce = MessageDigest
            .getInstance("SHA-256")
            .digest(rawNonce.toByteArray())
            .joinToString("") { "%02x".format(it) }

        val oneTapOption = GetSignInWithGoogleOption.Builder(
            context.getString(R.string.default_web_client_id)
        )
            .setNonce(hashedNonce)
            .build()

        // 2) CredentialManager-Request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(oneTapOption)
            .build()

        // 3) Credentials abholen (suspend)
        val credentialManager = CredentialManager.create(context)
        val response = credentialManager.getCredential(context, request)

        // 4) ID-Token extrahieren
        val custom = (response.credential as? CustomCredential)
            ?: throw IllegalStateException("Unexpected credential type")
        val idToken = GoogleIdTokenCredential.createFrom(custom.data).idToken

        // 5) Mit Firebase anmelden
        val firebaseCred = GoogleAuthProvider.getCredential(idToken, null)
        val authResult   = auth.signInWithCredential(firebaseCred).await()
        val userFb       = authResult.user!!

        // 6) Firestore-Profil laden/erstellen
        val userDoc  = db.collection("users").document(userFb.uid)
        val snapshot = userDoc.get().await()

        val user = if (snapshot.exists()) {
            // bereits da
            User(
                id                = userFb.uid,
                email             = snapshot.getString("email")!!,
                username          = snapshot.getString("username")!!,
                preferredLanguage = ApiLanguage.valueOf(
                    snapshot.getString("preferredLanguage")!!.uppercase()
                ),
                createdAt         = snapshot.getTimestamp("createdAt")!!.toDate()
            )
        } else {
            // neu anlegen
            val now = Date()
            val info = mapOf(
                "email"             to (userFb.email ?: ""),
                "username"          to (userFb.displayName ?: ""),
                "preferredLanguage" to "EN",
                "createdAt"         to now
            )
            userDoc.set(info).await()
            User(
                id                = userFb.uid,
                email             = info["email"] as String,
                username          = info["username"] as String,
                preferredLanguage = ApiLanguage.EN,
                createdAt         = now
            )
        }

        user
    }
}
