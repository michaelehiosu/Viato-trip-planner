package com.michael.viatoapp.model.request.attractions

data class AttractionsSearch (
    val longitude : String,
    val latitude : String,
    val distance : String,
    val currency : String,
    val dummy : Boolean
)