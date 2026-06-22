package com.example.bloodlink.domain.repository

interface AuthRepository {
    val isUserLoggedIn: Boolean
    val currentUserId: String?

    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        bloodGroup: String,
        city: String,
        phoneNumber: String
    ): Result<Unit>

    suspend fun logout()
}