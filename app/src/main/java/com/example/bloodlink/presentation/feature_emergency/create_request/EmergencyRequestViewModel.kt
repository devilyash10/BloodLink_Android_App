package com.example.bloodlink.presentation.feature_emergency.create_request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyRequestViewModel @Inject constructor(
    private val repository: BloodRepository
) : ViewModel() {

    fun submitRequest(
        bloodGroup: String,
        location: String,
        urgency: String,
        notes: String
    ) {
        viewModelScope.launch {
            // Logic for submitting to repository will go here
            // Once we have a real backend, we'll await a response
        }
    }
}