package com.example.bloodlink.presentation.feature_donors.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailedDonor(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val bloodGroup: String,
    val distance: String,
    val status: String,
    val lastDonated: String,
    val medication: String,
    val conditions: String,
    val age: String,
    val gender: String,    // NEW
    val city: String,      // NEW
    val area: String,      // NEW
    val totalDonations: Int // NEW
)


@HiltViewModel
class DonorProfileViewModel @Inject constructor(
    private val repository: BloodRepository, // FIX: Inject the real repository
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val donorId: String = checkNotNull(savedStateHandle["donorId"])

    private val _donorData = MutableStateFlow<DetailedDonor?>(null)
    val donorData: StateFlow<DetailedDonor?> = _donorData.asStateFlow()

    init {
        fetchDonorDetails()
    }

    private fun fetchDonorDetails() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                // FIX: Actually fetch the real user from your list!
                val user = repository.getDonorById(donorId)

                if (user != null) {
                    _donorData.value = DetailedDonor(
                        id = user.id,
                        name = user.fullName, // Now it will be Rahul or Neha!
                        phoneNumber = user.phoneNumber,
                        bloodGroup = user.bloodGroup,
                        distance = "${user.distanceKm} km away",
                        status = if (user.isAvailableAsDonor) "Available to Donate" else "Currently Unavailable",

                        // We simulate these medical details since they aren't in the base User model yet
                        // Simulated data (Replace these with real database fields when you update the User model)
                        lastDonated = "12 Oct 2025",
                        medication = "None",
                        conditions = "None",
                        age = "26 Yrs",
                        gender = "Male",
                        city = user.city.ifBlank { "Bhopal" },
                        area = "MP Nagar",
                        totalDonations = 4
                    )
                } else {
                    _errorMessage.value = "Donor not found."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load donor profile."
            } finally {
                _isLoading.value = false
            }
        }
    }
}