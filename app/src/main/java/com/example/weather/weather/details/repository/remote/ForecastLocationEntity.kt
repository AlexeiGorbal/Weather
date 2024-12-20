package com.example.weather.weather.details.repository.remote

import com.google.gson.annotations.SerializedName

data class ForecastLocationEntity(
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String
)