package com.example.bloodlink.domain.model

data class BloodBank(
    val id: String,
    val name: String,
    val address: String,
    val distanceKm: Double,
    val phoneNumber: String,
    val isOpen: Boolean,
    val availableBloodGroups: List<String> // e.g., ["O+", "A+", "B-"]
)