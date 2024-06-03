package com.michael.viatoapp.model.request.flights

data class FlightCitiesSearch(
    var fromEntityId : String?,
    var skyId : String?,
    var departDate : String?,
    var returnDate : String?,
    var currency : String?,
    var dummy : Boolean?
)
