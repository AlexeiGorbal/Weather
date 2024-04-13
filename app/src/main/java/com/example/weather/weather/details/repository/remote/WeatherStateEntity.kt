package com.example.weather.weather.details.repository.remote

import com.google.gson.annotations.SerializedName

data class WeatherStateEntity(
    @SerializedName("text") val text: String,
    @SerializedName("icon") val icon: String
)