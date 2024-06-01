package com.michael.viatoapp.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.michael.viatoapp.model.data.flights.Airport
import com.michael.viatoapp.model.data.flights.City
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.data.flights.Itinerary
import com.michael.viatoapp.model.data.flights.ItineraryDetails
import com.michael.viatoapp.model.request.flights.AllFlightsSearch
import com.michael.viatoapp.model.request.flights.FlighCountriesSearch
import com.michael.viatoapp.model.request.flights.FlightCitiesSearch
import com.michael.viatoapp.model.request.flights.FlightDetailsSearch
import com.michael.viatoapp.model.response.flights.AirportResponse
import com.michael.viatoapp.model.response.flights.CitiesResponse
import com.michael.viatoapp.model.response.flights.CityResult
import com.michael.viatoapp.model.response.flights.CountriesResponse
import com.michael.viatoapp.model.response.flights.FlightDetailsResponse
import com.michael.viatoapp.model.response.flights.FlightItinerary
import com.michael.viatoapp.model.response.flights.FlightsResponse
import com.michael.viatoapp.model.response.flights.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    var gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://us-central1-viato-app.cloudfunctions.net/app/trips/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)


    suspend fun getAllAirport(): List<Airport>{
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<AirportResponse> = apiService.getAllAirports()
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val airports = retrievedData.data.map { airport: Airport ->
                            Airport(
                                airport.iata,
                                airport.name,
                                airport.location,
                                airport.skyId,
                                airport.timezone,
                                airport.id
                            )
                        }
                        return@withContext airports
                    }
                }
            } catch (e: Exception) {
                // Handle any exceptions, e.g., network errors
                Log.e("getAirportsFromAPI", "Error: $e")
            }

            return@withContext emptyList()
        }
    }


    suspend fun getAllCountries(flightCountriesSearch : FlighCountriesSearch) : List<Country> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<CountriesResponse> = apiService.getAllCountries(flightCountriesSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val countries = retrievedData.data.everywhereDestination.results.map { result : Result ->
                            Country(
                                entityId = result.entityId,
                                skyId = result.skyId,
                                name = result.content.location.name,
                                cheapestPrice = result.content.flightQuotes.cheapest.price,
                                imageUrl = result.content.image.url
                            )
                        }
                            return@withContext countries
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving data", "Error: $e")
            }

            return@withContext emptyList();
        }
    }

    suspend fun getAllCities(flightCitiesSearch: FlightCitiesSearch) : List<City> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<CitiesResponse> = apiService.getAllCities(flightCitiesSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        var cities = retrievedData.data.countryDestination.results.map { cityResult : CityResult ->
                            City(
                                entityId = cityResult.entityId,
                                skyId = cityResult.skyId,
                                name = cityResult.content.location.name,
                                imageUrl = cityResult.content.image.url
                            )
                        }
                        return@withContext cities
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving data", "Error: $e")
            }

            return@withContext emptyList();
        }
    }

    suspend fun getAllFlights(flightsSearch: AllFlightsSearch) : List<Itinerary> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<FlightsResponse> = apiService.getAllFlights(flightsSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        var itineraries = retrievedData.data.itineraries.map { itinerary : FlightItinerary ->
                            Itinerary(
                                token = "",
                                id = itinerary.id,
                                rawPrice = itinerary.price.rawPrice,
                                formattedPrice = itinerary.price.formattedPrice,
                                originId = itinerary.legs[0].origin.id,
                                originName = itinerary.legs[0].origin.name,
                                destinationId = itinerary.legs[0].destination.id,
                                destinationName = itinerary.legs[0].destination.name,
                                marketingCarrierName = itinerary.legs[0].carrier.marketing.name,
                                marketingCarrierLogo = itinerary.legs[0].carrier.marketing.logoUrl,
                                operatingCarrier = itinerary.legs[0].carrier.operating.name,
                                operatingCarrierLogo = itinerary.legs[0].carrier.operating.logoUrl,
                                durationOutbound = itinerary.legs[0].durationInMinutes,
                                durationInbound = itinerary.legs[1].durationInMinutes,
                                stopCountOutbound = itinerary.legs[0].stopCount,
                                stopCountInbound = itinerary.legs[1].stopCount,
                                outboundDepartureTime = itinerary.legs[0].departure,
                                outboundArrivalTime = itinerary.legs[0].arrival,
                                inboundDepartureTime = itinerary.legs[1].departure,
                                inboundArrivalTime = itinerary.legs[1].arrival
                            )
                        }
                        return@withContext itineraries
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving flight data", "Error: $e")
            }

            return@withContext emptyList();
        }
    }

    suspend fun getFlightDetails(flightsDetailsSearch: FlightDetailsSearch): ItineraryDetails? {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<FlightDetailsResponse> = apiService.getFlightDetails(flightsDetailsSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val result = retrievedData.data.itinerary
                        if (result.pricingOptions.isNotEmpty() && result.pricingOptions[0].agents.isNotEmpty()) {
                            val itinerary = ItineraryDetails(
                                id = result.id,
                                bookingUrl = result.pricingOptions[0].agents[0].url
                            )
                            return@withContext itinerary
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving flight data", "Error: $e")
            }

            return@withContext null
        }
    }



}
