package com.example.weather.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weather.R
import com.example.weather.databinding.LocationItemBinding

class LocationViewHolder(view: View) : ViewHolder(view) {

    private val binding: LocationItemBinding = LocationItemBinding.bind(view)

    fun bind(location: Location) {
        binding.nameLocation.text = location.name
    }

    companion object {
        fun from(parent: ViewGroup): LocationViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.location_item,
                parent, false
            )
            return LocationViewHolder(view)
        }
    }
}