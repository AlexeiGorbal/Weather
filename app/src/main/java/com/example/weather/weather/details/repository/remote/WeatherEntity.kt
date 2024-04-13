package com.example.weather.weather.details.repository.remote

import com.google.gson.annotations.SerializedName

data class WeatherEntity(
    @SerializedName("current") val current: CurrentConditionsEntity,
    @SerializedName("forecast") val forecast: ForecastEntity,
) {

    data class ForecastEntity(
        @SerializedName("forecastday") val days: List<DayWeatherEntity>
    )
}