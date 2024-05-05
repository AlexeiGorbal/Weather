package com.example.weather.ongoing

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weather.CHANNEL_ID
import com.example.weather.R
import com.example.weather.location.search.repository.LocationSearchRepository
import com.example.weather.weather.details.repository.LocationWeatherRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class UpdateOngoingNotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val weatherRepository: LocationWeatherRepository,
    private val locationSearchRepository: LocationSearchRepository
) : CoroutineWorker(appContext, workerParams) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)

    override suspend fun doWork(): Result {
        val permissionResult =
            applicationContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            val location = withContext(Dispatchers.IO) {
                Tasks.await(
                    fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        null
                    )
                )
            }

            if (location != null) {
                val locationInfo = locationSearchRepository.searchByCoordinates(
                    location.latitude,
                    location.longitude
                )
                if (locationInfo != null) {
                    val weather = weatherRepository.loadWeather(locationInfo.id)

                    val build = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                        .setContentTitle(applicationContext.resources.getString(R.string.app_name))
                        .setContentText("${weather.currentConditions.tempF}° ${weather.location.region}, ${weather.location.country}")

                    with(NotificationManagerCompat.from(applicationContext)) {
                        if (applicationContext.checkSelfPermission(
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            notify(101, build.build())
                        }
                    }
                }
            }
        }

        return Result.success()
    }
}