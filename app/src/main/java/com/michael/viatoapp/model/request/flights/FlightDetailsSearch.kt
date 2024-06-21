package com.michael.viatoapp.model.request.flights

data class FlightDetailsSearch(
    var token: String?,
    var itineraryId: String?,
    var currency: String?,
    var dummy: Boolean
)
