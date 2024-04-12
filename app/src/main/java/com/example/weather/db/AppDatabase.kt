package com.example.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.location.saved.db.LocationInfoDao
import com.example.weather.location.saved.db.LocationInfoEntity

@Database(entities = [LocationInfoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getLocationInfoDao(): LocationInfoDao
}