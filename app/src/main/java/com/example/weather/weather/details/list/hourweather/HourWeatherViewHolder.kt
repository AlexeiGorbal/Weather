package com.example.weather.weather.details.list.hourweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.ItemHourWeatherBinding

class HourWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemHourWeatherBinding = ItemHourWeatherBinding.bind(view)

    fun bind(item: HourWeatherItem) {
        binding.time.text = item.time
        //       binding.currentWeatherIcon.setImageResource(locationWeather.weatherIcon.toInt())
        binding.currentTemperature.text = item.temp
    }

    companion object {

        fun from(parent: ViewGroup): HourWeatherViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_hour_weather,
                parent, false
            )
            return HourWeatherViewHolder(view)
        }
    }
}