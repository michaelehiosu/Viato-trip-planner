package com.michael.viatoapp.model.response.stays

import com.michael.viatoapp.model.data.stays.Hotel

data class HotelsResponse(
    var data : Hotels,
    var status : Boolean,
    var message : String
)

data class Hotels (
    var priceType : String,
    var hotelsCards : List<Hotel>
)

data class Image (
    var url : String
)