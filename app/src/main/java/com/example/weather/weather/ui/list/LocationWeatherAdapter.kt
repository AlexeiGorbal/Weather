package com.example.weather.weather.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.weather.ui.list.currentconditions.CurrentConditionsItem
import com.example.weather.weather.ui.list.currentconditions.CurrentConditionsViewHolder
import com.example.weather.weather.ui.list.dayweather.DayWeatherItem
import com.example.weather.weather.ui.list.dayweather.DayWeatherViewHolder
import com.example.weather.weather.ui.list.hourlyforecast.HourlyForecastItem
import com.example.weather.weather.ui.list.hourlyforecast.HourlyForecastViewHolder
import com.example.weather.weather.ui.list.title.TitleItem
import com.example.weather.weather.ui.list.title.TitleViewHolder

class LocationWeatherAdapter :
    ListAdapter<WeatherItem, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CurrentConditionsItem -> CURRENT_CONDITIONS_VIEW_TYPE
            is DayWeatherItem -> DAY_WEATHER_VIEW_TYPE
            is HourlyForecastItem -> HOUR_FORECAST_VIEW_TYPE
            is TitleItem -> TITLE_VIEW_TYPE
            else -> throw IllegalStateException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CURRENT_CONDITIONS_VIEW_TYPE -> CurrentConditionsViewHolder.from(parent)
            DAY_WEATHER_VIEW_TYPE -> DayWeatherViewHolder.from(parent)
            HOUR_FORECAST_VIEW_TYPE -> HourlyForecastViewHolder.from(parent)
            TITLE_VIEW_TYPE -> TitleViewHolder.from(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CurrentConditionsViewHolder -> holder.bind(getItem(position) as CurrentConditionsItem)
            is DayWeatherViewHolder -> holder.bind(getItem(position) as DayWeatherItem)
            is HourlyForecastViewHolder -> holder.bind(getItem(position) as HourlyForecastItem)
            is TitleViewHolder -> holder.bind(getItem(position) as TitleItem)
            else -> throw IllegalStateException()
        }
    }

    companion object {

        private const val CURRENT_CONDITIONS_VIEW_TYPE = 0
        private const val DAY_WEATHER_VIEW_TYPE = 1
        private const val HOUR_FORECAST_VIEW_TYPE = 2
        private const val TITLE_VIEW_TYPE = 3
    }

    private class DiffCallback : DiffUtil.ItemCallback<WeatherItem>() {

        override fun areItemsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
            return when {
                oldItem is CurrentConditionsItem && newItem is CurrentConditionsItem -> true
                oldItem is DayWeatherItem && newItem is DayWeatherItem -> true
                oldItem is HourlyForecastItem && newItem is HourlyForecastItem -> true
                oldItem is TitleItem && newItem is TitleItem -> true
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
            return oldItem == newItem
        }
    }
}