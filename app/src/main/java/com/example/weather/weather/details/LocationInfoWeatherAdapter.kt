package com.example.weather.weather.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weather.location.LocationInfo

class LocationInfoWeatherAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private var list = listOf<LocationInfo>()

    fun addList(list: List<LocationInfo>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return LocationWeatherFragment.newInstance(list[position].id)
    }
}