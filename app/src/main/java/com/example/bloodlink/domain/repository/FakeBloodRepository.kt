package com.example.bloodlink.data.repository

import com.example.bloodlink.domain.model.BloodBank
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.RequestStatus
import com.example.bloodlink.domain.model.UrgencyLevel
import com.example.bloodlink.domain.model.User
import com.example.bloodlink.domain.repository.BloodRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBloodRepository : BloodRepository {

    // Mock Current Logged-in User
    private val currentUser = User(
        id = "u_123",
        fullName = "Rajesh Kumar",
        phoneNumber = "+91 9876543210",
        bloodGroup = "O+",
        city = "Bangalore",
        isAvailableAsDonor = true,
        lastDonationDate = "12 Jan 2024",
        totalDonations = 2,
        age = 28
    )

    // Mock Donors matching your "Donor List Screen" design
    private val dummyDonors = listOf(
        User("d_1", "Rahul Sharma", "+91 9000000001", "O+", "Bangalore", 2.4, true, "12 Jan 2024", 3, 28),
        User("d_2", "Ankita Patel", "+91 9000000002", "O+", "Bangalore", 3.1, true, "10 Sep 2023", 1, 25),
        User("d_3", "Amit Verma", "+91 9000000003", "O+", "Bangalore", 4.5, true, "15 May 2023", 5, 32),
        User("d_4", "Neha Singh", "+91 9000000004", "O+", "Bangalore", 5.2, false, null, 0, 24),
        User("d_5", "Vikram Reddy", "+91 9000000005", "O+", "Bangalore", 6.8, true, "10 Jan 2024", 4, 29)
    )

    // Mock Blood Banks matching your "Blood Banks Screen" design
    private val dummyBloodBanks = listOf(
        BloodBank("b_1", "Red Cross Blood Bank", "Koramangala", 1.2, true, "Closes 8 PM"),
        BloodBank("b_2", "Jeevan Blood Bank", "HSR Layout", 2.5, true, "Closes 7 PM"),
        BloodBank("b_3", "Narayana Blood Bank", "BTM Layout", 3.8, true, "Closes 8 PM")
    )

    // Mock Requests matching your "My Requests Screen" design
    private val dummyRequests = listOf(
        BloodRequest("REQ12345", "Apollo Hospital", "Bangalore", "O+", UrgencyLevel.HIGH, RequestStatus.ACTIVE, 2, "2 min ago"),
        BloodRequest("REQ12300", "Fortis Hospital", "Bangalore", "B+", UrgencyLevel.MEDIUM, RequestStatus.ACTIVE, 0, "1 day ago"),
        BloodRequest("REQ12200", "Manipal Hospital", "Bangalore", "A+", UrgencyLevel.LOW, RequestStatus.COMPLETED, 1, "2 days ago")
    )

    override suspend fun getCurrentUser(): User {
        delay(500) // Simulating network call
        return currentUser
    }

    override fun getNearbyDonors(bloodGroup: String, radiusKm: Float): Flow<List<User>> = flow {
        delay(800) // Simulating loading time for UI spinners
        // Simulating backend filtering logic
        val filtered = dummyDonors.filter {
            (bloodGroup == "All" || it.bloodGroup == bloodGroup) &&
                    (it.distanceKm ?: 0.0) <= radiusKm.toDouble()
        }
        emit(filtered)
    }

    override fun getNearbyBloodBanks(): Flow<List<BloodBank>> = flow {
        delay(600)
        emit(dummyBloodBanks)
    }

    override fun getMyRequests(): Flow<List<BloodRequest>> = flow {
        delay(600)
        emit(dummyRequests)
    }

    override suspend fun getDonorById(donorId: String): User? {
        // Find the user in your dummy list whose ID matches the clicked ID
        return dummyDonors.find { it.id == donorId }
    }
}