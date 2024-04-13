package com.example.weather.location.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weather.location.LocationInfo
import com.example.weather.location.saved.repository.SavedLocationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedLocationsViewModel @Inject constructor(
    private val repository: SavedLocationsRepository
) : ViewModel() {

    val locations: LiveData<List<LocationInfo>>
        get() = repository.getLocations()
}