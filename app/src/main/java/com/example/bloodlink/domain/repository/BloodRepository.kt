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
    suspend fun getUserById(userId: String): Result<User>

    suspend fun acceptBloodRequest(requestId: String): Result<Unit>

    fun getMyBloodRequests(): Flow<List<BloodRequest>>

    suspend fun getRespondingHeroes(requestId: String): Result<List<User>>
    suspend fun markRequestCompleted(requestId: String): Result<Unit>
    //suspend fun updateDonorAvailability(donorId: String, isAvailable: Boolean): Result<Unit>
    suspend fun updateDonorAvailability(isAvailable: Boolean): Result<Unit>

    suspend fun searchDonors(bloodGroup: String, city: String): Result<List<User>>
    suspend fun getBloodBanks(): Result<List<BloodBank>>
    suspend fun getCurrentHospitalProfile(): Result<BloodBank>
    suspend fun updateHospitalInventory(newInventory: Map<String, Int>): Result<Unit>
}