package com.example.weather.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

class LocationAdapter : Adapter<LocationViewHolder>() {

    private val listLocations = mutableListOf<Location>()

    fun addLocations(listLocations: List<Location>) {
        this.listLocations.clear()
        this.listLocations.addAll(listLocations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(listLocations[position])
    }

    override fun getItemCount(): Int {
        return listLocations.size
    }
}