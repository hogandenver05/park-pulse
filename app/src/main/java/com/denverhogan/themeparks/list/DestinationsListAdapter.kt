package com.denverhogan.themeparks.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denverhogan.themeparks.databinding.DestinationListItemBinding
import com.denverhogan.themeparks.model.DestinationListItem

class DestinationsListAdapter(private val onItemClick : (DestinationListItem) -> Unit) :
    RecyclerView.Adapter<DestinationsListAdapter.ViewHolder>() {

    private val dataSet: MutableList<DestinationListItem> = mutableListOf()

    inner class ViewHolder(
        private val binding: DestinationListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(destination: DestinationListItem) {
            binding.destinationName.text = destination.name
            binding.destinationLocation.text = destination.location
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DestinationListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(destination = dataSet[position])
        holder.itemView.setOnClickListener {
            onItemClick(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<DestinationListItem>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }
}