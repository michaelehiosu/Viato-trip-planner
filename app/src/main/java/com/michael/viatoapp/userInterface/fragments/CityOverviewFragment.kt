package com.michael.viatoapp.userInterface.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.viatoapp.api.ApiClient
import com.michael.viatoapp.databinding.ActivityCityOverviewBinding
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.data.flights.Itinerary
import com.michael.viatoapp.model.data.stays.Hotel
import com.michael.viatoapp.model.request.flights.AllFlightsSearch
import com.michael.viatoapp.model.request.flights.FlighCountriesSearch
import com.michael.viatoapp.model.request.stays.HotelsSearch
import com.michael.viatoapp.model.response.Flight
import com.michael.viatoapp.userInterface.activities.MainNavigationActivity
import com.michael.viatoapp.userInterface.adapter.CountryAdapter
import com.michael.viatoapp.userInterface.adapter.FlightAdapter
import com.michael.viatoapp.userInterface.adapter.HotelAdapter
import kotlinx.coroutines.launch

class CityOverviewFragment : Fragment() {
    private lateinit var binding: ActivityCityOverviewBinding
    private lateinit var flightAdapter: FlightAdapter
    private lateinit var hotelAdapter: HotelAdapter
    private val apiClient = ApiClient()
    private val apiService = ApiClient().apiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = ActivityCityOverviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flightAdapter = FlightAdapter(mutableListOf())
        hotelAdapter = HotelAdapter(mutableListOf())

        binding.recyclerViewFlights.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFlights.adapter = flightAdapter

        binding.recyclerViewHotels.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHotels.adapter = hotelAdapter

        fetchFlightsAndHotels()

        binding.buttonGo.setOnClickListener {
            val context = activity
            if (context is MainNavigationActivity) {
                context.navigateToMoreInfoFragment()
            }
        }

        binding.buttonGoTo.setOnClickListener {
            val context = activity
            if (context is MainNavigationActivity) {
                context.navigateToMoreInfoFragment()
            }
        }
    }

    private fun fetchFlightsAndHotels() {
        lifecycleScope.launch {
            // Define your search parameters here
            val flightsSearch = AllFlightsSearch(
                fromEntityId = "LAX",
                toEntityId = "GIG",
                departDate = "2023-05-04",
                returnDate = "2023-05-10",
                currency = "EUR",
                dummy = true
            )
            val hotelsSearch = HotelsSearch(
                entityId = "Rio de Janeiro",
                checkIn = "2023-05-04",
                checkOut = "2023-05-10",
                maxPrice = 200,
                dummy = true
            )

            try {
                val flightList: MutableList<Itinerary> = apiClient.getAllFlights(flightsSearch)
                //val hotelList: List<Hotel> = apiService.getHotels(hotelsSearch).execute().body()?.data ?: emptyList()

                updateFlightsRecyclerView(flightList)
                //hotelAdapter.updateHotels(hotelList)
            } catch (e: Exception) {
                Log.e("CityOverviewFragment", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun updateFlightsRecyclerView(flights: MutableList<Itinerary>) {
        flights.map {
            binding.recyclerViewFlights.adapter = FlightAdapter(flights)
        }
    }
}
