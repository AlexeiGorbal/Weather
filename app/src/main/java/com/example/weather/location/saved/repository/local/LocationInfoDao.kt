package com.example.weather.location.saved.repository.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationInfoDao {

    @Insert
    suspend fun addLocation(location: LocationInfoEntity)

    @Delete
    suspend fun removeLocation(location: LocationInfoEntity)

    @Query("SELECT *  FROM LocationInfo WHERE id=:id")
    suspend fun getLocationById(id: Long): LocationInfoEntity?

    @Query("SELECT * FROM LocationInfo")
    fun getLocations(): Flow<List<LocationInfoEntity>>
}