package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setStations(stations: Array<String>)
        fun getStationFrom(): String
        fun getStationTo(): String
        fun openLink(link: String)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun onSubmitPressed(view: View)
    }
}
