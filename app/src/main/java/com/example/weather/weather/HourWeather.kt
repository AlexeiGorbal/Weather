package com.example.weather.weather

data class HourWeather(
    val timestamp: Long,
    val weatherIcon: String,
    val tempF: Float
)