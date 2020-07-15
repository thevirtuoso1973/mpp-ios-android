package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setStations(stations: Array<ApiResult.Journey.Station>)

        fun openLink(link: String)
        fun createAlert(msg: String)
        fun getCurrentUnixTime(): Long

        fun displayTrainTimes(trainTimes: TrainTimes)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun onSubmitPressed(result: AppSubmitResult)
    }
}
