package com.example.weather.weather

data class DayWeather(
    val timestamp: Long,
    val weatherIcon: String,
    val weatherState: String,
    val minTempF: Float,
    val maxTempF: Float,
    val hourlyForecast: List<HourWeather>
)