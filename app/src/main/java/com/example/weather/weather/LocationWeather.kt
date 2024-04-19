package com.example.weather.weather

data class LocationWeather(
    val location:Location,
    val currentConditions: CurrentConditions,
    val today: DayWeather,
    val forecast: List<DayWeather>
)