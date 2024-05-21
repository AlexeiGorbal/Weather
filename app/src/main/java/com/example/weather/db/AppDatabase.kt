package com.example.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.location.saved.repository.local.LocationInfoDao
import com.example.weather.location.saved.repository.local.LocationInfoEntity
import com.example.weather.weather.details.repository.local.CurrentConditionsEntity
import com.example.weather.weather.details.repository.local.DayWeatherEntity
import com.example.weather.weather.details.repository.local.ForecastLocationEntity
import com.example.weather.weather.details.repository.local.HourWeatherEntity
import com.example.weather.weather.details.repository.local.WeatherDao
import com.example.weather.weather.details.repository.local.WeatherEntity

@Database(
    entities = [
        LocationInfoEntity::class,
        WeatherEntity::class,
        ForecastLocationEntity::class,
        CurrentConditionsEntity::class,
        DayWeatherEntity::class,
        HourWeatherEntity::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getLocationInfoDao(): LocationInfoDao

    abstract fun getWeatherDao(): WeatherDao
}