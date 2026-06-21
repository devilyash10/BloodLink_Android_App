package com.example.bloodlink.domain.model

data class BloodRequest(
    val requestId: String,
    val hospitalName: String,
    val locationArea: String,
    val bloodGroup: String,
    val urgencyLevel: UrgencyLevel,
    val status: RequestStatus,
    val responsesCount: Int,
    val timeAgo: String
)