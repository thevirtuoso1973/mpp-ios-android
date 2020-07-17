package com.jetbrains.handson.mpp.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class JourneyDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup)

        val origin = findViewById<TextView>(R.id.originView)
        origin.text = "From: ${intent.getStringExtra("origin")}"
        val destination = findViewById<TextView>(R.id.destinationView)
        destination.text = "To: ${intent.getStringExtra("destination")}"
        val duration = findViewById<TextView>(R.id.duration)
        duration.text = "Duration: ${intent.getStringExtra("duration")}"
        val statusText = findViewById<TextView>(R.id.status)
        statusText.text = "Status: ${intent.getStringExtra("status")}"
        val operator = findViewById<TextView>(R.id.primaryOperator)
        operator.text = "Train Operator: ${intent.getStringExtra("primaryOperator")}"

        val stations = intent.getStringArrayExtra("stationChangeNames")
        val listView = findViewById<ListView>(R.id.changes_list)
        if (stations != null) {
            listView.adapter = ArrayAdapter<String>(this, R.layout.simple_text, stations)
        }

        println(intent.getStringExtra("origin"))
        println(intent.getStringExtra("destination"))

        val done = findViewById<Button>(R.id.done_button)
        done.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }
}