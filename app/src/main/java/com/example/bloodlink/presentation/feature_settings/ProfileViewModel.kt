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
class ProfileViewModel @Inject constructor(
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
            try {
                _currentUser.value = repository.getCurrentUser()
            } catch (e: Exception) {
                _errorMessage.value = "Could not load user data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logOut() {
        // In the future, this will clear the Room Database and Firebase session
        viewModelScope.launch {
            // Simulate logout delay
            _isLoading.value = true
            kotlinx.coroutines.delay(500)
            _isLoading.value = false
        }
    }
}