package com.example.bloodlink.presentation.feature_notifications

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.Notification
import com.example.bloodlink.domain.model.NotificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : BaseViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    init {
        fetchNotifications()
    }

    private fun fetchNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(800) // Simulate network delay

            // Mock Data
            _notifications.value = listOf(
                Notification(
                    id = UUID.randomUUID().toString(),
                    title = "Urgent: O+ Needed",
                    message = "A patient at Apollo Hospital urgently requires O+ blood. You are within 5km.",
                    timeAgo = "10 min ago",
                    isRead = false,
                    type = NotificationType.URGENT_REQUEST
                ),
                Notification(
                    id = UUID.randomUUID().toString(),
                    title = "Request Accepted",
                    message = "Rahul Sharma has accepted your blood request and is on the way.",
                    timeAgo = "1 hour ago",
                    isRead = true,
                    type = NotificationType.DONOR_ACCEPTED
                ),
                Notification(
                    id = UUID.randomUUID().toString(),
                    title = "Profile Verified",
                    message = "Your donor profile has been successfully verified. Thank you for joining BloodLink!",
                    timeAgo = "2 days ago",
                    isRead = true,
                    type = NotificationType.SYSTEM_UPDATE
                )
            )
            _isLoading.value = false
        }
    }

    fun markAllAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }
}