package com.jetbrains.handson.mpp.mobile

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_row.view.*
import java.util.*
import java.util.concurrent.TimeUnit

class TrainDataAdapter(private val dataSet: List<TrainTimes.Journey>) :
    RecyclerView.Adapter<TrainDataAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var journey: TrainTimes.Journey? = null

        @SuppressLint("SetTextI18n")
        fun addJourney(j: TrainTimes.Journey) {
            journey = j

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
        if (position == 0) { // the first position will be the heading
            holder.addTitle()
        } else {
            holder.addJourney(dataSet[position-1])
            holder.view.setOnClickListener { view ->
                val intent = Intent(view.context, JourneyDetailActivity::class.java)
                intent.putExtra("status", holder.journey?.status)
                intent.putExtra("primaryOperator", holder.journey?.trainOperator)
                intent.putExtra("stationChangeNames", holder.journey?.changes?.map {
                    it.name
                }?.toTypedArray())
                val diff = holder.journey?.arrivalTime!! - holder.journey?.departureTime!!
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                val min = TimeUnit.MILLISECONDS.toMinutes(diff) - hours*60
                intent.putExtra("duration", if (hours == 0L)
                    "$min minutes"
                else
                    "$hours hours, $min minutes")
                startActivity(view.context, intent, null)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size + 1 // extra row for headers
}
