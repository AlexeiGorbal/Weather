package com.example.weather.weather.ui.list.dayweather

import com.example.weather.weather.ui.list.WeatherItem
import com.example.weather.weather.ui.list.hourweather.HourWeatherItem

data class DayWeatherItem(
    val weatherIcon: String,
    val day: String,
    val weatherState: String,
    val minTemp: String,
    val maxTemp: String,
    val hourlyForecast: List<HourWeatherItem>
) : WeatherItem