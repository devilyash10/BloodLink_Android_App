package com.example.bloodlink.presentation.feature_emergency.my_request

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.RequestStatus
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRequestsViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    private val _allRequests = MutableStateFlow<List<BloodRequest>>(emptyList())

    private val _filteredRequests = MutableStateFlow<List<BloodRequest>>(emptyList())
    val filteredRequests: StateFlow<List<BloodRequest>> = _filteredRequests.asStateFlow()

    init {
        loadRequests()
    }

    private fun loadRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getMyRequests().collect { requests ->
                    _allRequests.value = requests
                    _filteredRequests.value = requests // Default to showing all
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load requests"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByStatus(status: RequestStatus) {
        _filteredRequests.value = _allRequests.value.filter { it.status == status }
    }

    fun showAll() {
        _filteredRequests.value = _allRequests.value
    }
}