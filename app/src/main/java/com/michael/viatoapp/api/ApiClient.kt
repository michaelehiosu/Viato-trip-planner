package com.michael.viatoapp.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.michael.viatoapp.model.data.attraction.Attraction
import com.michael.viatoapp.model.data.flights.Airport
import com.michael.viatoapp.model.data.flights.City
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.data.flights.Itinerary
import com.michael.viatoapp.model.data.flights.ItineraryDetails
import com.michael.viatoapp.model.data.stays.Hotel
import com.michael.viatoapp.model.data.stays.HotelCity
import com.michael.viatoapp.model.data.stays.HotelPrice
import com.michael.viatoapp.model.request.attractions.AttractionsSearch
import com.michael.viatoapp.model.request.flights.AllFlightsSearch
import com.michael.viatoapp.model.request.flights.FlightCitiesSearch
import com.michael.viatoapp.model.request.flights.FlightCountriesSearch
import com.michael.viatoapp.model.request.flights.FlightDetailsSearch
import com.michael.viatoapp.model.request.stays.CitySearch
import com.michael.viatoapp.model.request.stays.HotelPricesSearch
import com.michael.viatoapp.model.request.stays.HotelsSearch
import com.michael.viatoapp.model.response.attractions.Attractions
import com.michael.viatoapp.model.response.attractions.AttractionsResponse
import com.michael.viatoapp.model.response.flights.AirportResponse
import com.michael.viatoapp.model.response.flights.CitiesResponse
import com.michael.viatoapp.model.response.flights.CityResult
import com.michael.viatoapp.model.response.flights.CountriesResponse
import com.michael.viatoapp.model.response.flights.FlightDetailsResponse
import com.michael.viatoapp.model.response.flights.FlightItinerary
import com.michael.viatoapp.model.response.flights.FlightsResponse
import com.michael.viatoapp.model.response.flights.Result
import com.michael.viatoapp.model.response.stays.CityResponse
import com.michael.viatoapp.model.response.stays.FoundCity
import com.michael.viatoapp.model.response.stays.HotelCard
import com.michael.viatoapp.model.response.stays.HotelPricesResponse
import com.michael.viatoapp.model.response.stays.HotelsResponse
import com.michael.viatoapp.model.response.stays.Rate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var city: HotelCity
    var gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://us-central1-viato-app.cloudfunctions.net/app/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    public val apiService: ApiService = retrofit.create(ApiService::class.java)

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
                                iata = airport.iata,
                                name = airport.name,
                                location = airport.location,
                                skyId = airport.skyId,
                                time = airport.time,
                                id = airport.id
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


    suspend fun getAllCountries(flightCountriesSearch: FlightCountriesSearch): MutableList<Country> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<CountriesResponse> = apiService.getAllCountries(flightCountriesSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val countries = retrievedData.data.everywhereDestination.results.mapNotNull { result: Result ->
                            val flightQuotes = result.content.flightQuotes
                            val imageUrl = result.content.image?.url
                            val cheapestPrice = flightQuotes?.cheapest?.rawPrice
                            Country(
                                entityId = result.entityId,
                                skyId = result.skyId,
                                name = result.content.location.name,
                                cheapestPrice = cheapestPrice,
                                imageUrl = imageUrl
                            )
                        }
                        return@withContext countries
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving data", "Error: $e")
            }

            return@withContext emptyList()
        }.toMutableList()
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

    suspend fun getAllFlights(flightsSearch: AllFlightsSearch) : MutableList<Itinerary> {
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
                                rawPrice = itinerary.price.raw,
                                formattedPrice = itinerary.price.formatted,
                                originId = itinerary.legs[0].origin.id,
                                originName = itinerary.legs[0].origin.name,
                                destinationId = itinerary.legs[0].destination.id,
                                destinationName = itinerary.legs[0].destination.name,
                                marketingCarrierName = itinerary.legs[0].carrier?.marketing?.name,
                                marketingCarrierLogo = itinerary.legs[0].carrier?.marketing?.logoUrl,
                                operatingCarrier = itinerary.legs[0].carrier?.operating?.name,
                                operatingCarrierLogo = itinerary.legs[0].carrier?.operating?.logoUrl,
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

            return@withContext emptyList()
        }.toMutableList()
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

    // Hotels functions

    suspend fun getHotelCity(citySearch: CitySearch): HotelCity {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<CityResponse> = apiService.getHotelCity(citySearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val cities = retrievedData.data.map { city: FoundCity ->
                            HotelCity(
                                location = city.location,
                                entityName = city.entityName,
                                entityId = city.entityId,
                                entityType = city.entityType,
                            )
                        }

                        for (selectedCity in cities) {
                            if (selectedCity.entityType == "city") {
                                city = selectedCity
                            }
                        }
                        return@withContext city
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving cities data", "Error: $e")
            }

            return@withContext HotelCity("", "", "", "")
        }
    }

    suspend fun getHotels(hotelsSearch: HotelsSearch): MutableList<Hotel> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<HotelsResponse> = apiService.getHotels(hotelsSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        var hotels = retrievedData.data.results.hotelCards.map { hotel: HotelCard ->
                            Hotel(
                                name = hotel.name,
                                latitude = hotel.coordinates.latitude,
                                longitude = hotel.coordinates.longitude,
                                images = hotel.images[0],
                                reviewScore = hotel?.reviewsSummary?.score,
                                priceRaw = hotel.lowestPrice.rawPrice,
                                hotelId = hotel.hotelId,
                                relevantPoi = hotel.relevantPoiDistance,
                                scoreDesc = hotel?.reviewsSummary?.scoreDesc
                            )
                        }
                        return@withContext hotels
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving hotels data", "Error: $e")
            }

            return@withContext emptyList()
        }.toMutableList()
    }

    suspend fun getHotelPrices(hotelPricesSearch: HotelPricesSearch): MutableList<HotelPrice> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<HotelPricesResponse> = apiService.getHotelPrices(hotelPricesSearch)
                val response = call.execute()
                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val rates = retrievedData.data.metaInfo.rates.map { rate: Rate ->
                            HotelPrice(
                                partnerName = rate.partnerName,
                                partnerLogo = rate.partnerLogo,
                                roomType = rate.roomType,
                                roomPolicies = rate.roomPolicies,
                                deeplink = rate.deeplink,
                                rawPrice = rate.rawPrice,
                            )
                        }
                        return@withContext rates
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving Hotel Prices", "Error: $e")
            }
            return@withContext emptyList()
        }.toMutableList()
    }


    suspend fun getAttractions(attractionsSearch: AttractionsSearch) : MutableList<Attraction> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<AttractionsResponse> = apiService.getAllAttractions(attractionsSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        var attractions = retrievedData.data.map { attraction : Attractions ->
                            Attraction(
                                name = attraction.name,
                                locationId = attraction.location_id,
                                numReviews = attraction.num_reviews,
                                locationString = attraction.location_string,
                                image = attraction.photo?.images?.large?.url,
                                website = attraction.website,
                                address = attraction.address,
                                tripAdvisorLink = attraction.web_url,
                                subCategory = attraction.subcategory?.get(0)?.name,
                                latitude = attraction.latitude,
                                longitude = attraction.longitude
                            )
                        }
                        return@withContext attractions
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving attractions data", "Error: $e")
            }

            return@withContext emptyList()
        }.toMutableList()
    }
}
