package com.example.bloodlink.data.repository

import com.example.bloodlink.domain.model.BloodBank
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.RequestStatus
import com.example.bloodlink.domain.model.UrgencyLevel
import com.example.bloodlink.domain.model.User
import com.example.bloodlink.domain.repository.BloodRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseBloodRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : BloodRepository {

    override suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            if (doc.exists()) {
                User(
                    id = doc.id,
                    fullName = doc.getString("fullName") ?: "Unknown User",
                    phoneNumber = doc.getString("phoneNumber") ?: "",
                    bloodGroup = doc.getString("bloodGroup") ?: "--",
                    city = doc.getString("city") ?: "",
                    isAvailableAsDonor = doc.getBoolean("isAvailableAsDonor") ?: false,
                    distanceKm = 0.0
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getDonorById(donorId: String): User? {
        return try {
            val doc = firestore.collection("users").document(donorId).get().await()
            if (doc.exists()) {
                User(
                    id = doc.id,
                    fullName = doc.getString("fullName") ?: "Unknown Donor",
                    phoneNumber = doc.getString("phoneNumber") ?: "",
                    bloodGroup = doc.getString("bloodGroup") ?: "",
                    city = doc.getString("city") ?: "",
                    isAvailableAsDonor = doc.getBoolean("isAvailableAsDonor") ?: false,
                    distanceKm = 2.5
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override fun getNearbyDonors(bloodGroup: String, radiusKm: Float): Flow<List<User>> = callbackFlow {
        val subscription = firestore.collection("users")
            .whereEqualTo("bloodGroup", bloodGroup)
            .whereEqualTo("isAvailableAsDonor", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val donors = snapshot?.documents?.mapNotNull { doc ->
                    User(
                        id = doc.id,
                        fullName = doc.getString("fullName") ?: "Unknown",
                        phoneNumber = doc.getString("phoneNumber") ?: "",
                        bloodGroup = doc.getString("bloodGroup") ?: bloodGroup,
                        city = doc.getString("city") ?: "",
                        isAvailableAsDonor = true,
                        distanceKm = (1..10).random().toDouble()
                    )
                } ?: emptyList()

                trySend(donors)
            }
        awaitClose { subscription.remove() }
    }

    override fun getNearbyBloodBanks(): Flow<List<BloodBank>> = callbackFlow {
        val subscription = firestore.collection("institutions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val banks = snapshot?.documents?.mapNotNull { doc ->
                    val inventoryMap = doc.get("liveInventory") as? Map<String, Number> ?: emptyMap()
                    val availableGroups = inventoryMap.filter { it.value.toLong() > 0 }.keys.toList()

                    BloodBank(
                        id = doc.id,
                        name = doc.getString("name") ?: "Unknown Blood Bank",
                        address = doc.getString("address") ?: "No address provided",
                        distanceKm = (1..15).random().toDouble(),
                        contactPhone = doc.getString("contactPhone") ?: "",
                        isOpen = doc.getBoolean("isOpen24x7") ?: true,
                        availableBloodGroups = availableGroups
                    )
                } ?: emptyList()
                trySend(banks)
            }
        awaitClose { subscription.remove() }
    }

    // LEGACY FUNCTION CLEANED
    override fun getMyRequests(): Flow<List<BloodRequest>> = callbackFlow {
        val uid = auth.currentUser?.uid ?: return@callbackFlow

        val subscription = firestore.collection("blood_requests")
            .whereEqualTo("createdBy", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val requests = snapshot?.documents?.mapNotNull { doc ->
                    BloodRequest(
                        requestId = doc.id,
                        createdBy = doc.getString("createdBy") ?: uid,
                        patientName = doc.getString("patientName") ?: "Unknown",
                        bloodGroup = doc.getString("bloodGroup") ?: doc.getString("bloodGroupNeeded") ?: "",
                        hospitalName = doc.getString("hospitalName") ?: "",
                        locationArea = doc.getString("locationArea") ?: "",
                        urgencyLevel = doc.getString("urgencyLevel") ?: "HIGH",
                        unitsRequired = doc.getLong("unitsRequired")?.toInt() ?: 1,
                        additionalNotes = doc.getString("additionalNotes") ?: "",
                        responsesCount = doc.getLong("responsesCount")?.toInt() ?: 0,
                        status = if (doc.getString("status") == "COMPLETED" || doc.getString("status") == "FULFILLED") RequestStatus.COMPLETED else RequestStatus.ACTIVE
                    )
                } ?: emptyList()
                trySend(requests)
            }
        awaitClose { subscription.remove() }
    }

    // LEGACY FUNCTION CLEANED
    override suspend fun getRequestById(requestId: String): BloodRequest? {
        return try {
            val doc = firestore.collection("blood_requests").document(requestId).get().await()
            if (doc.exists()) {
                BloodRequest(
                    requestId = doc.id,
                    createdBy = doc.getString("createdBy") ?: doc.getString("requesterId") ?: "",
                    patientName = doc.getString("patientName") ?: "Unknown Patient",
                    bloodGroup = doc.getString("bloodGroup") ?: doc.getString("bloodGroupNeeded") ?: "",
                    hospitalName = doc.getString("hospitalName") ?: "",
                    locationArea = doc.getString("locationArea") ?: "",
                    urgencyLevel = doc.getString("urgencyLevel") ?: "HIGH",
                    unitsRequired = doc.getLong("unitsRequired")?.toInt() ?: 1,
                    additionalNotes = doc.getString("additionalNotes") ?: "No additional notes provided.",
                    responsesCount = doc.getLong("responsesCount")?.toInt() ?: 0,
                    status = if (doc.getString("status") == "COMPLETED" || doc.getString("status") == "FULFILLED") RequestStatus.COMPLETED else RequestStatus.ACTIVE
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override fun getActiveBloodRequests(): Flow<List<BloodRequest>> = callbackFlow {
        val currentUserId = auth.currentUser?.uid

        val subscription = firestore.collection("blood_requests")
            .whereEqualTo("status", "ACTIVE")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val requests = snapshot.documents.mapNotNull { doc ->
                        val createdBy = doc.getString("createdBy") ?: doc.getString("requesterId") ?: ""

                        if (createdBy == currentUserId) {
                            return@mapNotNull null
                        }

                        try {
                            BloodRequest(
                                requestId = doc.id,
                                createdBy = createdBy,
                                patientName = doc.getString("patientName") ?: "Unknown",
                                bloodGroup = doc.getString("bloodGroup") ?: doc.getString("bloodGroupNeeded") ?: "--",
                                hospitalName = doc.getString("hospitalName") ?: "Unknown Hospital",
                                locationArea = doc.getString("locationArea") ?: "",
                                urgencyLevel = doc.getString("urgencyLevel") ?: "HIGH",
                                unitsRequired = doc.getLong("unitsRequired")?.toInt() ?: 1,
                                additionalNotes = doc.getString("additionalNotes") ?: "",
                                responsesCount = doc.getLong("responsesCount")?.toInt() ?: 0,
                                status = RequestStatus.ACTIVE
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(requests)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getBloodRequestById(requestId: String): Result<BloodRequest> {
        return try {
            val document = firestore.collection("blood_requests").document(requestId).get().await()
            val request = document.toObject(BloodRequest::class.java)
            if (request != null) {
                Result.success(request.copy(requestId = document.id))
            } else {
                Result.failure(Exception("Request not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(userId: String): Result<User> {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            if (doc.exists()) {
                val user = com.example.bloodlink.domain.model.User(
                    id = doc.id,
                    fullName = doc.getString("fullName") ?: "Unknown",
                    phoneNumber = doc.getString("phoneNumber") ?: "No phone provided",
                    bloodGroup = doc.getString("bloodGroup") ?: "",
                    city = doc.getString("city") ?: "",
                    isAvailableAsDonor = doc.getBoolean("isAvailableAsDonor") ?: true,
                    totalDonations = doc.getLong("totalDonations")?.toInt() ?: 0
                )
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acceptBloodRequest(requestId: String): Result<Unit> {
        return try {
            val heroId = auth.currentUser?.uid ?: throw Exception("Not logged in")

            val responseData = hashMapOf(
                "donorId" to heroId,
                "respondedAt" to com.google.firebase.Timestamp.now(),
                "status" to "ACCEPTED"
            )

            firestore.collection("blood_requests")
                .document(requestId)
                .collection("responses")
                .document(heroId)
                .set(responseData)
                .await()

            firestore.collection("blood_requests").document(requestId)
                .update("responsesCount", com.google.firebase.firestore.FieldValue.increment(1))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMyBloodRequests(): Flow<List<BloodRequest>> = callbackFlow {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId == null) {
            close(Exception("User not logged in"))
            return@callbackFlow
        }

        val subscription = firestore.collection("blood_requests")
            .whereEqualTo("createdBy", currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val requests = snapshot.documents.mapNotNull { doc ->
                        try {
                            BloodRequest(
                                requestId = doc.id,
                                createdBy = doc.getString("createdBy") ?: "",
                                patientName = doc.getString("patientName") ?: "Unknown",
                                bloodGroup = doc.getString("bloodGroup") ?: "--",
                                hospitalName = doc.getString("hospitalName") ?: "Unknown Hospital",
                                locationArea = doc.getString("locationArea") ?: "",
                                urgencyLevel = doc.getString("urgencyLevel") ?: "HIGH",
                                unitsRequired = doc.getLong("unitsRequired")?.toInt() ?: 1,
                                additionalNotes = doc.getString("additionalNotes") ?: "",
                                responsesCount = doc.getLong("responsesCount")?.toInt() ?: 0,
                                status = if (doc.getString("status") == "COMPLETED" || doc.getString("status") == "FULFILLED")
                                    RequestStatus.COMPLETED
                                else
                                    RequestStatus.ACTIVE
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(requests)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getRespondingHeroes(requestId: String): Result<List<User>> {
        return try {
            val responsesSnapshot = firestore.collection("blood_requests")
                .document(requestId)
                .collection("responses")
                .get()
                .await()

            val donorIds = responsesSnapshot.documents.map { it.id }

            val heroes = mutableListOf<User>()
            for (id in donorIds) {
                getUserById(id).onSuccess { user -> heroes.add(user) }
            }

            Result.success(heroes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markRequestCompleted(requestId: String): Result<Unit> {
        return try {
            firestore.collection("blood_requests")
                .document(requestId)
                .update("status", "COMPLETED")
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDonorAvailability(isAvailable: Boolean): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            firestore.collection("users")
                .document(uid)
                .update("isAvailableAsDonor", isAvailable)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchDonors(bloodGroup: String, city: String): Result<List<User>> {
        return try {
            val snapshot = firestore.collection("users").get().await()
            val currentUserId = auth.currentUser?.uid

            val allDonors = snapshot.documents.mapNotNull { doc ->
                try {
                    User(
                        id = doc.id,
                        fullName = doc.getString("fullName") ?: "Unknown",
                        phoneNumber = doc.getString("phoneNumber") ?: "",
                        bloodGroup = doc.getString("bloodGroup") ?: "",
                        city = doc.getString("city") ?: "",
                        isAvailableAsDonor = doc.getBoolean("isAvailableAsDonor") ?: true,
                        totalDonations = doc.getLong("totalDonations")?.toInt() ?: 0,
                        userType = doc.getString("userType") ?: "INDIVIDUAL"
                    )
                } catch (e: Exception) {
                    null
                }
            }

            val filtered = allDonors.filter { donor ->
                val isAvailable = donor.isAvailableAsDonor
                val isIndividual = donor.userType == "INDIVIDUAL"
                val isNotMe = donor.id != currentUserId
                val matchBlood = if (bloodGroup == "All" || bloodGroup.isBlank()) true else donor.bloodGroup.trim() == bloodGroup.trim()
                val matchCity = if (city.isBlank()) true else donor.city.contains(city.trim(), ignoreCase = true)

                isAvailable && isIndividual && isNotMe && matchBlood && matchCity
            }

            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBloodBanks(): Result<List<BloodBank>> {
        return try {
            val snapshot = firestore.collection("institutions").get().await()
            val currentUserId = auth.currentUser?.uid

            val banks = snapshot.documents.mapNotNull { doc ->
                if (doc.id == currentUserId) return@mapNotNull null

                try {
                    val liveInventory = doc.get("liveInventory") as? Map<String, Long>
                    val inStockGroups = liveInventory?.filter { (it.value ?: 0) > 0 }?.keys?.toList() ?: emptyList()

                    BloodBank(
                        id = doc.id,
                        name = doc.getString("name") ?: "Unknown Bank",
                        address = doc.getString("address") ?: "",
                        contactPhone = doc.getString("contactPhone") ?: "",
                        isOpen = doc.getBoolean("isOpen24x7") ?: true,
                        distanceKm = 5.0,
                        availableBloodGroups = inStockGroups
                    )
                } catch (e: Exception) {
                    null
                }
            }
            Result.success(banks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentHospitalProfile(): Result<BloodBank> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Hospital not logged in")
            val doc = firestore.collection("institutions").document(uid).get().await()

            if (!doc.exists()) throw Exception("Institution profile not found")

            val liveInventoryRaw = doc.get("liveInventory") as? Map<String, Any> ?: emptyMap()
            val liveInventoryInt = liveInventoryRaw.mapValues { (it.value as? Number)?.toInt() ?: 0 }
            val inStockGroups = liveInventoryInt.filter { it.value > 0 }.keys.toList()

            val hospital = BloodBank(
                id = doc.id,
                name = doc.getString("name") ?: "Unknown Hospital",
                address = doc.getString("address") ?: "",
                contactPhone = doc.getString("contactPhone") ?: "",
                isOpen = doc.getBoolean("isOpen24x7") ?: true,
                distanceKm = 0.0,
                availableBloodGroups = inStockGroups,
                liveInventory = liveInventoryInt
            )
            Result.success(hospital)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateHospitalInventory(newInventory: Map<String, Int>): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Hospital not logged in")
            val updates = mapOf(
                "liveInventory" to newInventory,
                "lastInventoryUpdate" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            firestore.collection("institutions").document(uid).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createBloodRequest(
        patientName: String,
        bloodGroup: String,
        hospitalName: String,
        locationArea: String,
        urgencyLevel: String,
        unitsRequired: Int,
        additionalNotes: String
    ): Result<Unit> {
        return try {
            val requestId = java.util.UUID.randomUUID().toString().take(8).uppercase()
            val userId = auth.currentUser?.uid ?: "UNKNOWN"

            val requestMap = hashMapOf(
                "requestId" to requestId,
                "patientName" to patientName,
                "bloodGroup" to bloodGroup,
                "hospitalName" to hospitalName,
                "locationArea" to locationArea,
                "urgencyLevel" to urgencyLevel,
                "unitsRequired" to unitsRequired,
                "additionalNotes" to additionalNotes,
                "createdBy" to userId,
                "status" to "ACTIVE",
                "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            firestore.collection("blood_requests")
                .document(requestId)
                .set(requestMap)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserBloodRequests(): Flow<List<BloodRequest>> = callbackFlow {
        val userId = auth.currentUser?.uid ?: return@callbackFlow

        val listener = firestore.collection("blood_requests")
            .whereEqualTo("createdBy", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val requests = snapshot.documents.mapNotNull { it.toObject(BloodRequest::class.java)?.copy(requestId = it.id) }
                    trySend(requests).isSuccess
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun markRequestFulfilled(requestId: String): Result<Unit> {
        return try {
            firestore.collection("blood_requests")
                .document(requestId)
                .update("status", "COMPLETED") // Aligning to COMPLETED to match your Domain Model
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}