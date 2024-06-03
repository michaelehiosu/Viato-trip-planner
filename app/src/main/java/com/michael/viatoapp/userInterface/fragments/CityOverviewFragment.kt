package com.michael.viatoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.viatoapp.model.response.Flight
import com.michael.viatoapp.userInterface.adapter.FlightAdapter
import com.michael.viatoapp.model.response.Hotel
import com.michael.viatoapp.userInterface.adapter.HotelAdapter
import com.michael.viatoapp.databinding.ActivityCityOverviewBinding
import com.michael.viatoapp.userInterface.activities.MainNavigationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CityOverviewFragment : Fragment() {

    private lateinit var binding: ActivityCityOverviewBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityCityOverviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize RecyclerViews
        binding.recyclerViewFlights.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHotels.layoutManager = LinearLayoutManager(context)

        fetchFlightsAndHotels { flights, hotels ->
            bindFlights(flights)
            bindHotels(hotels)
        }

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

    private fun fetchFlightsAndHotels(callback: (MutableList<Flight>, MutableList<Hotel>) -> Unit) {
        val flightsCollection = firestore.collection("flights")
        val hotelsCollection = firestore.collection("hotels")

        val flights = mutableListOf<Flight>()
        val hotels = mutableListOf<Hotel>()

        flightsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val flight = Flight(
                        document.getString("departure") ?: "",
                        document.getString("arrival") ?: "",
                        document.getString("price") ?: "",
                        document.getString("duration") ?: "",
                        document.getString("type") ?: "",
                        document.getString("departureTime") ?: "",
                        document.getString("arrivalTime") ?: ""
                    )
                    flights.add(flight)
                }
                hotelsCollection
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(20)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val hotel = Hotel(
                                document.getString("name") ?: "",
                                document.getString("rating") ?: "",
                                document.getString("price") ?: "",
                                document.getString("address") ?: "",
                                document.getString("review") ?: ""
                            )
                            hotels.add(hotel)
                        }
                        callback(flights, hotels)
                    }
            }
    }

    private fun bindFlights(flights: MutableList<Flight>) {
        val flightAdapter = FlightAdapter(flights)
        binding.recyclerViewFlights.adapter = flightAdapter
        flightAdapter.notifyDataSetChanged()
    }

    private fun bindHotels(hotels: MutableList<Hotel>) {
        val hotelAdapter = HotelAdapter(hotels)
        binding.recyclerViewHotels.adapter = hotelAdapter
        hotelAdapter.notifyDataSetChanged()
    }
}
