package com.example.weather.weather.details.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CurrentConditions")
data class CurrentConditionsEntity(
    @PrimaryKey val currentConditionsId: Long,
    @ColumnInfo("id") val weatherCreatorId: Long,
    @ColumnInfo("temp_f") val tempF: Float,
    @ColumnInfo("feelslike_f") val feelsLikeF: Float,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("icon") val icon: String
)