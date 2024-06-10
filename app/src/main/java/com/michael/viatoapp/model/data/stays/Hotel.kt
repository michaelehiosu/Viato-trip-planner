package com.michael.viatoapp.model.data.stays

data class Hotel(
    var name: String?,
    var latitude: Double?,
    var longitude: Double?,
    var images: String?,
    var reviewScore: Double?,
    var priceRaw: Int?,
    var hotelId: String?
)
