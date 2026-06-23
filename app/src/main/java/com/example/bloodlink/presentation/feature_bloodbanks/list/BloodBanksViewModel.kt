package com.example.bloodlink.presentation.feature_bloodbanks.list

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodBank // <-- Updated import
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BloodBanksViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    private val _allBanks = MutableStateFlow<List<BloodBank>>(emptyList())

    private val _filteredBloodBanks = MutableStateFlow<List<BloodBank>>(emptyList())
    val filteredBloodBanks: StateFlow<List<BloodBank>> = _filteredBloodBanks.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchBloodBanks()
    }

    private fun fetchBloodBanks() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.getBloodBanks()
                .onSuccess { banks ->
                    _allBanks.value = banks
                    _filteredBloodBanks.value = banks
                }
                .onFailure { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to load blood banks."
                }

            _isLoading.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _filteredBloodBanks.value = _allBanks.value
        } else {
            _filteredBloodBanks.value = _allBanks.value.filter { bank ->
                bank.name.contains(query, ignoreCase = true) ||
                        bank.address.contains(query, ignoreCase = true)
            }
        }
    }
}