package com.michael.viatoapp.api

import com.michael.viatoapp.model.request.flights.AllFlightsSearch
import com.michael.viatoapp.model.request.flights.FlighCountriesSearch
import com.michael.viatoapp.model.request.flights.FlightCitiesSearch
import com.michael.viatoapp.model.request.flights.FlightDetailsSearch
import com.michael.viatoapp.model.request.flights.FlightsSessionComplete
import com.michael.viatoapp.model.response.flights.AirportResponse
import com.michael.viatoapp.model.response.flights.CitiesResponse
import com.michael.viatoapp.model.response.flights.CountriesResponse
import com.michael.viatoapp.model.response.flights.FlightDetailsResponse
import com.michael.viatoapp.model.response.flights.FlightsResponse
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

interface ApiService {
    @POST("flight/countries")
    fun getAllCountries(@Body() countriesSearch: FlighCountriesSearch): Call<CountriesResponse>

    @POST("flight/cities")
    fun getAllCities(@Body() citiesSearch: FlightCitiesSearch): Call<CitiesResponse>

    @POST("flight/flights")
    fun getAllFlights(@Body() flightsSearch: AllFlightsSearch): Call<FlightsResponse>

    @POST("flight/complete")
    fun getAllFlightsComplete(@Body() flightSession: FlightsSessionComplete): Call<AirportResponse>

    @POST("flight")
    fun getFlightDetails(@Body() flightDetailsResponse: FlightDetailsSearch): Call<FlightDetailsResponse>

    @GET("airports")
    fun getAllAirports(): Call<AirportResponse>

    @POST("stays/city")
    fun getCity(@Body() citySearch: CitySearch): Call<CityResponse>

    @POST("stays/hotels")
    fun getHotels(@Body() hotelsSearch: HotelsSearch): Call<HotelsResponse>

    @POST("stays/hotels/prices")
    fun getHotelPrices(@Body() hotelPricesSearch: HotelPricesSearch): Call<HotelPricesResponse>
}