package com.denverhogan.themeparks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denverhogan.themeparks.model.ThemePark

class ThemeParksAdapter(
    private val dataSet: List<ThemePark>,
    private val onItemClick: () -> Unit
) : RecyclerView.Adapter<ThemeParksAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, onItemClick: () -> Unit) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.theme_park_name)
        val location: TextView = view.findViewById(R.id.theme_park_location)

        init {
            view.setOnClickListener {
                onItemClick()
            }
        }

        fun bind(themePark: ThemePark) {
            name.text = themePark.name
            location.text = themePark.location
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.theme_park_list_item, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(themePark = dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}