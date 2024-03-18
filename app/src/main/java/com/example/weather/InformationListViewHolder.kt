package com.example.weather

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weather.databinding.DayTempItemBinding
import com.example.weather.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InformationListViewHolder(view: View) : ViewHolder(view) {

  private  val binding: DayTempItemBinding = DayTempItemBinding.bind(view)

    fun bind(information:Information) {
        binding.dayTime.text=information.time
        binding.imageWeather.setImageResource(information.image)
        binding.temperature.text=information.temperature
    }

}