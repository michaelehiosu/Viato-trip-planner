package com.michael.viatoapp.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.michael.viatoapp.model.data.flights.Airports
import com.michael.viatoapp.model.data.flights.Countries
import com.michael.viatoapp.model.request.flights.FlighCountriesSearch
import com.michael.viatoapp.model.response.flights.AllAirportResponse
import com.michael.viatoapp.model.response.flights.AllCountriesResponse
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


    suspend fun getAllAirport(): List<Airports>{
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<AllAirportResponse> = apiService.getAllAirports()
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        val airports = retrievedData.todoArray.map { allAirport: Airports ->
                            Airports(
                                allAirport.todo
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


    suspend fun getAllCountries(flightCountriesSearch : FlighCountriesSearch) : List<Countries> {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<AllCountriesResponse> = apiService.getAllCountries(flightCountriesSearch)
                val response = call.execute()

                if (response.isSuccessful) {
                    val retrievedData = response.body()
                    if (retrievedData != null) {
                        var countries = retrievedData.todo.map { allCountries: Countries ->
                            Countries(
                                allCountries.todo
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


}
