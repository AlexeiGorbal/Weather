package com.example.weather.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationInfo(
    val id: Long,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
) : Parcelable