package com.example.bloodlink.presentation.feature_emergency.network_alerts

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkAlertsViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    private val _alerts = MutableStateFlow<List<BloodRequest>>(emptyList())
    val alerts: StateFlow<List<BloodRequest>> = _alerts.asStateFlow()

    init {
        listenToNetworkAlerts()
    }

    private fun listenToNetworkAlerts() {
        viewModelScope.launch {
            _isLoading.value = true

            // Background coroutine for the hot flow so it doesn't block the UI thread
            viewModelScope.launch {
                repository.getActiveBloodRequests().collect { activeRequests ->
                    // Sort so CRITICAL emergencies are always at the top!
                    // Strict priority: CRITICAL (3) -> HIGH (2) -> NORMAL (1)
                    val sortedAlerts = activeRequests.sortedByDescending { alert ->
                        when (alert.urgencyLevel.uppercase()) {
                            "CRITICAL" -> 4
                            "HIGH" -> 3
                            "MEDIUM" -> 2
                            "NORMAL" -> 1
                            else -> 0
                        }
                    }
                    _alerts.value = sortedAlerts
                    _alerts.value = sortedAlerts
                    _isLoading.value = false
                }
            }
        }
    }
}