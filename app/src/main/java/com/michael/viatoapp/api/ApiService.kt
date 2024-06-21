package com.michael.viatoapp.api

import com.michael.viatoapp.model.request.attractions.AttractionsSearch
import com.michael.viatoapp.model.request.flights.AllFlightsSearch
import com.michael.viatoapp.model.request.flights.FlightCitiesSearch
import com.michael.viatoapp.model.request.flights.FlightCountriesSearch
import com.michael.viatoapp.model.request.flights.FlightDetailsSearch
import com.michael.viatoapp.model.request.flights.FlightsSessionComplete
import com.michael.viatoapp.model.request.stays.CitySearch
import com.michael.viatoapp.model.request.stays.HotelPricesSearch
import com.michael.viatoapp.model.request.stays.HotelsSearch
import com.michael.viatoapp.model.response.attractions.AttractionsResponse
import com.michael.viatoapp.model.response.flights.AirportResponse
import com.michael.viatoapp.model.response.flights.CitiesResponse
import com.michael.viatoapp.model.response.flights.CountriesResponse
import com.michael.viatoapp.model.response.flights.FlightDetailsResponse
import com.michael.viatoapp.model.response.flights.FlightsResponse
import com.michael.viatoapp.model.response.stays.CityResponse
import com.michael.viatoapp.model.response.stays.HotelPricesResponse
import com.michael.viatoapp.model.response.stays.HotelsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("trips/flight/countries")
    fun getAllCountries(@Body() countriesSearch: FlightCountriesSearch): Call<CountriesResponse>

    @POST("trips/flight/cities")
    fun getAllCities(@Body() citiesSearch: FlightCitiesSearch): Call<CitiesResponse>

    @POST("trips/flight/flights")
    fun getAllFlights(@Body() flightsSearch: AllFlightsSearch): Call<FlightsResponse>

    @POST("trips/flight/complete")
    fun getAllFlightsComplete(@Body() flightSession: FlightsSessionComplete): Call<AirportResponse>

    @POST("trips/flight")
    fun getFlightDetails(@Body() flightDetailsResponse: FlightDetailsSearch): Call<FlightDetailsResponse>

    @GET("trips/airports")
    fun getAllAirports(): Call<AirportResponse>

    @POST("trips/stays/city")
    fun getHotelCity(@Body() citySearch: CitySearch): Call<CityResponse>

    @POST("trips/stays/hotels")
    fun getHotels(@Body() hotelsSearch: HotelsSearch): Call<HotelsResponse>

    @POST("trips/stays/hotels/prices")
    fun getHotelPrices(@Body() hotelPricesSearch: HotelPricesSearch): Call<HotelPricesResponse>

    @POST("nearby/attractions")
    fun getAllAttractions(@Body() attractionsSearch: AttractionsSearch): Call<AttractionsResponse>
}