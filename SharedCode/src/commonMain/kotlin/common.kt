package com.jetbrains.handson.mpp.mobile

const val baseURL = "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/%s_from/%s_to"

val stations = arrayOf("KGX", "EDB", "MAN")
val stations_full = arrayOf("King's Cross", "Edinburgh", "Manchester")

expect fun platformName(): String

fun getShortStationName(index: Int): String {
    return stations[index]
}

fun getFullStations(): Array<String> {
    return stations_full
}

fun getFullUrl(station_from: String, station_to: String): String {
    return baseURL.replace("%s_from", station_from).replace("%s_to", station_to)
}
