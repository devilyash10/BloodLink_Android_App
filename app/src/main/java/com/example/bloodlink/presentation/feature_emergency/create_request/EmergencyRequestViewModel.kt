package com.example.bloodlink.presentation.feature_emergency.create_request

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyRequestViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    private val _requestSuccess = MutableStateFlow(false)
    val requestSuccess: StateFlow<Boolean> = _requestSuccess.asStateFlow()

    fun submitEmergencyRequest(
        patientName: String,
        bloodGroup: String,
        hospitalName: String,
        locationArea: String,
        urgencyLevel: String,
        units: String,
        notes: String
    ) {
        // 1. Strict Validation
        if (patientName.isBlank()) {
            _errorMessage.value = "Patient name is required."
            return
        }
        if (hospitalName.isBlank()) {
            _errorMessage.value = "Hospital name is required."
            return
        }
        if (locationArea.isBlank()) {
            _errorMessage.value = "City/Area is required so donors can find you."
            return
        }

        // Safely convert units to an integer
        val unitsInt = units.toIntOrNull() ?: 1

        // 2. Push to Firebase via Repository
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.createBloodRequest(
                patientName = patientName.trim(),
                bloodGroup = bloodGroup.trim(),
                hospitalName = hospitalName.trim(),
                locationArea = locationArea.trim(),
                urgencyLevel = urgencyLevel,
                unitsRequired = unitsInt,
                additionalNotes = notes.trim()
            ).onSuccess {
                // If Firebase accepts it, trigger the success navigation!
                _requestSuccess.value = true
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Failed to post emergency request."
            }

            _isLoading.value = false
        }
    }
}