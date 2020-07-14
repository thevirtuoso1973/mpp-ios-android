package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {
    private val presenter = ApplicationPresenter()

    @Suppress("UNUSED_PARAMETER")
    fun notifyPresenterSubmit(view: View) {
        presenter.onSubmitPressed(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.onViewTaken(this)
    }

    override fun setStations(stations: Array<Station>) {
        val arrayAdapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item,
            stations.map { it.fullName }
        )
        val spinnerFrom: Spinner = findViewById(R.id.spinner_from)
        val spinnerTo: Spinner = findViewById(R.id.spinner_to)
        spinnerFrom.adapter = arrayAdapter
        spinnerTo.adapter = arrayAdapter
    }

    override fun createAlert(msg: String) {
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun getCurrentUnixTime(): Long {
        return Date().time
    }

    override fun getStationFrom(): Int {
        val spinner: Spinner = findViewById(R.id.spinner_from)
        return spinner.selectedItemPosition
    }

    override fun getStationTo(): Int {
        val spinner: Spinner = findViewById(R.id.spinner_to)
        return spinner.selectedItemPosition
    }

    override fun openLink(link: String) {
        startActivity(Intent.parseUri(link, 0))
    }
}
