package com.example.bloodlink.presentation.feature_home

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
class HomeViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    private val _userName = MutableStateFlow("Loading...")
    val userName: StateFlow<String> = _userName.asStateFlow()

    // Tells the UI whether to display the individual donor layout or hospital layout
    private val _isHospital = MutableStateFlow(false)
    val isHospital: StateFlow<Boolean> = _isHospital.asStateFlow()

    // --- RESTORED: Real-time emergency requests stream ---
    private val _bloodRequests = MutableStateFlow<List<BloodRequest>>(emptyList())
    val bloodRequests: StateFlow<List<BloodRequest>> = _bloodRequests.asStateFlow()

    init {
        loadIdentity()
        fetchActiveRequests()
    }

    private fun loadIdentity() {
        viewModelScope.launch {
            // 1. Try to fetch as an Individual Donor
            val donorProfile = repository.getCurrentUser()

            if (donorProfile != null) {
                _userName.value = donorProfile.fullName
                _isHospital.value = false
            } else {
                // 2. If null, try to fetch as a Hospital/Institution from the institutions collection
                repository.getCurrentHospitalProfile().onSuccess { hospital ->
                    _userName.value = hospital.name
                    _isHospital.value = true
                }.onFailure {
                    _userName.value = "Welcome"
                }
            }
        }
    }

    private fun fetchActiveRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            // Collects active emergencies in real-time from your Firestore listener
            repository.getActiveBloodRequests().collect { requests ->
                _bloodRequests.value = requests
                _isLoading.value = false
            }
        }
    }
}