package com.example.weather.weather.details

import com.example.weather.weather.LocationWeather
import com.example.weather.weather.TemperatureUnit

sealed class UiState {

    data object Initial : UiState()

    data class DisplayWeather(
        val weather: LocationWeather,
        val tempUnit: TemperatureUnit
    ) : UiState()
}