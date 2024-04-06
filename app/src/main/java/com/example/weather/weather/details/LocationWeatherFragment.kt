package com.example.weather.weather.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.weather.databinding.FragmentLocationWeatherBinding
import com.example.weather.weather.HourWeather
import com.example.weather.weather.LocationWeather
import com.example.weather.weather.details.list.LocationWeatherAdapter
import com.example.weather.weather.details.list.WeatherItem
import com.example.weather.weather.details.list.currentconditions.CurrentConditionsItem
import com.example.weather.weather.details.list.dayweather.DayWeatherItem
import com.example.weather.weather.details.list.hourlyforecast.HourlyForecastItem
import com.example.weather.weather.details.list.hourweather.HourWeatherItem
import com.example.weather.weather.details.list.title.TitleItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationWeatherFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentLocationWeatherBinding? = null
    private val binding: FragmentLocationWeatherBinding
        get() = _binding!!

    private val viewModel: LocationWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationWeatherBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = LocationWeatherAdapter()
        binding.weatherRecyclerView.adapter = adapter

        val locationId = arguments?.getLong(LOCATION_ID)
        locationId?.also {
            viewModel.loadWeather(it)
        }

        viewModel.weather.observe(viewLifecycleOwner) {
            val list = mapToWeatherItems(it)
            adapter.submitList(list)
        }
    }

    private fun mapToWeatherItems(weather: LocationWeather): List<WeatherItem> {
        val list = mutableListOf(
            CurrentConditionsItem(
                weather.currentConditions.weatherIcon,
                weather.currentConditions.weatherState,
                weather.currentConditions.tempF.toString(),
                weather.currentConditions.feelsLikeF.toString()

            ),
            TitleItem("Weather during the day"),
            HourlyForecastItem(
                weather.today.hourlyForecast.map(::mapToHourWeatherItem)
            ),
            TitleItem("Weather during a week"),
        )

        val forecast = weather.forecast.map {
            DayWeatherItem(
                it.weatherIcon,
                it.day,
                it.weatherState,
                it.minTempF.toString(),
                it.maxTempF.toString(),
                it.hourlyForecast.map(::mapToHourWeatherItem)
            )
        }
        list.addAll(forecast)

        return list
    }

    private fun mapToHourWeatherItem(weather: HourWeather): HourWeatherItem {
        return HourWeatherItem(
            weather.timestamp.toString(),
            weather.weatherIcon,
            weather.tempF.toString()
        )
    }

    companion object {

        private const val LOCATION_ID = "locationId"

        fun newInstance(locationId: Long) = LocationWeatherFragment().apply {
            val bundle = Bundle()
            bundle.putLong(LOCATION_ID, locationId)
            arguments = bundle
        }
    }
}