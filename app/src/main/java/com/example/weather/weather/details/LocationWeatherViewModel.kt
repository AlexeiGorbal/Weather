package com.example.weather.weather.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.settings.repository.SettingsRepository
import com.example.weather.weather.LocationWeather
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

    private var weather: LocationWeather? = null

    init {
        viewModelScope.launch {
            settingsRepository.getObservableTempUnit().collect {
                val weather = this@LocationWeatherViewModel.weather
                if (weather != null) {
                    _uiState.value = UiState.DisplayWeather(weather, it)
                }
            }
        }
    }

    fun loadWeather(locationId: Long) {
        viewModelScope.launch {
            val weather = weatherRepository.loadWeather(locationId)
            this@LocationWeatherViewModel.weather = weather
            _uiState.value = UiState.DisplayWeather(
                weather,
                settingsRepository.getTempUnit()
            )
        }
    }
}