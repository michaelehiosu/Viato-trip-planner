package com.michael.viatoapp.model.response.flights


data class FlightsResponse(
    var data : FlightData,
    var status : Boolean,
    var message : String
)

data class FlightData (
    val itineraries : ArrayList<FlightItinerary>
)

data class FlightItinerary (
    val id : String,
    val price : FlightPrice,
    val legs : ArrayList<Leg>,
    val isSelfTransfer : Boolean,

)

data class FlightPrice (
    val rawPrice : Int,
    val formattedPrice : String
)

data class Leg (
    val origin : Origin,
    val destination : Destination,
    val durationInMinutes : Int,
    val stopCount : Int,
    val departure : String,
    val arrival : String,
    val carrier : Carrier,
)

data class Origin (
    val id : String,
    val name : String,
    val entityId : String,
    val city : String,
    val country : String,
    val displayCode : String
)

data class Destination (
    val id : String,
    val name : String,
    val entityId : String,
    val city : String,
    val country : String,
    val displayCode : String
)

data class Carrier (
    val marketing : Carriers,
    val operating : Carriers
)

data class Carriers (
    val id : String,
    val logoUrl : String,
    val name : String
)
