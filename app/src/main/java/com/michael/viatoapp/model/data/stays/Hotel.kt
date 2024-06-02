package com.michael.viatoapp.model.data.stays

data class Hotel(
    var name : String,
    var latitude : Double,
    var longitude : Double,
    var images : ArrayList<Image>,
    var reviewScore : Double,
    var priceRaw : Int,
    var hotelId : String
)

data class Image (
    var url : String
)
