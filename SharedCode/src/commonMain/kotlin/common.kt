package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.*
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

fun toHumanReadableDate(epochMillis: Long, now: Long): String {
    val date = DateTime.Companion.fromUnix(epochMillis / 1000)
    val hourFormatter = DateFormat("HH:mm")
    val dateFormatter = DateFormat("HH:mm dd/MM")
    val now = DateTime.Companion.fromUnix(now / 1000)
    val diff = date - now

    // If now.day == date.day || diff < 12h
    if (now.startOfDay == date.startOfDay || diff.hours < 12) {
        // Display as HH:mm (in xx h/m)
        val diffStr = if (diff.hours < 1) "${diff.minutes.toInt()} min" else "${diff.hours.toInt()} hr"
        return "${hourFormatter.format(date)} (in $diffStr)"
    }
    return dateFormatter.format(date)
}

fun formatPrice(price: Int): String {
    return "£${price/100}.${"${price % 100}".padStart(2, '0')}"
}

fun ApiResult.toTrainTimes(now: Long): TrainTimes{
    val journeys = mutableListOf<TrainTimes.Journey>()
    val priceStr = "£%d.%02d"
    this.outboundJourneys.forEach { journey ->
        journeys.plusAssign(TrainTimes.Journey(
            getPrice(journey.tickets),
            formatPrice(getPrice(journey.tickets)),
            getEpochMillisFromUTC(journey.departureTime),
            getEpochMillisFromUTC(journey.arrivalTime),
            toHumanReadableDate(getEpochMillisFromUTC(journey.departureTime), now),
            toHumanReadableDate(getEpochMillisFromUTC(journey.arrivalTime), now),
            journey.status,
            journey.primaryTrainOperator.name,
            journey.legs.map {
                leg -> leg.origin
            }.drop(1).toTypedArray()
        ))
    }
    return TrainTimes(
        this.outboundJourneys.firstOrNull()?.originStation?.name ?: "NONE",
        this.outboundJourneys.lastOrNull()?.destinationStation?.name ?: "NONE",
        journeys.toTypedArray())
}