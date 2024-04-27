package com.example.weather.weather.details.list.forecastlocation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.ItemForecastLocationBinding

class ForecastLocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemForecastLocationBinding = ItemForecastLocationBinding.bind(view)

    fun bind(item: ForecastLocationItem) {
        binding.currentRegion.text = item.region
        binding.currentCountry.text = item.country
    }

    companion object {

        fun from(parent: ViewGroup): ForecastLocationViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_forecast_location,
                parent, false
            )
            return ForecastLocationViewHolder(view)
        }
    }
}