package com.example.weather.location.search.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weather.R
import com.example.weather.databinding.ItemSearchLocationBinding
import com.example.weather.location.LocationInfo

class LocationSearchViewHolder(view: View) : ViewHolder(view) {

    private val binding: ItemSearchLocationBinding = ItemSearchLocationBinding.bind(view)

    fun bind(location: LocationInfo, onItemClick: (LocationInfo) -> Unit) {
        binding.name.text = location.name
        binding.region.text = "${location.region}, ${location.country}"
        itemView.setOnClickListener {
            onItemClick(location)
        }
    }

    companion object {

        fun from(parent: ViewGroup): LocationSearchViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_search_location,
                parent, false
            )
            return LocationSearchViewHolder(view)
        }
    }
}