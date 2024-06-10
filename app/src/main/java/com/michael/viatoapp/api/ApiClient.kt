package com.michael.viatoapp.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.michael.viatoapp.model.data.stays.City
import com.michael.viatoapp.model.data.stays.Hotel
import com.michael.viatoapp.model.data.stays.HotelPrice
import com.michael.viatoapp.model.request.stays.CitySearch
import com.michael.viatoapp.model.request.stays.HotelPricesSearch
import com.michael.viatoapp.model.request.stays.HotelsSearch
import com.michael.viatoapp.model.response.stays.CityResponse
import com.michael.viatoapp.model.response.stays.FoundCity
import com.michael.viatoapp.model.response.stays.HotelCard
import com.michael.viatoapp.model.response.stays.HotelPricesResponse
import com.michael.viatoapp.model.response.stays.HotelsResponse
import com.michael.viatoapp.model.response.stays.Rates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var city : City
    var gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://us-central1-viato-app.cloudfunctions.net/app/trips/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    suspend fun getCity(citySearch: CitySearch): City {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<CityResponse> = apiService.getCity(citySearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val cities = retrievedData.data.map { city: FoundCity ->
                            City(
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

            return@withContext City("", "", "", "")
        }
    }

    suspend fun getHotels(hotelsSearch: HotelsSearch): List<Hotel> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<HotelsResponse> = apiService.getHotels(hotelsSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val hotels = retrievedData.data.hotelsCards.map {hotel: HotelCard ->
                            Hotel (
                                name = hotel.name,
                                latitude = hotel.coordinates.latitude,
                                longitude = hotel.coordinates.longitude,
                                images = hotel.images,
                                reviewScore = hotel.reviewSummary.reviewScore,
                                priceRaw = hotel.lowestPrice.rawPrice,
                                hotelId = hotel.hotelId,
                            )
                        }
                        return@withContext hotels
                    }
                }
            } catch (e: Exception) {
                Log.e("Error Retrieving hotels data", "Error: $e")
            }

            return@withContext emptyList()
        }
    }

    suspend fun getHotelPrices(hotelPricesSearch: HotelPricesSearch): List<HotelPrice> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<HotelPricesResponse> = apiService.getHotelPrices(hotelPricesSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val rates = retrievedData.data.rates.map {rate: Rates ->
                            HotelPrice(
                                partnerName = rate.partnerName,
                                partnerLogo = rate.partnerLogo,
                                roomType = rate.roomType,
                                roomPolicies = rate.roomPolicies,
                                deepLink = rate.deepLink,
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
        }
    }
}