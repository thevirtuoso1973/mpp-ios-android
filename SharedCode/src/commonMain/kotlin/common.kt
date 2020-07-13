package com.jetbrains.handson.mpp.mobile

const val baseURL = "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/%s_from/%s_to"

expect fun platformName(): String

fun getStations(): Array<String> {
    return arrayOf("KGX", "EDB", "MAN")
}

fun getFullUrl(station_from: String, station_to: String): String {
    return baseURL.replace("%s_from", station_from).replace("%s_to", station_to)
}
