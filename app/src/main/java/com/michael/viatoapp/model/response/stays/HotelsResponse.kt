package com.michael.viatoapp.model.response.stays

data class HotelsResponse(
    var data : Data,
    var status : Boolean,
    var message : String
)

data class Data(
    val results: Result
)

data class Result(
    val hotelCards : ArrayList<HotelCard>
)

data class HotelCard(
    val name : String,
    val coordinates : Coordinates,
    val images : ArrayList<String>,
    val reviewSummary : ReviewScore,
    val lowestPrice : RawPrice,
    val hotelId : String

)

data class ReviewScore(
    val score : Double,
)

data class RawPrice(
    val rawPrice : Int
)

data class Coordinates(
    val latitude : Double,
    val longitude : Double,
)
