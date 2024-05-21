package com.example.weather.weather.details.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ForecastLocation")
data class ForecastLocationEntity(
    @PrimaryKey val forecastLocationId: Long,
    @ColumnInfo("id") val weatherCreatorId: Long,
    @ColumnInfo("region") val region: String,
    @ColumnInfo("country") val country: String
)