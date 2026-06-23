package com.example.bloodlink.domain.util

object BloodCompatibility {
    // Key: Donor's Blood Group
    // Value: List of Blood Groups they are medically allowed to donate to
    private val compatibilityMap = mapOf(
        "O-"  to listOf("O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"),
        "O+"  to listOf("O+", "A+", "B+", "AB+"),
        "A-"  to listOf("A-", "A+", "AB-", "AB+"),
        "A+"  to listOf("A+", "AB+"),
        "B-"  to listOf("B-", "B+", "AB-", "AB+"),
        "B+"  to listOf("B+", "AB+"),
        "AB-" to listOf("AB-", "AB+"),
        "AB+" to listOf("AB+")
    )

    fun canDonate(donorGroup: String, receiverGroup: String): Boolean {
        // Strip out any weird spaces just in case
        val cleanDonor = donorGroup.trim().uppercase()
        val cleanReceiver = receiverGroup.trim().uppercase()

        return compatibilityMap[cleanDonor]?.contains(cleanReceiver) == true
    }
}