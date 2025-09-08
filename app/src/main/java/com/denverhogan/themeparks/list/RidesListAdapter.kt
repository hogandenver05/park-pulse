package com.denverhogan.themeparks.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denverhogan.themeparks.databinding.RideListItemBinding
import com.denverhogan.themeparks.model.Ride

class RidesListAdapter(private val onItemClick : (Ride) -> Unit) :
    RecyclerView.Adapter<RidesListAdapter.ViewHolder>() {

    private val dataSet: MutableList<Ride> = mutableListOf()

    inner class ViewHolder(
        private val binding: RideListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ride: Ride) {
            binding.rideName.text = ride.name
            if (ride.isOpen) binding.rideWaitTime.text = ride.waitTime.toString()
            else binding.rideWaitTime.text = "Closed"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RideListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ride = dataSet[position])
        holder.itemView.setOnClickListener {
            onItemClick(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Ride>) {
        dataSet.clear()
        dataSet.addAll(data)
        notifyDataSetChanged()
    }
}