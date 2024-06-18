package com.michael.viatoapp.model.data.stays

import java.io.Serializable

data class Hotel(
    var name: String?,
    var latitude: Double,
    var longitude: Double,
    var images: String?,
    var reviewScore: Double?,
    var priceRaw: Int?,
    var relevantPoi : String?,
    var scoreDesc : String?,
    var hotelId: String?
): Serializable
