package com.example.bloodlink.presentation.feature_requests.detail

import androidx.lifecycle.SavedStateHandle
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
class RequestDetailViewModel @Inject constructor(
    private val repository: BloodRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    // Safely extract the ID directly from the Navigation Graph!
    private val requestId: String = checkNotNull(savedStateHandle["requestId"])

    private val _requestDetail = MutableStateFlow<BloodRequest?>(null)
    val requestDetail: StateFlow<BloodRequest?> = _requestDetail.asStateFlow()

    init {
        loadRequestDetails()
    }

    private fun loadRequestDetails() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.getBloodRequestById(requestId)
                .onSuccess { request ->
                    _requestDetail.value = request
                }
                .onFailure { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to load request details."
                }

            _isLoading.value = false
        }
    }
}