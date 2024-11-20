package com.example.weather.weather.details.list.forecastlocation

import com.example.weather.weather.details.list.WeatherItem

data class ForecastLocationItem(
    val region: String,
    val country: String
) : WeatherItem