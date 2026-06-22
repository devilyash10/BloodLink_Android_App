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
import kotlinx.coroutines.channels.awaitClose

class FirebaseBloodRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : BloodRepository {

    override suspend fun getCurrentUser(): User? {
        // Replace any hardcoded fallback strings with clean functional safe exits:
        val uid = auth.currentUser?.uid ?: return null // for suspend operations returning optional types


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
                    distanceKm = 2.5 // We will calculate real geospatial distance later
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override fun getNearbyDonors(bloodGroup: String, radiusKm: Float): Flow<List<User>> = callbackFlow {
        // Real-time listener for available donors matching the blood group!
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
                        distanceKm = (1..10).random().toDouble() // Random dummy distance
                    )
                } ?: emptyList()

                trySend(donors)
            }

        awaitClose { subscription.remove() }
    }

    override fun getNearbyBloodBanks(): Flow<List<BloodBank>> = callbackFlow {
        // Listening to the institutions collection in real-time
        val subscription = firestore.collection("institutions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val banks = snapshot?.documents?.mapNotNull { doc ->
                    // Safely extract the live inventory map
                    val inventoryMap = doc.get("liveInventory") as? Map<String, Number> ?: emptyMap()

                    // Only show blood groups that have at least 1 unit in stock
                    val availableGroups = inventoryMap.filter { it.value.toLong() > 0 }.keys.toList()

                    BloodBank(
                        id = doc.id,
                        name = doc.getString("name") ?: "Unknown Blood Bank",
                        address = doc.getString("address") ?: "No address provided",
                        distanceKm = (1..15).random().toDouble(), // Random distance until Maps API is added
                        phoneNumber = doc.getString("contactPhone") ?: "",
                        isOpen = doc.getBoolean("isOpen24x7") ?: true,
                        availableBloodGroups = availableGroups
                    )
                } ?: emptyList()

                trySend(banks)
            }

        awaitClose { subscription.remove() }
    }

    // REMOVED 'suspend'
    override fun getMyRequests(): Flow<List<BloodRequest>> = callbackFlow {
        val uid = auth.currentUser?.uid ?: return@callbackFlow

        val subscription = firestore.collection("blood_requests")
            .whereEqualTo("requesterId", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val requests = snapshot?.documents?.mapNotNull { doc ->
                    BloodRequest(
                        requestId = doc.id,
                        patientName = doc.getString("patientName") ?: "Unknown", // Added
                        bloodGroup = doc.getString("bloodGroupNeeded") ?: "",
                        hospitalName = doc.getString("hospitalName") ?: "",
                        locationArea = doc.getString("hospitalName") ?: "",
                        urgencyLevel = UrgencyLevel.valueOf(doc.getString("urgencyLevel") ?: "HIGH"),
                        unitsRequired = doc.getLong("unitsRequired")?.toInt() ?: 1, // Added
                        additionalNotes = doc.getString("additionalNotes") ?: "", // Added
                        timeAgo = "Just now",
                        responsesCount = doc.getLong("responsesCount")?.toInt() ?: 0,
                        status = RequestStatus.valueOf(doc.getString("status") ?: "ACTIVE")
                    )
                } ?: emptyList()

                trySend(requests)
            }

        awaitClose { subscription.remove() }
    }
    override suspend fun getRequestById(requestId: String): BloodRequest? {
        return try {
            val doc = firestore.collection("blood_requests").document(requestId).get().await()
            if (doc.exists()) {
                BloodRequest(
                    requestId = doc.id,
                    patientName = doc.getString("patientName") ?: "Unknown Patient",
                    bloodGroup = doc.getString("bloodGroupNeeded") ?: "",
                    hospitalName = doc.getString("hospitalName") ?: "",
                    locationArea = doc.getString("hospitalName") ?: "",
                    urgencyLevel = UrgencyLevel.valueOf(doc.getString("urgencyLevel") ?: "HIGH"),
                    unitsRequired = doc.getLong("unitsRequired")?.toInt() ?: 1,
                    additionalNotes = doc.getString("additionalNotes") ?: "No additional notes provided.",
                    timeAgo = "Recently",
                    responsesCount = doc.getLong("responsesCount")?.toInt() ?: 0,
                    status = RequestStatus.valueOf(doc.getString("status") ?: "ACTIVE")
                )
            } else null
        } catch (e: Exception) {
            null
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
            // 1. Ensure the user is logged in
            val uid = auth.currentUser?.uid ?: throw Exception("User not authenticated.")

            // 2. Ask Firestore to generate a fresh, unique Document ID
            val newRequestRef = firestore.collection("blood_requests").document()

            // 3. Map the data exactly to your Firestore schema
            val requestData = hashMapOf(
                "patientName" to patientName,
                "bloodGroupNeeded" to bloodGroup,
                "hospitalName" to hospitalName,
                "hospitalId" to "pending_hospital_id", // Placeholder until you add real hospital picking
                "locationArea" to locationArea,
                "requesterId" to uid,
                "urgencyLevel" to urgencyLevel,
                "unitsRequired" to unitsRequired,
                "additionalNotes" to additionalNotes,
                "status" to "ACTIVE",
                "responsesCount" to 0,
                "createdAt" to com.google.firebase.Timestamp.now()
            )

            // 4. Save to database
            newRequestRef.set(requestData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // Add this function:
    override fun getActiveBloodRequests(): Flow<List<BloodRequest>> = callbackFlow {
        // We only want to see requests that are currently ACTIVE
        val subscription = firestore.collection("blood_requests")
            .whereEqualTo("status", "ACTIVE")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // If there's an issue, kill the stream safely
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val requests = snapshot.documents.mapNotNull { doc ->
                        try {
                            // Safely map the Firestore document back into our Kotlin object
                            BloodRequest(
                                requestId = doc.id,
                                patientName = doc.getString("patientName") ?: "Unknown",
                                bloodGroup = doc.getString("bloodGroupNeeded") ?: "--",
                                hospitalName = doc.getString("hospitalName") ?: "Unknown Hospital",
                                locationArea = doc.getString("locationArea") ?: "",
                                urgencyLevel = when (doc.getString("urgencyLevel")?.uppercase()) {
                                    "CRITICAL" -> UrgencyLevel.CRITICAL
                                    "HIGH" -> UrgencyLevel.HIGH
                                    else -> UrgencyLevel.NORMAL
                                },
                                unitsRequired = doc.getLong("unitsRequired")?.toInt() ?: 1,
                                additionalNotes = doc.getString("additionalNotes") ?: "",
                                // Simple time mock for now (can upgrade to real timestamp formatting later)
                                timeAgo = "Just now",
                                responsesCount = doc.getLong("responsesCount")?.toInt() ?: 0,
                                status = RequestStatus.ACTIVE
                            )
                        } catch (e: Exception) {
                            null // Skip corrupt documents safely
                        }
                    }
                    trySend(requests) // Send the list of requests to the UI
                }
            }

        // When the screen is destroyed, stop listening to save battery and money!
        awaitClose { subscription.remove() }
    }
    override suspend fun getBloodRequestById(requestId: String): Result<BloodRequest> {
        return try {
            val doc = firestore.collection("blood_requests").document(requestId).get().await()

            if (doc.exists()) {
                val request = BloodRequest(
                    requestId = doc.id,
                    patientName = doc.getString("patientName") ?: "Unknown",
                    bloodGroup = doc.getString("bloodGroupNeeded") ?: "--",
                    hospitalName = doc.getString("hospitalName") ?: "Unknown Hospital",
                    locationArea = doc.getString("locationArea") ?: "",
                    urgencyLevel = when (doc.getString("urgencyLevel")?.uppercase()) {
                        "CRITICAL" -> UrgencyLevel.CRITICAL
                        "HIGH" -> UrgencyLevel.HIGH
                        else -> UrgencyLevel.NORMAL
                    },
                    unitsRequired = doc.getLong("unitsRequired")?.toInt() ?: 1,
                    additionalNotes = doc.getString("additionalNotes") ?: "",
                    timeAgo = "Just now", // Placeholder
                    responsesCount = doc.getLong("responsesCount")?.toInt() ?: 0,
                    status = RequestStatus.ACTIVE
                )
                Result.success(request)
            } else {
                Result.failure(Exception("Request not found."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}