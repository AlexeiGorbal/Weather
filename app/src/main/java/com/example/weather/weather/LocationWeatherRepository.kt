package com.example.weather.weather

import com.example.weather.location.LocationInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationWeatherRepository @Inject constructor() {

    suspend fun loadWeather(locationId: Long): LocationWeather {
       TODO()
    }
}