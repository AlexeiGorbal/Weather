package com.example.weather.weather.ui.list.hourweather

import com.example.weather.weather.ui.list.WeatherItem

data class HourWeatherItem(
    val time: String,
    val weatherIcon: String,
    val temp: String
) : WeatherItem