package com.example.bloodlink.data.repository

import com.example.bloodlink.domain.repository.AuthRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    override val currentUserId: String?
        get() = auth.currentUser?.uid

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        bloodGroup: String,
        city: String,
        phoneNumber: String
    ): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User instantiation failed.")

            // Build matching user profile document architecture
            val userData = mapOf(
                "fullName" to fullName,
                "bloodGroup" to bloodGroup,
                "city" to city,
                "phoneNumber" to phoneNumber,
                "email" to email,
                "isAvailableAsDonor" to false,
                "createdAt" to Timestamp.now()
            )

            firestore.collection("users").document(uid).set(userData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }
}