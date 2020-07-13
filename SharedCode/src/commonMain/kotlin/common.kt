package com.jetbrains.handson.mpp.mobile

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
    return "https://www.lner.co.uk/travel-information/travelling-now/live-train-times/depart/${station_from}/${station_to}"
}

//https://mobile-api-dev.lner.co.uk/v1/fares?
//      originStation=FRO&
//      destinationStation=KGX&
//      noChanges=false&
//      numberOfAdults=2&
//      numberOfChildren=0&
//      journeyType=single&
//      inboundDateTime=2020-07-01T12:16:27.371&
//      inboundIsArriveBy=false&
//      outboundDateTime=2020-07-14T19%3A30%3A00.000%2B01%3A00&
//      outboundIsArriveBy=false

class ApiUrlBuilder(val originStation: String, val destinationStation: String) {
    var noChanges = false
    var numberOfAdults = 1
    var numberOfChildren = 0
    var journeyType = "single"
    var inboundDateTime = "2020-07-01T12:16:27.371"
    var inboundIsArriveBy = false
    var outboundDateTime = "2020-07-01T12:16:27.371"
    var outboundIsArriveBy = false

    fun withNoChanges(changesDisallowed: Boolean): ApiUrlBuilder {
        noChanges = true
        return this
    }
    fun withAdults(numAdults: Int): ApiUrlBuilder {
        numberOfAdults = numAdults
        return this
    }
    fun withChildren(numChildren: Int): ApiUrlBuilder {
        numberOfChildren = numChildren
        return this
    }
    fun withJourneyType(type: String): ApiUrlBuilder {
        when (type) {
            "single", "return" -> journeyType = type
            else -> throw RuntimeException("Invalid journey type $type")
        }
        return this
    }

    fun build(): String {
        return "https://mobile-api-dev.lner.co.uk/v1/fares?" +
          "originStation=$originStation&" +
          "destinationStation=$destinationStation&" +
          "noChanges=$noChanges&" +
          "numberOfAdults=$numberOfAdults&" +
          "numberOfChildren=$numberOfChildren&" +
          "journeyType=$journeyType&" +
          "inboundDateTime=$inboundDateTime&" +
          "inboundIsArriveBy=$inboundIsArriveBy&" +
          "outboundDateTime=$outboundDateTime&" +
          "outboundIsArriveBy=$outboundIsArriveBy"
    }
}
