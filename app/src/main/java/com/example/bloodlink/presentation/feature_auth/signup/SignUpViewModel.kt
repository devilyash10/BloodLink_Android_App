package com.example.bloodlink.presentation.feature_auth.signup

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess: StateFlow<Boolean> = _signUpSuccess.asStateFlow()

    fun signUp(email: String, password: String, name: String, bloodGroup: String, city: String, phone: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank() || bloodGroup.isBlank() || city.isBlank() || phone.isBlank()) {
            _errorMessage.value = "Please fill in all configuration parameters."
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            authRepository.signUp(email.trim(), password.trim(), name.trim(), bloodGroup.trim(), city.trim(), phone.trim())
                .onSuccess { _signUpSuccess.value = true }
                .onFailure { _errorMessage.value = it.localizedMessage ?: "Registration failed." }
            _isLoading.value = false
        }
    }
}