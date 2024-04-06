package com.example.weather.weather.details.list.dayweather

import com.example.weather.weather.details.list.WeatherItem
import com.example.weather.weather.details.list.hourweather.HourWeatherItem

data class DayWeatherItem(
    val weatherIcon: String,
    val day: String,
    val weatherState: String,
    val minTemp: String,
    val maxTemp: String,
    val hourlyForecast: List<HourWeatherItem>
) : WeatherItem