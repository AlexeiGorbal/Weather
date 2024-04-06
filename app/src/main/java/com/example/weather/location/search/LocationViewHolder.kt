package com.example.weather.location.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weather.R
import com.example.weather.databinding.LocationItemBinding
import com.example.weather.location.LocationInfo

class LocationViewHolder(view: View) : ViewHolder(view) {

    private val binding: LocationItemBinding = LocationItemBinding.bind(view)

    fun bind(location: LocationInfo, onItemClick: (LocationInfo) -> Unit) {
        binding.nameLocation.text = location.name
        binding.nameLocation.setOnClickListener {
            onItemClick(location)
        }
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