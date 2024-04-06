package com.example.weather.weather.details.list.currentconditions

import com.example.weather.weather.details.list.WeatherItem

data class CurrentConditionsItem(
    val weatherIcon: String,
    val weatherState: String,
    val temp: String,
    val feelsLike: String
) : WeatherItem