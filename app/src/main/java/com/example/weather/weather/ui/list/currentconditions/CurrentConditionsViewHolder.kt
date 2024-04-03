package com.example.weather.weather.ui.list.currentconditions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.ItemCurrentConditionsBinding

class CurrentConditionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemCurrentConditionsBinding = ItemCurrentConditionsBinding.bind(view)

    fun bind(item: CurrentConditionsItem) {
        //       binding.currentWeatherIcon.setImageResource(locationWeather.weatherIcon.toInt())
        binding.currentWeatherState.text = item.weatherState
        binding.currentTemperature.text = item.temp
        binding.currentFeelsLike.text = item.feelsLike
    }

    companion object {

        fun from(parent: ViewGroup): CurrentConditionsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_current_conditions,
                parent, false
            )
            return CurrentConditionsViewHolder(view)
        }
    }
}