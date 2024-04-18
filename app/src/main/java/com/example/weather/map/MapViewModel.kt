package com.example.weather.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.location.LocationInfo
import com.example.weather.location.saved.repository.SavedLocationsRepository
import com.example.weather.location.search.repository.LocationSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val savedLocationsRepository: SavedLocationsRepository,
    private val locationSearchRepository: LocationSearchRepository
) : ViewModel() {

    private val _selectedLocation = MutableStateFlow<LocationInfo?>(null)
    val selectedLocation: Flow<LocationInfo>
        get() = _selectedLocation.filterNotNull()

    private val _userLocation = MutableStateFlow<LocationInfo?>(null)
    val userLocation: Flow<LocationInfo>
        get() = _userLocation.filterNotNull()

    val locations: Flow<List<LocationInfo>>
        get() = savedLocationsRepository.getLocations()

    private val _isLocationSaved = MutableStateFlow<Boolean?>(null)
    val isLocationSaved: Flow<Boolean>
        get() = _isLocationSaved.filterNotNull()

    fun onUserLocationAvailable(lat: Double, lon: Double) {
        viewModelScope.launch {
            val location = locationSearchRepository.searchByCoordinates(lat, lon) ?: return@launch
            _userLocation.value = location
            _selectedLocation.value = location

            val savedLocation = savedLocationsRepository.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null
        }
    }

    fun onLocationSelected(location: LocationInfo) {
        viewModelScope.launch {
            _selectedLocation.value = location

            val savedLocation = savedLocationsRepository.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null
        }
    }

    fun onLocationSelectedOnMap(lat: Double, lon: Double) {
        viewModelScope.launch {
            val location = locationSearchRepository.searchByCoordinates(lat, lon) ?: return@launch
            _selectedLocation.value = location

            val savedLocation = savedLocationsRepository.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null
        }
    }

    fun onSelectedLocationSavedStateChanged() {
        viewModelScope.launch {
            val selectedLocation = _selectedLocation.value ?: return@launch
            val savedLocation = savedLocationsRepository.getLocationById(selectedLocation.id)
            if (savedLocation == null) {
                savedLocationsRepository.addLocation(selectedLocation)
                _isLocationSaved.value = true
            } else {
                savedLocationsRepository.removeLocation(selectedLocation)
                _isLocationSaved.value = false
            }
        }
    }
}