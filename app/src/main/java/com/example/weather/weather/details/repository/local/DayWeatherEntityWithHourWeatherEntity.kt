package com.example.weather.weather.details.repository.local

import androidx.room.Embedded
import androidx.room.Relation

data class DayWeatherEntityWithHourWeatherEntity(
    @Embedded val dayWeather: DayWeatherEntity,
    @Relation(
        parentColumn = "dayWeatherId",
        entityColumn = "dayWeatherCreatorId"
    )
    val hours: List<HourWeatherEntity>
)