package com.michael.viatoapp.model.request.stays;

data class HotelPricesSearch (
    var hotelId : String?,
    var checkIn : String?,
    var checkOut : String?,
    var dummy : Boolean?
)
