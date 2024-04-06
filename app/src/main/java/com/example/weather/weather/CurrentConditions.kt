package com.example.weather.weather

data class CurrentConditions(
    val weatherIcon: String,
    val tempF: Float,
    val weatherState: String,
    val feelsLikeF: Float
)