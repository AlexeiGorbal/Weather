package com.example.weather.location.saved.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weather.location.LocationInfo
import com.example.weather.location.LocationInfoDiffCallback

class SavedLocationsAdapter(private val onItemClick: (LocationInfo) -> Unit) :
    ListAdapter<LocationInfo, SavedLocationViewHolder>(LocationInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedLocationViewHolder {
        return SavedLocationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SavedLocationViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
}