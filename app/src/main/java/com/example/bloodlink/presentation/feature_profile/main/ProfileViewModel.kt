package com.example.bloodlink.presentation.feature_profile.main

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.User
import com.example.bloodlink.domain.repository.AuthRepository
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val bloodRepository: BloodRepository,
    private val authRepository: AuthRepository // Inject Auth for logout
) : BaseViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = bloodRepository.getCurrentUser()
                if (user != null) {
                    _currentUser.value = user
                } else {
                    _errorMessage.value = "Profile not found."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load profile."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutEvent.value = true
        }
    }
    fun toggleAvailability(isAvailable: Boolean) {
        viewModelScope.launch {
            // 1. Optimistic UI Update (flips the switch instantly on screen)
            val currentUserData = _currentUser.value ?: return@launch
            _currentUser.value = currentUserData.copy(isAvailableAsDonor = isAvailable)

            // 2. Background Firebase Sync
            bloodRepository.updateDonorAvailability(isAvailable).onFailure {
                _errorMessage.value = "Failed to update status. Please try again."
                // Revert the switch if Firebase fails
                _currentUser.value = currentUserData.copy(isAvailableAsDonor = !isAvailable)
            }
        }
    }
}