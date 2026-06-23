package com.example.bloodlink.presentation.feature_donors.list

import android.util.Log // <-- Added for debugging!
import androidx.lifecycle.SavedStateHandle
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
class DonorListViewModel @Inject constructor(
    private val repository: BloodRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    // 1. Navigation automatically decoded this for us! No Uri.decode needed.
    private val targetBloodGroup: String = savedStateHandle["bloodGroup"] ?: "All"

    // 2. Navigation automatically decoded this too.
    private val rawQuery: String = savedStateHandle["query"] ?: ""
    private val targetQuery: String = if (rawQuery == "ALL") "" else rawQuery

    private val targetDistance: Float = savedStateHandle.get<Float>("distance") ?: 5f

    private val _donors = MutableStateFlow<List<User>>(emptyList())
    val donors: StateFlow<List<User>> = _donors.asStateFlow()

    init {
        performSearch()
    }

    private fun performSearch() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // --- DEBUG LOG: This will print exactly what we are searching for in Logcat! ---
            Log.d("SEARCH_DEBUG", "Searching Firebase for -> Blood: '$targetBloodGroup', City: '$targetQuery'")

            repository.searchDonors(
                bloodGroup = targetBloodGroup,
                city = targetQuery
            ).onSuccess { results ->
                Log.d("SEARCH_DEBUG", "Found ${results.size} donors in Firebase!")
                _donors.value = results
            }.onFailure { error ->
                Log.e("SEARCH_DEBUG", "Search failed: ${error.message}")
                _errorMessage.value = error.localizedMessage ?: "Failed to find donors."
            }

            _isLoading.value = false
        }
    }
}