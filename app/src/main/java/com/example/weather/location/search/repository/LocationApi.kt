package com.example.weather.location.search.repository

import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {

    @GET("search.json?")
    suspend fun getLocationsByInput(@Query("q") input: String): List<LocationInfoEntity>
}