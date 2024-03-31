package com.example.weather.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.location.LocationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowLocationViewModel @Inject constructor() : ViewModel() {

    private val _selectedLocation = MutableLiveData<LocationInfo>()
    val selectedLocation: LiveData<LocationInfo>
        get() = _selectedLocation

    fun onLocationSelected(location: LocationInfo) {
        _selectedLocation.value = location
    }
}