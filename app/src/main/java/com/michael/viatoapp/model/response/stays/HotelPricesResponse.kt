package com.michael.viatoapp.model.response.stays

import com.michael.viatoapp.model.data.stays.HotelPrice

data class HotelPricesResponse(
    var hotelPrices : ArrayList<HotelPrice>,
    var status : Boolean,
    var message : String
)