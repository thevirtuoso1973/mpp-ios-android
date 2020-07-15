package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

data class Station(val fullName: String, val shortName: String)

data class AppSubmitResult(val stationFromIndex: Int, val stationToIndex: Int)

val stations = arrayOf(
    Station("King's Cross", "KGX"),
    Station("Edinburgh", "EDB"),
    Station("Manchester", "MAN")
)

expect fun platformName(): String

fun getShortStationName(index: Int): String {
    return stations[index].shortName
}

@OptIn(UnstableDefault::class)
suspend fun getAPIResponse(apiUrl: String): ApiResult? {
    val client = HttpClient {
        install(JsonFeature) {
            val jsonConfig = JsonConfiguration(ignoreUnknownKeys = true)
            serializer = KotlinxSerializer(Json(jsonConfig))
        }
    }
    return try {
        client.get(apiUrl)
    } catch (ex: MissingFieldException) {
        println("Failed to serialise!")
        println("Msg: ${ex.message}, cause: ${ex.cause}")
        println(client.get<String>(apiUrl))
        null
    }
}

fun getEpochMillisFromUTC(s: String): Long {
    val formatter = DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
    val date = formatter.parse(s)
    return date.utc.unixMillisLong
}
fun getPrice(tickets: List<ApiResult.Journey.Ticket>): Int {
    return tickets.map { it.priceInPennies }.sum()
}

fun ApiResult.toTrainTimes(): TrainTimes{
    val journeys = mutableListOf<TrainTimes.Journey>()
    this.outboundJourneys.forEach { journey ->
        journeys.plusAssign(TrainTimes.Journey(
            getPrice(journey.tickets),
            getEpochMillisFromUTC(journey.departureTime),
            getEpochMillisFromUTC(journey.arrivalTime),
            journey.legs.size-1
        ))
    }
    return TrainTimes(
        this.outboundJourneys.firstOrNull()?.originStation?.displayName ?: "NONE",
        this.outboundJourneys.lastOrNull()?.originStation?.displayName ?: "NONE",
        journeys.toTypedArray())
}