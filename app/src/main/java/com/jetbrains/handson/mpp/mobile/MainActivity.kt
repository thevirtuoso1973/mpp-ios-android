package com.jetbrains.handson.mpp.mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {
    private val presenter = ApplicationPresenter()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var data = mutableListOf<TrainTimes.Journey>()

    var origin: String? = null
    var dest: String? = null

    var stationsRetrieved = false

    @Suppress("UNUSED_PARAMETER")
    fun notifyPresenterSubmit(view: View) {
        if (stationsRetrieved) {
            presenter.onSubmitPressed(AppSubmitResult(getStationFrom(), getStationTo()))
        } else {
            presenter.onViewTaken(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = TrainDataAdapter(data)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        val divider = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)

        val button = findViewById<Button>(R.id.button_submit)
        if (stationsRetrieved) {
            button.text = getString(R.string.submit)
        } else {
            button.text = "Get stations"
        }
        presenter.onViewTaken(this)
    }

    override fun setStations(stations: Array<StationApiResult.Station>) {
        val button = findViewById<Button>(R.id.button_submit)
        button.text = getString(R.string.submit)
        stationsRetrieved = true

        val arrayAdapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item,
            stations.map { it.name }
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

    override fun displayTrainTimes(trainTimes: TrainTimes) {
        origin = trainTimes.origin
        dest = trainTimes.destination

        data.clear()
        data.addAll(trainTimes.journeys)
        viewAdapter.notifyDataSetChanged()
    }

    override fun getCurrentUnixTime(): Long {
        return Date().time
    }

    override fun getSecondsFromUtc(): Long {
        val tz = TimeZone.getDefault()
        val cal = GregorianCalendar.getInstance(tz)
        val offsetMillis = tz.getOffset(cal.timeInMillis)
        return (offsetMillis / 1000).toLong()
    }

    private fun getStationFrom(): Int {
        val spinner: Spinner = findViewById(R.id.spinner_from)
        return spinner.selectedItemPosition
    }

    private fun getStationTo(): Int {
        val spinner: Spinner = findViewById(R.id.spinner_to)
        return spinner.selectedItemPosition
    }

    override fun openLink(link: String) {
        startActivity(Intent.parseUri(link, 0))
    }

    override fun setLoading(loading: Boolean) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        button_submit.isEnabled = !loading
    }
}
