package com.example.weather.weather.details.repository.local

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherEntityWithCurrentConditionsEntity(
    @Embedded val weatherEntity: WeatherEntity,
    @Relation(
        parentColumn = "weatherId",
        entityColumn = "weatherCreatorId"
    )
    val currentConditions: CurrentConditionsEntity
)