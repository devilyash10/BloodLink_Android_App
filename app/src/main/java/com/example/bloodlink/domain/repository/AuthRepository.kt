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
        phoneNumber: String,
        userType: String // <-- ADD THIS
    ): Result<Unit>

//    suspend fun register(
//        email: String, password: String, fullName: String,
//        phoneNumber: String, bloodGroup: String, city: String,
//        userType: String // <-- ADD THIS
//    ): Result<Unit>

    suspend fun logout()
}