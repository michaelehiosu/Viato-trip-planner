package com.michael.viatoapp.model.data.flights

import java.io.Serializable

data class Country(
    var entityId : String?,
    val skyId : String?,
    val name : String?,
    val cheapestPrice : String?,
    val imageUrl : String?,
): Serializable
