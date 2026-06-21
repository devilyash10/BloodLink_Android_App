package com.example.bloodlink.domain.repository

import com.example.bloodlink.domain.model.BloodBank
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.User
import kotlinx.coroutines.flow.Flow

interface BloodRepository {
    suspend fun getCurrentUser(): User
    fun getNearbyDonors(bloodGroup: String, radiusKm: Float): Flow<List<User>>
    fun getNearbyBloodBanks(): Flow<List<BloodBank>>
    fun getMyRequests(): Flow<List<BloodRequest>>
}