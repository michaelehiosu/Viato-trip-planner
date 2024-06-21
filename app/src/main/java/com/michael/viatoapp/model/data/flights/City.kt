package com.michael.viatoapp.model.data.flights

import java.io.Serializable

data class City(
    var entityId: String,
    val skyId: String,
    val name: String,
    val imageUrl: String,
) : Serializable
