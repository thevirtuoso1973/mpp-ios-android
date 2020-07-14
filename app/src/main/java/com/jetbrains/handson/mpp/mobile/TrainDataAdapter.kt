package com.jetbrains.handson.mpp.mobile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_row.view.*
import java.util.*

class TrainDataAdapter(private val dataSet: List<TrainTimes.Journey>) :
    RecyclerView.Adapter<TrainDataAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    class MyViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
        fun addJourney(j: TrainTimes.Journey) {
            view.numberChanges.text = j.numberChanges.toString()
            view.price.text = j.price.toString()
            view.departureTime.text = Date(j.departureTime*1000).toString()
            view.arrivalTime.text = Date(j.arrivalTime*1000).toString()
        }
        @SuppressLint("SetTextI18n")
        fun addTitle() {
            view.numberChanges.text = "Changes"
            view.price.text = "Price"
            view.departureTime.text = "Departure Time"
            view.arrivalTime.text = "Arrival Time"
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        // create a new view
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(layout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (position == 0) {
            holder.addTitle()
        } else {
            holder.addJourney(dataSet[position-1])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size + 1 // extra row for headers
}
