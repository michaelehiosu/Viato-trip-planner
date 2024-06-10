package com.michael.viatoapp.model.response.flights

import com.michael.viatoapp.model.data.flights.Airport

data class AirportResponse(
    var data : ArrayList<Airport>,
    var status : Boolean,
    var message : String
)
