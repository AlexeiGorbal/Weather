package com.example.weather.location.saved.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LocationInfo")
data class LocationInfoEntity(
    @PrimaryKey
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("region") val region: String,
    @ColumnInfo("country") val country: String,
    @ColumnInfo("lat") val lat: Double,
    @ColumnInfo("lon") val lon: Double
)