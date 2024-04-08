package com.example.weather.weather.details.repository

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast.json?days=8")
    suspend fun getWeather(@Query("q") locationId: String): WeatherEntity
}