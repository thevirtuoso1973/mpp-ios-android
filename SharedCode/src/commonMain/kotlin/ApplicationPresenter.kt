package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var viewRef: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.viewRef = view
        view.setStations(stations)
    }

    override fun onSubmitPressed(result: AppSubmitResult) {
        val view = this.viewRef!!
        val stationFrom = getShortStationName(result.stationFromIndex)
        val stationTo = getShortStationName(result.stationToIndex)
        if (stationFrom == stationTo) {
            view.createAlert("No ticket needed!")
            return
        }
        //view.openLink(getFullUrl(stationFrom, stationTo))
        val currentDateTime = DateTime.fromUnix(view.getCurrentUnixTime()).plus(TimeSpan(10 * 60 * 1000.0))
        val urlString = ApiUrlBuilder(stationFrom, stationTo)
            .withOutboundDate(currentDateTime)
            .build()
        println("API URL: $urlString")
        view.setLoading(true)
        launch {
            val apiResult = getAPIResponse(urlString)
            view.setLoading(false)
            if (apiResult != null) {
                view.displayTrainTimes(apiResult.toTrainTimes())
            } else {
                view.createAlert("Request failed, please try again later or with different stations.")
            }
        }
    }
}
