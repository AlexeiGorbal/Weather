package com.example.weather.weather.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.weather.LocationWeather
import com.example.weather.weather.details.repository.LocationWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationWeatherViewModel @Inject constructor(
    private val repository: LocationWeatherRepository
) : ViewModel() {

    private val _weather = MutableStateFlow<LocationWeather?>(null)
    val weather: Flow<LocationWeather>
        get() = _weather.filterNotNull()

    fun loadWeather(locationId: Long) {
        viewModelScope.launch {
            _weather.value = repository.loadWeather(locationId)
        }
    }
}