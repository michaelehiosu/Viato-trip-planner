package com.michael.viatoapp.model.response.flights

data class FlightDetailsResponse(
    var data: FlightDetail,
    var status: Boolean,
    var message: String
)

data class FlightDetail(
    var itinerary: FlightDetailItinerary
)

data class FlightDetailItinerary(
    var id: String,
    var legs: ArrayList<Leg>,
    var pricingOptions: ArrayList<PricingOption>
)

data class PricingOption(
    var agents: ArrayList<Agent>
)

data class Agent(
    var name: String,
    var url: String,
    var price: String
)



