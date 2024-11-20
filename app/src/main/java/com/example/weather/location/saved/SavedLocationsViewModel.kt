package com.example.weather.location.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.location.LocationInfo
import com.example.weather.location.saved.repository.SavedLocationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedLocationsViewModel @Inject constructor(
    private val repository: SavedLocationsRepository
) : ViewModel() {

    val locations: Flow<List<LocationInfo>>
        get() = repository.getLocations()

    fun removeLocation(index: Int) {
        viewModelScope.launch {
            val list = locations.firstOrNull() ?: return@launch
            repository.removeLocation(list[index])
        }
    }
}