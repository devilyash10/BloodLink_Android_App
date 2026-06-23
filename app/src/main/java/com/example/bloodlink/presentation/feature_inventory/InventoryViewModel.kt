package com.example.bloodlink.presentation.feature_inventory

import androidx.lifecycle.viewModelScope
import com.example.bloodlink.core.base.BaseViewModel
import com.example.bloodlink.domain.repository.BloodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: BloodRepository
) : BaseViewModel() {

    // Default map of all blood groups set to 0
    private val defaultInventory = mapOf(
        "O+" to 0, "O-" to 0, "A+" to 0, "A-" to 0,
        "B+" to 0, "B-" to 0, "AB+" to 0, "AB-" to 0
    )

    private val _inventoryState = MutableStateFlow<Map<String, Int>>(defaultInventory)
    val inventoryState: StateFlow<Map<String, Int>> = _inventoryState.asStateFlow()

    private val _hasUnsavedChanges = MutableStateFlow(false)
    val hasUnsavedChanges: StateFlow<Boolean> = _hasUnsavedChanges.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    init {
        loadCurrentInventory()
    }

    private fun loadCurrentInventory() {
        viewModelScope.launch {
            _isLoading.value = true
            // In a real app, you'd fetch the existing map from Firebase here and merge it with defaultInventory.
            // For now, we start at 0 so they can initialize their vault.
            _isLoading.value = false
        }
    }

    fun updateStock(bloodGroup: String, change: Int) {
        val currentStock = _inventoryState.value[bloodGroup] ?: 0
        val newStock = (currentStock + change).coerceAtLeast(0) // Prevents negative stock

        val updatedMap = _inventoryState.value.toMutableMap()
        updatedMap[bloodGroup] = newStock

        _inventoryState.value = updatedMap
        _hasUnsavedChanges.value = true
    }

    fun markOutOfStock(bloodGroup: String) {
        val updatedMap = _inventoryState.value.toMutableMap()
        updatedMap[bloodGroup] = 0
        _inventoryState.value = updatedMap
        _hasUnsavedChanges.value = true
    }

    fun saveInventoryToDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateHospitalInventory(_inventoryState.value).onSuccess {
                _hasUnsavedChanges.value = false
                _saveSuccess.value = true
            }.onFailure {
                _errorMessage.value = "Failed to update inventory. Try again."
            }
            _isLoading.value = false
        }
    }

    fun resetSaveSuccess() { _saveSuccess.value = false }
}