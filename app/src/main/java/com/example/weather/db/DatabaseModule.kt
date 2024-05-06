package com.example.weather.db

import android.content.Context
import androidx.room.Room
import com.example.weather.location.saved.repository.local.LocationInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideLocationInfoDao(appDatabase: AppDatabase): LocationInfoDao {
        return appDatabase.getLocationInfoDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "weather_database")
            .build()
    }
}