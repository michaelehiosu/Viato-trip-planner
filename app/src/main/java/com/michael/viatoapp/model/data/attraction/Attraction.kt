package com.michael.viatoapp.model.data.attraction

import java.io.Serializable

data class Attraction(
    val name: String?,
    val locationId: String?,
    val numReviews: String?,
    val locationString: String?,
    val image: String?,
    val website: String?,
    val address: String?,
    val tripAdvisorLink: String?,
    val subCategory: String?,
    val longitude: String?,
    val latitude: String?
) : Serializable
