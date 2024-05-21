package com.example.weather.weather.details.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DayWeather")
data class DayWeatherEntity(
    @PrimaryKey val dayWeatherId: Long,
    @ColumnInfo("id") val weatherCreatorId: Long,
    @ColumnInfo("date_epoch") val timestamp: Long,
    @ColumnInfo("mintemp_f") val minTempF: Float,
    @ColumnInfo("maxtemp_f") val maxTempF: Float,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("icon") val icon: String,
    @ColumnInfo("hour") val hours: List<HourWeatherEntity>
) {

//    data class DayEntity(
//        @PrimaryKey
//        @ColumnInfo("mintemp_f") val minTempF: Float,
//        @ColumnInfo("maxtemp_f") val maxTempF: Float,
//        @ColumnInfo("text") val text: String,
//        @ColumnInfo("icon") val icon: String
//    )
}