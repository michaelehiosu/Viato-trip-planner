package com.michael.viatoapp.model.request.flights

data class AllFlightsSearch(
    var fromEntityId : String,
    var toEntityId : String,
    var departDate : String,
    var returnDate : String,
    var currency : String,
    var dummy : Boolean,
)
