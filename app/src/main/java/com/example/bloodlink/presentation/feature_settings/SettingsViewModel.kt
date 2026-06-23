package com.example.bloodlink.presentation.feature_settings

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.User
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _currentUser.value = repository.getCurrentUser()
            _isLoading.value = false
        }
    }

    fun toggleAvailability(isAvailable: Boolean) {
        viewModelScope.launch {
            // 1. Optimistic UI Update (flips the switch instantly on screen)
            _currentUser.value = _currentUser.value?.copy(isAvailableAsDonor = isAvailable)

            // 2. Background Firebase Sync
            repository.updateDonorAvailability(isAvailable).onFailure { error ->
                _errorMessage.value = "Failed to update status. Please try again."
                // Revert the switch if Firebase fails
                _currentUser.value = _currentUser.value?.copy(isAvailableAsDonor = !isAvailable)
            }
        }
    }
}