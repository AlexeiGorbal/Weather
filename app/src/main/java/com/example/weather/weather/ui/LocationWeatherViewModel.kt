package com.example.weather.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.search.UiState
import com.example.weather.weather.LocationWeather
import com.example.weather.weather.LocationWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationWeatherViewModel @Inject constructor(
    private val repository: LocationWeatherRepository
) : ViewModel() {

    private val _weather = MutableLiveData<LocationWeather>()
    val weather: LiveData<LocationWeather>
        get() = _weather

    fun loadWeather(locationId: Long) {
        viewModelScope.launch {
            _weather.value = repository.loadWeather(locationId)
        }
    }
}