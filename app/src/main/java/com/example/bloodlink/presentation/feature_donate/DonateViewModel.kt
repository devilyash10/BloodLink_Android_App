package com.example.bloodlink.presentation.feature_donate

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodBank
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.repository.BloodRepository
import com.example.bloodlink.domain.util.BloodCompatibility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonateViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    private val _userBloodGroup = MutableStateFlow("Unknown")

    // Tab 1: Split Emergency Lists
    private val _eligibleRequests = MutableStateFlow<List<BloodRequest>>(emptyList())
    val eligibleRequests: StateFlow<List<BloodRequest>> = _eligibleRequests.asStateFlow()

    private val _ineligibleRequests = MutableStateFlow<List<BloodRequest>>(emptyList())
    val ineligibleRequests: StateFlow<List<BloodRequest>> = _ineligibleRequests.asStateFlow()

    // Tab 2: Routine Donation Banks
    private val _bloodBanks = MutableStateFlow<List<BloodBank>>(emptyList())
    val bloodBanks: StateFlow<List<BloodBank>> = _bloodBanks.asStateFlow()

    init {
        loadDonationData()
    }

    private fun loadDonationData() {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. Get User's Blood Group
            val user = repository.getCurrentUser()
            val myBloodGroup = user?.bloodGroup ?: "Unknown"
            _userBloodGroup.value = myBloodGroup

            // 2. THE FIX: Launch the Firebase listener in a separate coroutine!
            viewModelScope.launch {
                repository.getActiveBloodRequests().collect { allRequests ->
                    val (eligible, ineligible) = allRequests.partition { request ->
                        BloodCompatibility.canDonate(myBloodGroup, request.bloodGroup)
                    }
                    _eligibleRequests.value = eligible
                    _ineligibleRequests.value = ineligible
                }
            }

            // 3. Fetch Blood Banks for Tab 2
            repository.getBloodBanks().onSuccess { banks ->
                _bloodBanks.value = banks
            }

            // 4. Because the listener is in its own launch block, this will now execute!
            _isLoading.value = false
        }
    }
}