package com.example.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

class InformationListAdapter : Adapter<InformationListViewHolder>() {

    private val weather = mutableListOf<Information>(
        Information("9:00", R.drawable.ic_launcher_background, "3"),
        Information("10:00", R.drawable.ic_launcher_background, "3"),
        Information("11:00", R.drawable.ic_launcher_background, "3"),
        Information("12:00", R.drawable.ic_launcher_background, "3"),
        Information("13:00", R.drawable.ic_launcher_background, "3"),
        Information("14:00", R.drawable.ic_launcher_background, "3"),
        Information("15:00", R.drawable.ic_launcher_background, "3"),
        Information("16:00", R.drawable.ic_launcher_background, "3"),
        Information("17:00", R.drawable.ic_launcher_background, "3")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.day_temp_item, parent, false)
        return InformationListViewHolder(view)
    }

    override fun onBindViewHolder(holder: InformationListViewHolder, position: Int) {
        holder.bind(weather[position])
    }

    override fun getItemCount(): Int {
        return weather.size
    }
}