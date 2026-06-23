package com.example.bloodlink.domain.model

data class BloodBank(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val contactPhone: String = "", // Crucial for the Call button
    val isOpen: Boolean = true,
    val distanceKm: Double = 0.0,
    val availableBloodGroups: List<String> = emptyList() // We will extract this from liveInventory!
)