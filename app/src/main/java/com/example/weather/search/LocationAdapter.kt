package com.example.weather.search

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class LocationAdapter(private val onItemClick: (LocationInfo) -> Unit) :
    ListAdapter<LocationInfo, LocationViewHolder>(LocationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    private class LocationDiffCallback : DiffUtil.ItemCallback<LocationInfo>() {

        override fun areItemsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean {
            return oldItem == newItem
        }
    }
}