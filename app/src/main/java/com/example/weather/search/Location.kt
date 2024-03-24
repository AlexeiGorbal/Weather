package com.example.weather.search

data class Location(
    val id: Long,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
)