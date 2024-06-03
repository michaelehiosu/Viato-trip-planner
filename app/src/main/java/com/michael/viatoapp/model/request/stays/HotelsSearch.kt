package com.michael.viatoapp.model.request.stays;

data class HotelsSearch (
    var entityId : String,
    var checkIn : String,
    var checkOut : String,
    var maxPrice : Int,
    var dummy : Boolean
)
