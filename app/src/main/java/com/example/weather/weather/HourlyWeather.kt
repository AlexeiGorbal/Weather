package com.example.weather.weather

data class HourlyWeather(
    val timestamp: Long,
    val weatherIcon: String,
    val tempF: Float
)