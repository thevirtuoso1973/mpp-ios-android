package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

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

@OptIn(UnstableDefault::class)
fun deserialiseJson(jsonString: String): ApiResult {
    val jsonConfig = JsonConfiguration(ignoreUnknownKeys = true)
    val json = Json(jsonConfig)
    return json.parse(ApiResult.serializer(), jsonString)
}

fun getEpochFromUTC(s: String): Long {
    val formatter = DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
    val date = formatter.parse(s)
    return date.utc.unixMillisLong
}
fun getPrice(tickets: List<ApiResult.Journey.Ticket>): Int {
    return tickets.map { it.priceInPennies }.sum()
}