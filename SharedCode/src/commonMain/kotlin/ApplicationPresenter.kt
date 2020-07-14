package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.minutes
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setStations(stations)
    }

    override fun onSubmitPressed(view: ApplicationContract.View) {
        val stationFrom = getShortStationName(view.getStationFrom())
        val stationTo = getShortStationName(view.getStationTo())
        if (stationFrom == stationTo) {
            view.createAlert("No ticket needed!")
            return
        }
        //view.openLink(getFullUrl(stationFrom, stationTo))
        val currentDateTime = DateTime.fromUnix(view.getCurrentUnixTime()).plus(TimeSpan(10 * 60 * 1000.0))
        val urlString = ApiUrlBuilder(stationFrom, stationTo)
            .withOutboundDate(currentDateTime)
            .build()
        //view.createAlert("Fetching API from $urlString")
        val apiJob = async { getAPIResponseString(urlString) }
        launch {
            //println(deserialiseJson(apiJob.await()))
            val apiResult = deserialiseJson(apiJob.await())
            println("Outbound journey times: ")
            apiResult.outboundJourneys.forEach {
                println(it.departureTime)
            }
        }
    }
}
