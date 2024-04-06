package com.example.weather.location.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSearchViewModel @Inject constructor(
    private val repository: LocationSearchRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>(UiState.Initial)
    val uiState: LiveData<UiState>
        get() = _uiState

    fun searchLocations(userInput: String) {
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