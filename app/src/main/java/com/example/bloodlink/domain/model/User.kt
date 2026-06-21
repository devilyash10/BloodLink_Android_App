package com.example.bloodlink.domain.model

data class User(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val bloodGroup: String,
    val city: String,
    val distanceKm: Double? = null,
    val isAvailableAsDonor: Boolean = true,
    val lastDonationDate: String? = null,
    val totalDonations: Int = 0,
    val age: Int = 0
)