package com.example.weather.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.settings.repository.SettingsRepository
import com.example.weather.weather.TemperatureUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    val tempUnit = repository.getObservableTempUnit()

    fun changeTempUnit(unit: TemperatureUnit) {
        viewModelScope.launch {
            repository.saveTempUnit(unit)
        }
    }
}