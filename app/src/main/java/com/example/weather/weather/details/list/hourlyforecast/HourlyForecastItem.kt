package com.example.weather.weather.details.list.hourlyforecast

import com.example.weather.weather.details.list.WeatherItem
import com.example.weather.weather.details.list.hourweather.HourWeatherItem

data class HourlyForecastItem(val hourWeather: List<HourWeatherItem>) : WeatherItem