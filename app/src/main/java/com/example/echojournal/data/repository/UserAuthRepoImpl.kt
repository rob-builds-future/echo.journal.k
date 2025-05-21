package com.example.echojournal.data.repository

import com.example.echojournal.data.remote.model.User
import com.example.echojournal.data.remote.model.util.ApiLanguage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class FirebaseUserAuthRepo : UserAuthRepo {

    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

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

    override suspend fun signIn(email: String, password: String): Result<User> = runCatching {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val uid = authResult.user!!.uid

        // Profil aus Firestore laden
        val doc = db.collection("users").document(uid).get().await()
        val emailStored = doc.getString("email")!!
        val username     = doc.getString("username")!!
        val langCode     = doc.getString("preferredLanguage")!!
        val ts           = doc.getTimestamp("createdAt")!!.toDate()

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
        val username     = doc.getString("username") ?: ""
        val langCode     = doc.getString("preferredLanguage") ?: ApiLanguage.EN.name
        val ts           = doc.getTimestamp("createdAt")?.toDate() ?: Date()

        return User(
            id = uid,
            email = emailStored,
            username = username,
            preferredLanguage = ApiLanguage.valueOf(langCode.uppercase()),
            createdAt = ts
        )
    }
}
