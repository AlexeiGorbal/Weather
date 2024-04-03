package com.example.weather.weather.ui.list

interface WeatherItem {

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}