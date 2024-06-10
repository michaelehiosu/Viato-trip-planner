package com.michael.viatoapp.model.response.stays

import com.michael.viatoapp.model.data.stays.HotelPrice

data class HotelPricesResponse(
    var data : HotelPrices,
    var status : Boolean,
    var message : String
)

data class HotelPrices(
    var rates : ArrayList<Rates>,
)

data class Rates(
    var partnerName : String,
    var partnerLogo : String,
    var roomType : String,
    var roomPolicies : String,
    var deepLink : String,
    var rawPrice : Int,
)