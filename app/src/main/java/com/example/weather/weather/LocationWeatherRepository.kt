package com.example.weather.weather

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationWeatherRepository @Inject constructor() {

    suspend fun loadWeather(locationId: Long): LocationWeather {
        val hourWeather = mutableListOf(
            HourWeather(1700000000, "icon", 55.0F),
            HourWeather(1700002353, "icon", 45.0F)
        )

        val forecast = mutableListOf(
            DayWeather(
                "icon", "monday", "cloudy", 50.5F, 54.8F, hourWeather
            ),
            DayWeather(
                "icon", "wednesday", "clear", 55.5F, 58.8F, hourWeather
            )
        )

        val currentConditions = CurrentConditions("icon", 54.5F, "cloudy", 53.0F)

        val dayWeather = DayWeather(
            "icon",
            "monday",
            "cloudy",
            50.5F,
            54.8F,
            hourWeather
        )
        return LocationWeather(currentConditions, dayWeather, forecast)
    }
}