package com.example.bloodlink.domain.repository

import com.example.bloodlink.domain.model.BloodBank
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.User
import kotlinx.coroutines.flow.Flow


// Add this inside the interface:


interface BloodRepository {
    suspend fun getCurrentUser(): User?
    fun getNearbyDonors(bloodGroup: String, radiusKm: Float): Flow<List<User>>
    fun getNearbyBloodBanks(): Flow<List<BloodBank>>
    fun getMyRequests(): Flow<List<BloodRequest>>

    suspend fun getDonorById(donorId: String): User?
    suspend fun getRequestById(requestId: String): BloodRequest?
    // Add this to your interface
    suspend fun createBloodRequest(
        patientName: String,
        bloodGroup: String,
        hospitalName: String,
        locationArea: String,
        urgencyLevel: String,
        unitsRequired: Int,
        additionalNotes: String
    ): Result<Unit>

    fun getActiveBloodRequests(): Flow<List<BloodRequest>>
    // Fetch a single request by its ID
    suspend fun getBloodRequestById(requestId: String): Result<BloodRequest>
}