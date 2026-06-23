package com.example.bloodlink.presentation.feature_requests.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.RequestStatus
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

    private val _request = MutableStateFlow<BloodRequest?>(null)
    val request: StateFlow<BloodRequest?> = _request.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isHospital = MutableStateFlow(false)
    val isHospital: StateFlow<Boolean> = _isHospital.asStateFlow()

    private val _isOwner = MutableStateFlow(false)
    val isOwner: StateFlow<Boolean> = _isOwner.asStateFlow()

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    private val _respondingHeroes = MutableStateFlow<List<User>>(emptyList())
    val respondingHeroes: StateFlow<List<User>> = _respondingHeroes.asStateFlow()

    private val _requesterPhone = MutableStateFlow("")
    val requesterPhone: StateFlow<String> = _requesterPhone.asStateFlow()

    private val _requesterName = MutableStateFlow("Loading...")
    val requesterName: StateFlow<String> = _requesterName.asStateFlow()

    private val _requesterType = MutableStateFlow("INDIVIDUAL")
    val requesterType: StateFlow<String> = _requesterType.asStateFlow()

    init {
        loadRequestDetails()
    }

    fun loadRequestDetails() {
        viewModelScope.launch {
            _isLoading.value = true
            val user = repository.getCurrentUser()
            if (user != null) {
                _currentUser.value = user
            } else {
                repository.getCurrentHospitalProfile().onSuccess { _isHospital.value = true }
            }

            repository.getBloodRequestById(requestId).onSuccess { req ->
                _request.value = req
                _isCompleted.value = req.status == RequestStatus.COMPLETED

                val currentUserId = user?.id ?: ""
                _isOwner.value = currentUserId == req.createdBy

                repository.getUserById(req.createdBy).onSuccess { creator ->
                    _requesterName.value = creator.fullName
                    _requesterPhone.value = creator.phoneNumber.ifBlank { "0000000000" }
                    _requesterType.value = "INDIVIDUAL"
                }.onFailure {
                    _requesterName.value = req.hospitalName.ifBlank { "Hospital Network" }
                    _requesterPhone.value = "+91 1800-123-4567"
                    _requesterType.value = "HOSPITAL"
                }

                repository.getRespondingHeroes(req.requestId).onSuccess { heroes ->
                    _respondingHeroes.value = heroes
                }
            }.onFailure {
                _errorMessage.value = "Failed to load request details."
            }
            _isLoading.value = false
        }
    }

    fun respondToRequest() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.acceptBloodRequest(requestId).onSuccess {
                loadRequestDetails()
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Failed to respond."
            }
            _isLoading.value = false
        }
    }

    fun resolveFromOtherSources() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.resolveRequestWithOutsideSource(requestId).onSuccess {
                loadRequestDetails()
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Failed to close request."
            }
            _isLoading.value = false
        }
    }

    fun resolveWithHero(heroId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.resolveRequestWithHero(requestId, heroId).onSuccess {
                loadRequestDetails()
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage ?: "Fulfillment allocation error."
            }
            _isLoading.value = false
        }
    }
}