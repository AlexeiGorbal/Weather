package com.example.weather.weather

enum class TemperatureUnit(val id: Int) {

    CELSIUS(1),
    FAHRENHEIT(2);

    companion object {

        fun getById(id: Int): TemperatureUnit {
            return if (id == CELSIUS.id) {
                CELSIUS
            } else {
                FAHRENHEIT
            }
        }
    }
}