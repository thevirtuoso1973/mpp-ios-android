package com.jetbrains.handson.mpp.mobile

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get

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

fun getFullUrl(station_from: String, station_to: String): String {
    return "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/${station_from}/${station_to}"
}

suspend fun getAPIResponseString(apiUrl: String): String {
    val client = HttpClient { install(JsonFeature) {
        serializer = KotlinxSerializer()
    }}
    return client.get(apiUrl) as String
}
