package com.example.bloodlink.presentation.feature_profile.edit

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    // Form fields
    var fullName = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
    var city = MutableStateFlow("")
    var bloodGroup = MutableStateFlow("") // Usually read-only for safety

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = repository.getCurrentUser()
                if (user != null) {
                    fullName.value = user.fullName
                    phoneNumber.value = user.phoneNumber
                    city.value = user.city
                    bloodGroup.value = user.bloodGroup
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load profile data."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            _isLoading.value = true

            // Simulate saving to database/network
            delay(1000)

            // In a real app, you would call:
            // repository.updateUser(User(..., fullName.value, phoneNumber.value, ...))

            _isLoading.value = false
            _isSaved.value = true // Trigger navigation back
        }
    }
}