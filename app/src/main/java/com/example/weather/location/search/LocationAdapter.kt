package com.example.weather.location.search

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weather.location.LocationInfo
import com.example.weather.location.LocationInfoDiffCallback

class LocationAdapter(private val onItemClick: (LocationInfo) -> Unit) :
    ListAdapter<LocationInfo, LocationViewHolder>(LocationInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
}