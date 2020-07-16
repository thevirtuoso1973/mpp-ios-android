package com.jetbrains.handson.mpp.mobile

import android.annotation.SuppressLint
import android.icu.util.TimeUnit
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_row.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class TrainDataAdapter(private val dataSet: List<TrainTimes.Journey>) :
    RecyclerView.Adapter<TrainDataAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    class MyViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun addJourney(j: TrainTimes.Journey) {
            view.changes.text = j.changes.size.toString()
            view.price.text = j.priceFormatted

            view.departureTime.text = j.departureTimeFormatted
            view.arrivalTime.text = j.arrivalTimeFormatted
        }

        @SuppressLint("SetTextI18n")
        fun addTitle() {
            view.changes.text = "Changes"
            view.price.text = "Price"
            view.departureTime.text = "Departure"
            view.arrivalTime.text = "Arrival"
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
