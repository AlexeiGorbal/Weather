package com.example.weather.location

data class LocationInfo(
    val id: Long,
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double
)