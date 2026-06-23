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
        // 1. Get the current user's ID
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
                        val requesterId = doc.getString("requesterId") ?: ""

                        // 2. THE FIX: If the request belongs to the logged-in user, ignore it!
                        if (requesterId == currentUserId) {
                            return@mapNotNull null
                        }

                        try {
                            BloodRequest(
                                requestId = doc.id,
                                requesterId = requesterId,
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
                                timeAgo = "Just now",
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
            val doc = firestore.collection("blood_requests").document(requestId).get().await()

            if (doc.exists()) {
                val request = BloodRequest(
                    requestId = doc.id,
                    requesterId = doc.getString("requesterId") ?: "",
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
    override suspend fun getUserById(userId: String): Result<User> {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            if (doc.exists()) {
                val user = com.example.bloodlink.domain.model.User(
                    id = doc.id,
                    fullName = doc.getString("fullName") ?: "Unknown",
                    //email = doc.getString("email") ?: "",
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

            // 1. Add the hero to the responses list
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

            // 2. Increment the response count on the main request
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
            .whereEqualTo("requesterId", currentUserId)
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
                                requesterId = doc.getString("requesterId") ?: "",
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
                                timeAgo = "Just now",
                                responsesCount = doc.getLong("responsesCount")?.toInt() ?: 0,
                                status = if (doc.getString("status") == "COMPLETED") RequestStatus.COMPLETED else RequestStatus.ACTIVE
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
            // 1. Get the list of hero IDs from the responses sub-collection
            val responsesSnapshot = firestore.collection("blood_requests")
                .document(requestId)
                .collection("responses")
                .get()
                .await()

            val donorIds = responsesSnapshot.documents.map { it.id }

            // 2. Fetch the actual User profiles for those IDs
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
}