package com.example.bloodlink.presentation.feature_donors.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloodlink.domain.model.User
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonorListViewModel @Inject constructor(
    private val repository: BloodRepository,
    savedStateHandle: SavedStateHandle // Allows getting navigation arguments
) : ViewModel() {

    private val _donorList = MutableStateFlow<List<User>>(emptyList())
    val donorList: StateFlow<List<User>> = _donorList

    init {
        // Here we simulate the filter values you might have passed
        // from the Search screen in a real app
        fetchDonors(bloodGroup = "O+", radiusKm = 10f)
    }

    private fun fetchDonors(bloodGroup: String, radiusKm: Float) {
        viewModelScope.launch {
            repository.getNearbyDonors(bloodGroup, radiusKm).collect { donors ->
                _donorList.value = donors
            }
        }
    }
}