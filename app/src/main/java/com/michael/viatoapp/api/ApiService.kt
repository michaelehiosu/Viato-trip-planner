package com.michael.viatoapp.api

import android.icu.text.StringSearch
import com.michael.viatoapp.model.request.stays.CitySearch
import com.michael.viatoapp.model.request.stays.HotelPricesSearch
import com.michael.viatoapp.model.request.stays.HotelsSearch
import com.michael.viatoapp.model.response.stays.CityResponse
import com.michael.viatoapp.model.response.stays.HotelPricesResponse
import com.michael.viatoapp.model.response.stays.HotelsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("stays/city")
    fun getCity(@Body() citySearch: CitySearch): Call<CityResponse>

    @POST("stays/hotels")
    fun getHotels(@Body() hotelsSearch: HotelsSearch): Call<HotelsResponse>

    @POST("stays/hotels/prices")
    fun getHotelPrices(@Body() hotelPricesSearch: HotelPricesSearch): Call<HotelPricesResponse>
}