package com.example.weather.weather

data class CurrentConditions(
    val weatherIcon: String,
    val weatherState: String,
    val tempF: Float,
    val feelsLikeF: String
)