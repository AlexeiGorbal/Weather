package com.example.weather.weather.ui.list.hourlyforecast

import com.example.weather.weather.ui.list.WeatherItem
import com.example.weather.weather.ui.list.hourweather.HourWeatherItem

data class HourlyForecastItem(val hourWeather: List<HourWeatherItem>) : WeatherItem