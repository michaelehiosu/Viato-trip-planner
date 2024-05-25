package com.michael.viatoapp.api

import com.michael.viatoapp.model.request.flights.AllFlightsSearch
import com.michael.viatoapp.model.request.flights.FlighCountriesSearch
import com.michael.viatoapp.model.request.flights.FlightCitiesSearch
import com.michael.viatoapp.model.request.flights.FlightsSessionComplete
import com.michael.viatoapp.model.response.flights.AllAirportResponse
import com.michael.viatoapp.model.response.flights.AllCitiesResponse
import com.michael.viatoapp.model.response.flights.AllCountriesResponse
import com.michael.viatoapp.model.response.flights.AllFlightsResponse
import com.michael.viatoapp.model.response.flights.FlightDetailsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("flight/countries")
    fun getAllCountries(@Body() countriesSearch: FlighCountriesSearch): Call<AllCountriesResponse>

    @POST("flight/cities")
    fun getAllCities(@Body() citiesSearch: FlightCitiesSearch): Call<AllCitiesResponse>

    @POST("flight/flights")
    fun getAllFlights(@Body() flightsSearch: AllFlightsSearch): Call<AllFlightsResponse>

    @POST("flight/complete")
    fun getAllFlightsComplete(@Body() flightSession: FlightsSessionComplete): Call<AllFlightsResponse>

    @POST("flight")
    fun getFlightDetails(@Body() flightDetailsResponse: FlightDetailsResponse): Call<FlightDetailsResponse>

    @GET("device/read.php")
    fun getAllAirports(): Call<AllAirportResponse>
}

