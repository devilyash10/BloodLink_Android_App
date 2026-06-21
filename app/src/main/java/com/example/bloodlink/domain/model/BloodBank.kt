package com.example.bloodlink.domain.model

data class BloodBank(
    val id: String,
    val name: String,
    val area: String,
    val distanceKm: Double,
    val isOpen: Boolean,
    val closesAt: String
)