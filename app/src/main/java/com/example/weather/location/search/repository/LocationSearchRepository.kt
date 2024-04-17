package com.example.weather.location.search.repository

import com.example.weather.location.LocationInfo
import com.example.weather.location.search.repository.remote.LocationApi
import com.example.weather.location.search.repository.remote.LocationInfoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSearchRepository @Inject constructor(
    private val api: LocationApi
) {

    suspend fun searchByInput(input: String): List<LocationInfo> {
        return api.getLocationsByInput(input).map { it.toModel() }
    }

    suspend fun searchByCoordinates(lat: Double, lon: Double): LocationInfo? {
        return api.getLocationByCoordinates("$lat,$lon").firstOrNull()?.toModel()
    }

    private fun LocationInfoEntity.toModel(): LocationInfo {
        return LocationInfo(id, name, region, country, lat, lon)
    }
}