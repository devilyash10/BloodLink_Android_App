package com.example.bloodlink.domain.model

data class BloodRequest(
    val requestId: String,
    val patientName: String,     // NEW
    val bloodGroup: String,
    val hospitalName: String,
    val locationArea: String,
    val urgencyLevel: UrgencyLevel,
    val unitsRequired: Int,      // NEW
    val additionalNotes: String, // NEW
    val timeAgo: String,
    val responsesCount: Int,
    val status: RequestStatus
)
