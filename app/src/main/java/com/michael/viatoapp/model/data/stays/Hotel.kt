package com.michael.viatoapp.model.data.stays

import com.michael.viatoapp.model.response.stays.Image

data class Hotel(
    var name: String,
    var latitude: Double,
    var longitude: Double,
    var images: ArrayList<Image>,
    var reviewScore: Double,
    var priceRaw: Int,
    var hotelId: String
)
