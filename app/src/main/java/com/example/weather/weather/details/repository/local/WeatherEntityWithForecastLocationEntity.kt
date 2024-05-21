package com.example.weather.weather.details.repository.local

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherEntityWithForecastLocationEntity(
    @Embedded val weatherEntity: WeatherEntity,
    @Relation(
        parentColumn = "weatherId",
        entityColumn = "weatherCreatorId"
    )
    val forecastLocation: ForecastLocationEntity
)