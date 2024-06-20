package com.michael.viatoapp.model.response.stays

data class HotelPricesResponse(
    var data: DataPrices,
    var status: Boolean,
    var message: String
)

data class DataPrices(
    val metaInfo: MetaInfo
)

data class MetaInfo(
    val rates: ArrayList<Rate>
)

data class Rate(
    var partnerName: String,
    var partnerLogo: String,
    var roomType: String,
    var roomPolicies: String,
    var deepLink: String,
    var rawPrice: Int,
)