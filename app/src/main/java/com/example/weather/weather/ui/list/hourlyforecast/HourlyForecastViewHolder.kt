package com.example.weather.weather.ui.list.hourlyforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weather.R
import com.example.weather.databinding.ItemHourlyForecastBinding

class HourlyForecastViewHolder(view: View) : ViewHolder(view) {

    private val binding: ItemHourlyForecastBinding = ItemHourlyForecastBinding.bind(view)

    private val adapter = HourlyForecastAdapter()

    init {
        binding.hourlyForecastRecyclerView.adapter = adapter
    }

    fun bind(item: HourlyForecastItem) {
        adapter.submitList(item.hourWeather)
    }

    companion object {

        fun from(parent: ViewGroup): HourlyForecastViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_hourly_forecast,
                parent, false
            )
            return HourlyForecastViewHolder(view)
        }
    }
}