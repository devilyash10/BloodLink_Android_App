package com.example.bloodlink.presentation.feature_bloodbanks.list

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodBank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BloodBanksViewModel @Inject constructor() : BaseViewModel() {

    private val _allBloodBanks = MutableStateFlow<List<BloodBank>>(emptyList())

    private val _filteredBloodBanks = MutableStateFlow<List<BloodBank>>(emptyList())
    val filteredBloodBanks: StateFlow<List<BloodBank>> = _filteredBloodBanks.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchNearbyBloodBanks()
    }

    private fun fetchNearbyBloodBanks() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(600)

            val mockData = listOf(
                BloodBank("bb_1", "Apollo Hospital Blood Bank", "Bannerghatta Road, Bangalore", 2.4, "+91 80000 12345", true, listOf("A+", "O+", "AB+")),
                BloodBank("bb_2", "Red Cross Society", "Ashoka Pillar, Bangalore", 4.1, "+91 80000 54321", true, listOf("O-", "B+", "A-", "AB-")),
                BloodBank("bb_3", "Fortis Healthcare", "Cunningham Road, Bangalore", 6.8, "+91 80000 98765", false, listOf("B-", "O+")),
                BloodBank("bb_4", "Lions Club Blood Centre", "Jayanagar, Bangalore", 8.2, "+91 80000 11223", true, listOf("A+", "B+", "O+", "AB+"))
            ).sortedBy { it.distanceKm }

            _allBloodBanks.value = mockData
            _filteredBloodBanks.value = mockData
            _isLoading.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _filteredBloodBanks.value = _allBloodBanks.value
        } else {
            _filteredBloodBanks.value = _allBloodBanks.value.filter {
                it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
            }
        }
    }
}