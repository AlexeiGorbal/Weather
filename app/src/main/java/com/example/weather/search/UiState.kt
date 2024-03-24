package com.example.weather.search

sealed class UiState {

    data object Initial : UiState()

    data object Loading : UiState()

    data class Suggestions(val locations: List<Location>) : UiState()

    data object NoResults : UiState()
}