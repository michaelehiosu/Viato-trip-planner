package com.michael.viatoapp.model.data.flights

import java.io.Serializable

data class Itinerary(
    var token: String?,
    var id: String?,
    val rawPrice: Double?,
    val formattedPrice: String?,
    val originId: String?,
    val originName: String?,
    val destinationId: String?,
    val destinationName: String?,
    val marketingCarrierName: String?,
    val marketingCarrierLogo: String?,
    val operatingCarrier: String?,
    val operatingCarrierLogo: String?,
    val durationOutbound: Int?,
    val durationInbound: Int?,
    val stopCountInbound: Int?,
    val stopCountOutbound: Int?,
    val outboundDepartureTime: String?,
    val outboundArrivalTime: String?,
    val inboundDepartureTime: String?,
    val inboundArrivalTime: String?
) : Serializable