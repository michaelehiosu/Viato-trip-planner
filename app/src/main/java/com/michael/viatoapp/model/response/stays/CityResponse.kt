package com.michael.viatoapp.model.response.stays

data class CityResponse(
    val data : ArrayList<FoundCity>,
    val status : Boolean,
    val message : String
)

data class FoundCity(
    val location : String,
    val entityName : String,
    val entityId : String,
    val entityType : String,
)