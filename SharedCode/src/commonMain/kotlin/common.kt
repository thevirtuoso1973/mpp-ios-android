package com.jetbrains.handson.mpp.mobile

data class Station(val fullName: String, val shortName: String)

val stations = arrayOf(
    Station("King's Cross", "KGX"),
    Station("Edinburgh", "EDB"),
    Station("Manchester", "MAN")
)

expect fun platformName(): String

fun getShortStationName(index: Int): String {
    return stations[index].shortName
}

fun getFullStations(): Array<String> {
    return stations.map { it.fullName }.toTypedArray()
}

fun getFullUrl(station_from: String, station_to: String): String {
    return "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/${station_from}/${station_to}"
}

