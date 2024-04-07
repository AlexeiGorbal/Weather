package com.example.weather.location.search.repository

import com.example.weather.location.LocationInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSearchRepository @Inject constructor(
    private val api: LocationApi
) {

    suspend fun searchByInput(input: String): List<LocationInfo> {
        return api.getLocationsByInput(input).map { it.toModel() }
    }

    private fun LocationInfoEntity.toModel(): LocationInfo {
        return LocationInfo(id, name, region, country, lat, lon)
    }
}