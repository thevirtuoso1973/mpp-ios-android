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

        private fun getTimeString(hour: Int, min: Int): String {
            return "%02d:%02d".format(hour, min)
        }

        @SuppressLint("SetTextI18n")
        fun addJourney(j: TrainTimes.Journey) {
            /*
            Not as clean as it could be due to the API level 23.
             */
            journey = j

            view.changes.text = j.numberChanges.toString()
            view.price.text = "£${j.price/100}.%02d".format(j.price % 100)

            val curr = System.currentTimeMillis()
            val departCalendar = Calendar.getInstance()
            departCalendar.time = Date(j.departureTime)
            val arriveCalendar = Calendar.getInstance()
            arriveCalendar.time = Date(j.arrivalTime)

            val diffDepart = java.util.concurrent.TimeUnit
                .MILLISECONDS
                .toHours(j.departureTime-curr)
            val diffArrive = java.util.concurrent.TimeUnit
                .MILLISECONDS
                .toHours(j.arrivalTime-curr)

            view.departureTime.text = "${getTimeString(
                departCalendar.get(Calendar.HOUR_OF_DAY),
                departCalendar.get(Calendar.MINUTE)
            )} (in ${if (diffDepart == 0L) "less than an hour" else "$diffDepart hours"})"
            view.arrivalTime.text = "${getTimeString(
                arriveCalendar.get(Calendar.HOUR_OF_DAY),
                arriveCalendar.get(Calendar.MINUTE)
            )} (in ${if (diffArrive == 0L) "less than an hour" else "$diffArrive hours"})"
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
