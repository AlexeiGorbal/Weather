package com.example.weather.weather.details.repository

import com.example.weather.weather.CurrentConditions
import com.example.weather.weather.DayWeather
import com.example.weather.weather.HourWeather
import com.example.weather.weather.LocationWeather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationWeatherRepository @Inject constructor(
    private val api: WeatherApi
) {

    suspend fun loadWeather(locationId: Long): LocationWeather {
        val weatherEntity = api.getWeather("id:$locationId")

        val currentConditions = weatherEntity.current.toModel()
        val today = weatherEntity.forecast.days[0].toModel()
        val forecast = weatherEntity.forecast.days
            .asSequence()
            .drop(1)
            .map { it.toModel() }
            .toList()

        return LocationWeather(currentConditions, today, forecast)
    }

    private fun CurrentConditionsEntity.toModel(): CurrentConditions {
        return CurrentConditions(weatherState.icon, tempF, weatherState.text, feelsLikeF)
    }

    private fun DayWeatherEntity.toModel(): DayWeather {
        return DayWeather(
            timestamp,
            day.weatherState.icon,
            day.weatherState.text,
            day.minTempF,
            day.maxTempF,
            hours.map { it.toModel() })
    }

    private fun HourWeatherEntity.toModel(): HourWeather {
        return HourWeather(timestamp, weatherState.icon, tempF)
    }
}