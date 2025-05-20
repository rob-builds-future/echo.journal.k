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

    override fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        // Nutzer-Profile nur aus Firestore laden, wenn nötig
        // Hier stub: wir geben nur ID & Email zurück
        return User(
            id = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            username = "",
            preferredLanguage = ApiLanguage.EN,
            createdAt = Date(0)
        )
    }
}
