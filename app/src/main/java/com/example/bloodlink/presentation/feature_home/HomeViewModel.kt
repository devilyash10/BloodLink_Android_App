package com.example.bloodlink.presentation.feature_home

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.User
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    // 1. User State (Restored!)
    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState.asStateFlow()

    // 2. Real-Time Blood Requests
    private val _bloodRequests = MutableStateFlow<List<BloodRequest>>(emptyList())
    val bloodRequests: StateFlow<List<BloodRequest>> = _bloodRequests.asStateFlow()

    init {
        fetchUser()
        fetchActiveRequests()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            _userState.value = repository.getCurrentUser()
        }
    }

    private fun fetchActiveRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getActiveBloodRequests()
                .catch { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to load requests"
                    _isLoading.value = false
                }
                .collect { requests ->
                    _bloodRequests.value = requests
                    _isLoading.value = false
                }
        }
    }
}