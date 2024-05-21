package com.example.weather.weather.details.repository.local

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherEntityWithDayWeatherEntity(
    @Embedded val weather: WeatherEntity,
    @Relation(
        parentColumn = "weatherId",
        entityColumn = "weatherCreatorId"
    )
    val forecast: List<DayWeatherEntity>
)