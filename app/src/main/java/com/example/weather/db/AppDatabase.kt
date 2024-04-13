package com.example.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.location.saved.repository.local.LocationInfoDao
import com.example.weather.location.saved.repository.local.LocationInfoEntity

@Database(entities = [LocationInfoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getLocationInfoDao(): LocationInfoDao
}