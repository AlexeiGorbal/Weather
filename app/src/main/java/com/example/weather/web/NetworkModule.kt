package com.example.weather.web

import com.example.weather.location.search.repository.LocationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideWeatherApi(): LocationApi {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(ApiKeyInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApi::class.java)
    }
}