package com.example.weather.weather.details.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Weather")
data class WeatherEntity(
    @PrimaryKey val weatherId: Long,
    @ColumnInfo("location") val location: ForecastLocationEntity,
    @ColumnInfo("current") val current: CurrentConditionsEntity,
    @ColumnInfo("forecast") val forecast: List<DayWeatherEntity>,
) {

//    @Entity(tableName = "Forecast")
//    data class ForecastEntity(
//        @PrimaryKey val forecast: Long,
//        @ColumnInfo("id") val weatherCreatorId: Long,
//        @ColumnInfo("forecastday") val days: List<DayWeatherEntity>
//    )
}