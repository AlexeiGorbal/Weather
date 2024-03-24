package com.example.weather.search

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor() {

    suspend fun searchByInput(input: String): List<Location> {
        val getListLocations = mutableListOf<Location>()
        getListLocations.clear()

        val locations = mutableListOf(
            Location(2801268, "London", "United Kingdom", 51.52, -0.11),
            Location(2548773, "Los Angeles", "United States of America", 34.05, -118.24),
            Location(568120, "Berlin", "Germany", 52.52, 13.4),
            Location(2781297, "Birmingham", "United Kingdom", 52.49, -1.86)
        )

        locations.forEach {
            if (input == it.name.take(input.length)) {
                getListLocations.add(it)
            }
        }
        return getListLocations
    }
}