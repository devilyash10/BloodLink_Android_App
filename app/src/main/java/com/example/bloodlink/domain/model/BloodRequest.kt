package com.example.bloodlink.domain.model

data class BloodRequest(
    val requestId: String = "",
    val patientName: String = "",
    val bloodGroup: String = "",
    val hospitalName: String = "",
    val locationArea: String = "",
    val urgencyLevel: String = "HIGH",
    val unitsRequired: Int = 1,
    val additionalNotes: String = "",
    val responsesCount: Int = 0,
    // Ensure status uses your existing RequestStatus enum type
    val status: RequestStatus = RequestStatus.ACTIVE,
    // --- THE FIX: Add this line so the model can resolve the creator's ID ---
    val createdBy: String = ""
)