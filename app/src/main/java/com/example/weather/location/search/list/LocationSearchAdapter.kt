package com.example.weather.location.search.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weather.location.LocationInfo
import com.example.weather.location.LocationInfoDiffCallback

class LocationSearchAdapter(private val onItemClick: (LocationInfo) -> Unit) :
    ListAdapter<LocationInfo, LocationSearchViewHolder>(LocationInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationSearchViewHolder {
        return LocationSearchViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LocationSearchViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
}