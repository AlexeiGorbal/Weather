package com.example.weather.location.saved.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.weather.location.LocationInfo
import com.example.weather.location.saved.repository.local.LocationInfoDao
import com.example.weather.location.saved.repository.local.LocationInfoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedLocationsRepository @Inject constructor(
    private val locationInfoDao: LocationInfoDao
) {

    suspend fun addLocation(location: LocationInfo) {
        locationInfoDao.addLocation(location.toEntity())
    }

    suspend fun removeLocation(location: LocationInfo) {
        locationInfoDao.removeLocation(location.toEntity())
    }

    suspend fun getLocationById(locationId: Long): LocationInfo? {
        return locationInfoDao.getLocationById(locationId)?.toModel()
    }

    fun getLocations(): LiveData<List<LocationInfo>> {
        val locations = locationInfoDao.getLocations().map { locations ->
            locations.asSequence()
                .map { it.toModel() }
                .toList()
        }
        return locations
    }
}

private fun LocationInfo.toEntity(): LocationInfoEntity {
    return LocationInfoEntity(id, name, region, country, lat, lon)
}

private fun LocationInfoEntity.toModel(): LocationInfo {
    return LocationInfo(id, name, region, country, lat, lon)
}