package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.Serializable

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
        noChanges = changesDisallowed
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

@Serializable
data class ApiResult (
    //val numberOfAdults = 0
    //val numberOfChildren = 0
    val outboundJourneys: List<Journey>
) {
    @Serializable
    data class Journey (
        //val journeyOptionToken = ""
        val journeyId: String,
        val originStation: Station,
        val destinationStation: Station,
        val departureTime: String,
        val arrivalTime: String,
        val status: String,
        val primaryTrainOperator: TrainOperator,
        val legs: List<Leg>
    ) {
        @Serializable
        data class Station (
            val displayName: String,
            val nlc: String,
            val crs: String
        )
        @Serializable
        data class TrainOperator (
            val code: String,
            val name: String
        )

        @Serializable
        data class Leg(
            val legId: String,
            val rsid: String,
            val origin: Station,
            val destination: Station,
            val type: String,
            val mode: String, // Enum?
            val durationInMinutes: Int,
            val departureDateTime: String,
            val arrivalDateTime: String,
            val status: String,
            val trainOperator: TrainOperator,
            // val trainFacilities: List<Facility>
            val additionalFacilitiesInformation: String
        )
    }
}
