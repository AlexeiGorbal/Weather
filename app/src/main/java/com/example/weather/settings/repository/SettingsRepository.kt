package com.example.weather.settings.repository

import com.example.weather.settings.repository.local.SettingsSharedPreferences
import com.example.weather.weather.TemperatureUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val prefs: SettingsSharedPreferences
) {

    private val _tempUnitFlow = MutableStateFlow(TemperatureUnit.getById(prefs.tempUnitId))

    suspend fun saveTempUnit(unit: TemperatureUnit) {
        prefs.tempUnitId = unit.id
        _tempUnitFlow.value = unit
    }

    suspend fun getTempUnit(): TemperatureUnit {
        return TemperatureUnit.getById(prefs.tempUnitId)
    }

    fun getObservableTempUnit(): Flow<TemperatureUnit> {
        return _tempUnitFlow
    }
}