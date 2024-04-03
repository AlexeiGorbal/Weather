package com.example.weather.weather.ui.list.title

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.ItemTitleBinding

class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemTitleBinding = ItemTitleBinding.bind(view)

    fun bind(item: TitleItem) {
        binding.title.text = item.title
    }

    companion object {

        fun from(parent: ViewGroup): TitleViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_title,
                parent, false
            )
            return TitleViewHolder(view)
        }
    }
}