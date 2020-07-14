package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_row.view.*

class TrainDataAdapter(private val dataSet: List<TrainTimes.Journey>) :
    RecyclerView.Adapter<TrainDataAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    class MyViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
        fun addData(s: String) {
            view.list_item.text = s
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
        val journey = dataSet[position]
        holder.addData(journey.toString())
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
