package com.example.weather.weather.details.list.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.ItemLocationBinding

class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemLocationBinding = ItemLocationBinding.bind(view)

    fun bind(item: LocationItem) {
        binding.currentRegion.text = item.region
        binding.currentCountry.text = item.country
    }

    companion object {

        fun from(parent: ViewGroup): LocationViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_location,
                parent, false
            )
            return LocationViewHolder(view)
        }
    }
}