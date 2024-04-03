package com.example.weather.weather.ui.list.hourlyforecast

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.weather.weather.ui.list.hourweather.HourWeatherItem
import com.example.weather.weather.ui.list.hourweather.HourWeatherViewHolder

class HourlyForecastAdapter : ListAdapter<HourWeatherItem, HourWeatherViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourWeatherViewHolder {
        return HourWeatherViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HourWeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<HourWeatherItem>() {

        override fun areItemsTheSame(oldItem: HourWeatherItem, newItem: HourWeatherItem): Boolean {
            return true
        }

        override fun areContentsTheSame(
            oldItem: HourWeatherItem,
            newItem: HourWeatherItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}