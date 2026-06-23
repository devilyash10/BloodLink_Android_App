package com.example.bloodlink.presentation.feature_profile.hospital

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodBank
import com.example.bloodlink.domain.repository.AuthRepository
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalProfileViewModel @Inject constructor(
    private val repository: BloodRepository,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _hospitalData = MutableStateFlow<BloodBank?>(null)
    val hospitalData: StateFlow<BloodBank?> = _hospitalData.asStateFlow()

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent.asStateFlow()

    init {
        loadHospitalProfile()
    }

    private fun loadHospitalProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getCurrentHospitalProfile().onSuccess { profile ->
                _hospitalData.value = profile
            }.onFailure {
                _errorMessage.value = "Failed to load hospital data."
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutEvent.value = true
        }
    }
}