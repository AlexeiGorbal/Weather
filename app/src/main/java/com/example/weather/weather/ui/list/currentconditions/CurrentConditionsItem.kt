package com.example.weather.weather.ui.list.currentconditions

import com.example.weather.weather.ui.list.WeatherItem

data class CurrentConditionsItem(
    val weatherIcon: String,
    val weatherState: String,
    val temp: String,
    val feelsLike: String
) : WeatherItem