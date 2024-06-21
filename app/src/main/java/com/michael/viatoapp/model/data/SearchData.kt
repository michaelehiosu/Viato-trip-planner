package com.michael.viatoapp.model.data

import java.io.Serializable

data class SearchData(
    val budget: String,
    val isFlightPressed: Boolean,
    var isHotelPressed: Boolean,
) : Serializable
