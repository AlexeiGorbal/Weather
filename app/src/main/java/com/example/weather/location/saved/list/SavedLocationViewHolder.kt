package com.example.weather.location.saved.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weather.R
import com.example.weather.databinding.ItemSavedLocationBinding
import com.example.weather.location.LocationInfo

class SavedLocationViewHolder(view: View) : ViewHolder(view) {

    val binding = ItemSavedLocationBinding.bind(view)

    fun bind(location: LocationInfo, onItemClick: (LocationInfo) -> Unit) {
        binding.name.text = location.name
        binding.region.text = "${location.region}, ${location.country}"
        itemView.setOnClickListener {
            onItemClick(location)
        }
    }

    companion object {

        fun from(parent: ViewGroup): SavedLocationViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_saved_location, parent, false)
            return SavedLocationViewHolder(view)
        }
    }
}