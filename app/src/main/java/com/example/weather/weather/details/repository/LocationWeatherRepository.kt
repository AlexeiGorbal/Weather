package com.example.weather.weather.details.repository

import com.example.weather.weather.CurrentConditions
import com.example.weather.weather.DayWeather
import com.example.weather.weather.HourWeather
import com.example.weather.weather.Location
import com.example.weather.weather.LocationWeather
import com.example.weather.weather.details.repository.remote.CurrentConditionsEntity
import com.example.weather.weather.details.repository.remote.DayWeatherEntity
import com.example.weather.weather.details.repository.remote.HourWeatherEntity
import com.example.weather.weather.details.repository.remote.LocationEntity
import com.example.weather.weather.details.repository.remote.WeatherApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationWeatherRepository @Inject constructor(
    private val api: WeatherApi
) {

    suspend fun loadWeather(locationId: Long): LocationWeather {
        val weatherEntity = api.getWeather("id:$locationId")

        val location = weatherEntity.location.toModel()
        val currentConditions = weatherEntity.current.toModel()
        val today = weatherEntity.forecast.days[0].toModel()
        val forecast = weatherEntity.forecast.days
            .asSequence()
            .drop(1)
            .map { it.toModel() }
            .toList()

        return LocationWeather(location, currentConditions, today, forecast)
    }

    private fun LocationEntity.toModel(): Location {
        return Location(region, country)
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