package com.example.bloodlink.presentation.feature_requests.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.User
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

    private val requestId: String = checkNotNull(savedStateHandle["requestId"])

    private val _requestDetail = MutableStateFlow<BloodRequest?>(null)
    val requestDetail: StateFlow<BloodRequest?> = _requestDetail.asStateFlow()

    private val _isOwnRequest = MutableStateFlow(false)
    val isOwnRequest: StateFlow<Boolean> = _isOwnRequest.asStateFlow()

    private val _requesterProfile = MutableStateFlow<User?>(null)
    val requesterProfile: StateFlow<User?> = _requesterProfile.asStateFlow()

    private val _respondingHeroes = MutableStateFlow<List<User>>(emptyList())
    val respondingHeroes: StateFlow<List<User>> = _respondingHeroes.asStateFlow()

    private val _actionSuccess = MutableStateFlow(false)
    val actionSuccess: StateFlow<Boolean> = _actionSuccess.asStateFlow()

    // --- NEW: Compatibility & Dialog States ---
    var currentUserProfile: User? = null // Holds the hero's data

    private val _showIncompatibleDialog = MutableStateFlow(false)
    val showIncompatibleDialog: StateFlow<Boolean> = _showIncompatibleDialog.asStateFlow()

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

                    // Fetch and save the logged-in user!
                    currentUserProfile = repository.getCurrentUser()

                    if (request.requesterId == currentUserProfile?.id) {
                        _isOwnRequest.value = true
                        fetchRespondingHeroes()
                    } else {
                        _isOwnRequest.value = false
                        fetchRequesterProfile(request.requesterId)
                    }
                }
                .onFailure { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to load request."
                }
            _isLoading.value = false
        }
    }

    private suspend fun fetchRequesterProfile(userId: String) {
        repository.getUserById(userId).onSuccess { user -> _requesterProfile.value = user }
    }

    private suspend fun fetchRespondingHeroes() {
        repository.getRespondingHeroes(requestId).onSuccess { heroes -> _respondingHeroes.value = heroes }
    }

    // --- NEW: Strict Medical Validation ---
    fun acceptRequest() {
        val donorBloodGroup = currentUserProfile?.bloodGroup ?: return
        val patientBloodGroup = _requestDetail.value?.bloodGroup ?: return

        // 1. Check biology first!
        if (!isBloodCompatible(donor = donorBloodGroup, patient = patientBloodGroup)) {
            _showIncompatibleDialog.value = true // Trigger the UI dialog
            return // Stop the process completely
        }

        // 2. If compatible, proceed with Firebase upload
        viewModelScope.launch {
            _isLoading.value = true
            repository.acceptBloodRequest(requestId).onSuccess { _actionSuccess.value = true }
            _isLoading.value = false
        }
    }

    fun dismissDialog() {
        _showIncompatibleDialog.value = false
    }

    fun markAsCompleted() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.markRequestCompleted(requestId).onSuccess { _actionSuccess.value = true }
            _isLoading.value = false
        }
    }

    // --- THE BIOLOGY MATRIX ---
    private fun isBloodCompatible(donor: String, patient: String): Boolean {
        val d = donor.trim().uppercase()
        val p = patient.trim().uppercase()

        return when (d) {
            "O-" -> true // Universal Donor
            "O+" -> p in listOf("O+", "A+", "B+", "AB+")
            "A-" -> p in listOf("A-", "A+", "AB-", "AB+")
            "A+" -> p in listOf("A+", "AB+")
            "B-" -> p in listOf("B-", "B+", "AB-", "AB+")
            "B+" -> p in listOf("B+", "AB+")
            "AB-" -> p in listOf("AB-", "AB+")
            "AB+" -> p == "AB+" // Can only donate to AB+
            else -> false
        }
    }
}