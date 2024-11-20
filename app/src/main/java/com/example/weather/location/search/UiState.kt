package com.example.weather.location.search

import com.example.weather.location.LocationInfo

sealed class UiState {

    data object Initial : UiState()

    data object Loading : UiState()

    data class Suggestions(val locations: List<LocationInfo>) : UiState()

    data object NoResults : UiState()
}