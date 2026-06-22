package com.example.bloodlink.presentation.feature_settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor() : ViewModel() {

    // Simple state flows for our toggle switches
    private val _pushNotifications = MutableStateFlow(true)
    val pushNotifications: StateFlow<Boolean> = _pushNotifications.asStateFlow()

    private val _locationServices = MutableStateFlow(true)
    val locationServices: StateFlow<Boolean> = _locationServices.asStateFlow()

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    fun togglePushNotifications(enabled: Boolean) {
        _pushNotifications.value = enabled
    }

    fun toggleLocationServices(enabled: Boolean) {
        _locationServices.value = enabled
    }

    fun toggleDarkMode(enabled: Boolean) {
        _darkMode.value = enabled
    }
}