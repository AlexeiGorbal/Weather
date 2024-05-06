package com.example.weather.location.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.location.search.repository.LocationSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSearchViewModel @Inject constructor(
    private val repository: LocationSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: Flow<UiState>
        get() = _uiState

    fun searchLocations(userInput: String) {
        if (userInput.isBlank()) {
            _uiState.value = UiState.NoResults
            return
        }

        _uiState.value = UiState.Loading

        viewModelScope.launch {
            val result = repository.searchByInput(userInput)
            _uiState.value = if (result.isEmpty()) {
                UiState.NoResults
            } else {
                UiState.Suggestions(result)
            }
        }
    }
}