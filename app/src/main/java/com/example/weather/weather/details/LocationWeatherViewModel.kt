package com.example.weather.weather.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.settings.repository.SettingsRepository
import com.example.weather.weather.details.repository.LocationWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationWeatherViewModel @Inject constructor(
    private val weatherRepository: LocationWeatherRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: Flow<UiState>
        get() = _uiState

    fun loadWeather(locationId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.DisplayWeather(
                weatherRepository.loadWeather(locationId),
                settingsRepository.getTempUnit()
            )
        }
    }
}