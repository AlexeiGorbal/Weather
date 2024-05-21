package com.example.weather.weather.details.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HourWeather")
data class HourWeatherEntity(
    @PrimaryKey val hourWeather: Long,
    @ColumnInfo("id") val dayWeatherCreatorId: Long,
    @ColumnInfo("time_epoch") val timestamp: Long,
    @ColumnInfo("temp_f") val tempF: Float,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("icon") val icon: String
)