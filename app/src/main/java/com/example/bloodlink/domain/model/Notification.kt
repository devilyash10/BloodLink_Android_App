package com.example.bloodlink.domain.model

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean,
    val type: NotificationType
)

enum class NotificationType {
    URGENT_REQUEST, // Red alert icon
    DONOR_ACCEPTED, // Green check icon
    SYSTEM_UPDATE   // Blue info icon
}