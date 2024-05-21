package com.example.weather.weather.details.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WeatherDao {

    @Insert
    suspend fun addWeather(weather: WeatherEntity)

    @Query("SELECT *  FROM LocationInfo WHERE id=:id")
    suspend fun getWeatherById(id: Long): WeatherEntity?

    @Transaction
    @Query("SELECT * FROM Weather")
    suspend fun getWeatherEntityWithDayWeatherEntity(): List<WeatherEntityWithDayWeatherEntity>

    @Transaction
    @Query("SELECT * FROM Weather")
    suspend fun getWeatherEntityWithForecastLocationEntity(): List<WeatherEntityWithForecastLocationEntity>

    @Transaction
    @Query("SELECT * FROM Weather")
    suspend fun getWeatherEntityWithCurrentConditionsEntity(): List<WeatherEntityWithCurrentConditionsEntity>

    @Transaction
    @Query("SELECT * FROM DayWeather")
    suspend fun geDayWeatherEntityWithHourWeatherEntity(): List<DayWeatherEntityWithHourWeatherEntity>
}