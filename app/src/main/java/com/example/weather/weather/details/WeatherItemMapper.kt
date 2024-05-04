package com.example.weather.weather.details

import android.content.res.Resources
import com.example.weather.R
import com.example.weather.weather.DayWeather
import com.example.weather.weather.HourWeather
import com.example.weather.weather.LocationWeather
import com.example.weather.weather.TemperatureUnit
import com.example.weather.weather.details.list.WeatherItem
import com.example.weather.weather.details.list.currentconditions.CurrentConditionsItem
import com.example.weather.weather.details.list.dayweather.DayWeatherItem
import com.example.weather.weather.details.list.forecastlocation.ForecastLocationItem
import com.example.weather.weather.details.list.hourlyforecast.HourlyForecastItem
import com.example.weather.weather.details.list.hourweather.HourWeatherItem
import com.example.weather.weather.details.list.title.TitleItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class WeatherItemMapper(
    private val resources: Resources,
    private val tempUnit: TemperatureUnit
) {

    private val dayFormatter = SimpleDateFormat("EEEE, d", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    private val timeFormatter = SimpleDateFormat("H:mm", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }

    fun map(weather: LocationWeather): List<WeatherItem> {
        val list = mutableListOf(
            CurrentConditionsItem(
                weather.currentConditions.weatherIcon,
                weather.currentConditions.weatherState,
                convertTemp(weather.currentConditions.tempF, tempUnit).toString(),
                resources.getString(
                    R.string.feels_like,
                    convertTemp(
                        weather.currentConditions.feelsLikeF,
                        tempUnit
                    ).toString()
                )
            ),
            ForecastLocationItem(weather.location.region, weather.location.country),
            TitleItem(resources.getString(R.string.twenty_four_hour)),
            HourlyForecastItem(
                weather.today.hourlyForecast.map(::mapToHourWeatherItem)
            ),
            TitleItem(resources.getString(R.string.seven_day_forecast)),
        )

        val forecast = weather.forecast.map(::mapToDayWeatherItem)
        list.addAll(forecast)

        return list
    }

    private fun mapToHourWeatherItem(weather: HourWeather): HourWeatherItem {
        return HourWeatherItem(
            timeFormatter.format(weather.timestamp.toDate()),
            weather.weatherIcon,
            convertTemp(weather.tempF, tempUnit).toString()
        )
    }

    private fun mapToDayWeatherItem(weather: DayWeather): DayWeatherItem {
        return DayWeatherItem(
            dayFormatter.format(weather.timestamp.toDate()),
            weather.weatherIcon,
            weather.weatherState,
            convertTemp(weather.minTempF, tempUnit).toString(),
            convertTemp(weather.maxTempF, tempUnit).toString(),
            weather.hourlyForecast.map(::mapToHourWeatherItem)
        )
    }

    private fun convertTemp(tempF: Float, tempUnit: TemperatureUnit): Int {
        return if (tempUnit == TemperatureUnit.CELSIUS) {
            (tempF - 32) * (5f / 9f)
        } else {
            tempF
        }.roundToInt()
    }
}

private fun Long.toDate(): Date {
    return Date(this)
}