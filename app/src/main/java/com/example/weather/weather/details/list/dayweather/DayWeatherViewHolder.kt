package com.example.weather.weather.details.list.dayweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.ItemDayWeatherBinding

class DayWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemDayWeatherBinding = ItemDayWeatherBinding.bind(view)

    fun bind(item: DayWeatherItem) {
        //       binding.currentWeatherIcon.setImageResource(locationWeather.weatherIcon.toInt())
        binding.day.text = item.day
        binding.currentWeatherState.text = item.weatherState
        binding.minTemp.text = item.minTemp
        binding.maxTemp.text = item.maxTemp
    }

    companion object {

        fun from(parent: ViewGroup): DayWeatherViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_day_weather,
                parent, false
            )
            return DayWeatherViewHolder(view)
        }
    }
}