package com.example.bloodlink.presentation.feature_emergency.my_request

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.RequestStatus
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class RequestFilter { ALL, ACTIVE, COMPLETED }

@HiltViewModel
class MyRequestsViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    private val _allRequests = MutableStateFlow<List<BloodRequest>>(emptyList())
    private val _currentFilter = MutableStateFlow(RequestFilter.ALL)

    // THE UPGRADE: This automatically reacts to both Firebase updates AND Tab clicks perfectly!
    val filteredRequests: StateFlow<List<BloodRequest>> = combine(
        _allRequests,
        _currentFilter
    ) { requests, filter ->
        when (filter) {
            RequestFilter.ALL -> requests
            RequestFilter.ACTIVE -> requests.filter { it.status == RequestStatus.ACTIVE }
            RequestFilter.COMPLETED -> requests.filter { it.status == RequestStatus.COMPLETED }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchMyRequests()
    }

    private fun fetchMyRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getMyBloodRequests().collect { requests ->
                _allRequests.value = requests
                _isLoading.value = false
            }
        }
    }

    fun setFilter(filter: RequestFilter) {
        _currentFilter.value = filter
    }

    // THE NEW ACTION: Closes the request in Firebase
    fun markAsCompleted(requestId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Ensure your BloodRepository has a function to update the status in Firestore!
            repository.markRequestFulfilled(requestId).onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Failed to update status."
            }
            _isLoading.value = false
        }
    }
}