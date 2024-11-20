package com.example.weather.location

import androidx.recyclerview.widget.DiffUtil

class LocationInfoDiffCallback : DiffUtil.ItemCallback<LocationInfo>() {
    override fun areItemsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean {
        return oldItem == newItem
    }
}