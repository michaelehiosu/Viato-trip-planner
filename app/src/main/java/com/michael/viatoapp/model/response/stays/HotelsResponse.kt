package com.michael.viatoapp.model.response.stays

data class HotelsResponse(
    var data : Hotels,
    var status : Boolean,
    var message : String
)

data class Hotels(
    val hotelsCards : ArrayList<HotelCard>
)

data class HotelCard(
    val name : String,
    val coordinates : Coordinates,
    val images : ArrayList<Image>,
    val reviewSummary : ReviewScore,
    val lowestPrice : RawPrice,
    val hotelId : String

)

data class ReviewScore(
    val reviewScore : Double,
)

data class RawPrice(
    val rawPrice : Int
)

data class Coordinates(
    val latitude : Double,
    val longitude : Double,
)

data class Image (
    val url : String
)