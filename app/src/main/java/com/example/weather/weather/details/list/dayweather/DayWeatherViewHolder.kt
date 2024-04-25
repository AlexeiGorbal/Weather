package com.example.weather.weather.details.list.dayweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.databinding.ItemDayWeatherBinding
import com.example.weather.weather.details.list.hourlyforecast.HourlyForecastAdapter

class DayWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemDayWeatherBinding = ItemDayWeatherBinding.bind(view)

    private val adapter = HourlyForecastAdapter()

    init {
        binding.hourlyForecastRecyclerView.adapter = adapter
    }

    fun bind(item: DayWeatherItem, isExpanded: Boolean, onItemClick: (DayWeatherItem) -> Unit) {
        Glide.with(itemView).load(item.weatherIcon).into(binding.currentWeatherIcon)
        binding.day.text = item.day
        binding.currentWeatherState.text = item.weatherState
        binding.minTemp.text = item.minTemp
        binding.maxTemp.text = item.maxTemp
        binding.hourlyForecastRecyclerView.isVisible = isExpanded
        adapter.submitList(item.hourlyForecast)
        itemView.setOnClickListener {
            onItemClick(item)
        }
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