package com.michael.viatoapp.model.response.flights

data class CountriesResponse (
        var data : Data,
        var status : Boolean,
        var message : String
)

data class Data (
    var everywhereDestination : EverywhereDestination
)

data class EverywhereDestination (
   var results : ArrayList<Result>
)

data class Result (
    var entityId : String,
    var skyId : String,
    var content : Content
)

data class Content (
    var location : Location,
    var flightQuotes : FlightQuotes,
    var image : Image
)

data class Location (
    var name : String,
    val continent: String?
)

data class FlightQuotes (
    var cheapest : Cheapest
)

data class Image (
    var url : String
)

data class Cheapest (
    val price : String,
    val rawPrice : String,
    val isDirect : Boolean
)