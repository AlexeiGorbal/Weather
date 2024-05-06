package com.example.weather.weather.details.list.hourweather

import com.example.weather.weather.details.list.WeatherItem

data class HourWeatherItem(
    val time: String,
    val weatherIcon: String,
    val temp: String
) : WeatherItem