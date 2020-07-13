package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setStations(stations: Array<String>)
        fun getStationFrom(): Int
        fun getStationTo(): Int
        fun openLink(link: String)
        fun createAlert(msg: String)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun onSubmitPressed(view: View)
    }
}
