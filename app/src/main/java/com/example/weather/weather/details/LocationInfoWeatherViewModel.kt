package com.example.weather.weather.details

import androidx.lifecycle.ViewModel
import com.example.weather.location.LocationInfo
import com.example.weather.location.saved.repository.SavedLocationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LocationInfoWeatherViewModel @Inject constructor(
    private val savedLocationsRepository: SavedLocationsRepository
) : ViewModel() {

    val locations: Flow<List<LocationInfo>>
        get() = savedLocationsRepository.getLocations()
}