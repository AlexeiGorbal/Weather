package com.example.weather.location.search

import com.example.weather.location.LocationInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSearchRepository @Inject constructor() {

    suspend fun searchByInput(input: String): List<LocationInfo> {
        val getListLocations = mutableListOf<LocationInfo>()
        getListLocations.clear()

        val locations = mutableListOf(
            LocationInfo(2801268, "London", "United Kingdom", 51.52, -0.11),
            LocationInfo(2548773, "Los Angeles", "United States of America", 34.05, -118.24),
            LocationInfo(568120, "Berlin", "Germany", 52.52, 13.4),
            LocationInfo(2781297, "Birmingham", "United Kingdom", 52.49, -1.86)
        )

        if (input.isNotEmpty()) {
            locations.forEach {
                if (input == it.name.take(input.length)) {
                    getListLocations.add(it)
                }
            }
        }
        return getListLocations
    }
}