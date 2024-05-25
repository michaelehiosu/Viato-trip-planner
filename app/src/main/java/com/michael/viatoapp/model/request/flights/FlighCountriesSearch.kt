package com.michael.viatoapp.model.request.flights

data class FlighCountriesSearch(
    var fromEntityId : String,
    var departDate : String,
    var returnDate : String,
    var currency : String,
    var dummy : Boolean
)