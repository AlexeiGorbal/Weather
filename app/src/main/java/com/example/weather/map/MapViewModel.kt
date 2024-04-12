package com.example.weather.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.location.LocationInfo
import com.example.weather.location.saved.SavedLocationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: SavedLocationsRepository
) : ViewModel() {

    private val _selectedLocation = MutableLiveData<LocationInfo>()
    val selectedLocation: LiveData<LocationInfo>
        get() = _selectedLocation

    private val _isLocationSaved = MutableLiveData<Boolean>()
    val isLocationSaved: LiveData<Boolean>
        get() = _isLocationSaved

    fun onLocationSelected(location: LocationInfo) {
        viewModelScope.launch {
            val savedLocation = repository.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null
            _selectedLocation.value = location
        }
    }

    fun onSelectedLocationSavedStateChanged() {
        viewModelScope.launch {
            val selectedLocation = selectedLocation.value ?: return@launch
            val savedLocation = repository.getLocationById(selectedLocation.id)
            if (savedLocation == null) {
                repository.addLocation(selectedLocation)
                _isLocationSaved.value = true
            } else {
                repository.removeLocation(selectedLocation)
                _isLocationSaved.value = false
            }
        }
    }
}