package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

data class AppSubmitResult(val stationFromIndex: Int, val stationToIndex: Int)

var stations: Array<StationApiResult.Station> = arrayOf(StationApiResult.Station(
    "King's Cross", "KGX")
)

expect fun platformName(): String

fun getShortStationName(index: Int): String {
    return stations[index].crs!!
}

@OptIn(UnstableDefault::class)
suspend inline fun <reified T> getAPIResponse(apiUrl: String): T? {
    val client = HttpClient {
        install(JsonFeature) {
            val jsonConfig = JsonConfiguration(ignoreUnknownKeys = true)
            serializer = KotlinxSerializer(Json(jsonConfig))
        }
    }
    return try {
        client.get<T>(apiUrl)
    } catch (ex: MissingFieldException) {
        println("Failed to serialise!")
        println("Msg: ${ex.message}, cause: ${ex.cause}")
        println(client.get<String>(apiUrl))
        null
    } catch (ex: Throwable) {
        println("Other error")
        println("Msg: ${ex.message}, cause: ${ex.cause}")
        // Can't rerun get() because it might have been the cause of the throw
        //println(client.get<String>(apiUrl))
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
            journey.legs.size-1,
            journey.status,
            journey.primaryTrainOperator.name,
            journey.legs.map {
                leg -> leg.origin
            }.drop(0).toTypedArray()
        ))
    }
    return TrainTimes(
        this.outboundJourneys.firstOrNull()?.originStation?.name ?: "NONE",
        this.outboundJourneys.lastOrNull()?.destinationStation?.name ?: "NONE",
        journeys.toTypedArray())
}