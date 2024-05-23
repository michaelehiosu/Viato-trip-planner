package com.michael.viatoapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michael.viatoapp.model.response.Flight
import com.michael.viatoapp.userInterface.adapter.FlightAdapter
import com.michael.viatoapp.model.response.Hotel
import com.michael.viatoapp.userInterface.adapter.HotelAdapter
import com.michael.viatoapp.R

class CityOverviewFragment : Fragment() {

    private lateinit var flightRecyclerView: RecyclerView
    private lateinit var hotelRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_city_overview, container, false)

        Log.d("CityOverviewFragment", "onCreateView called")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy data
        val flights = mutableListOf<Flight>(
            Flight("LAX", "GIG", "$400", "13h 5m", "Direct", "9.30am - May 04", "11.30pm - May 04"),
            Flight("LAX", "GIG", "$420", "13h 5m", "Direct", "6.30am - May 04", "8.30pm - May 04")
            // Add more flights here
        )

        val hotels = mutableListOf<Hotel>(
            Hotel("Hotel Atlantico Prime", "7/10", "$400", "Avenida Delfim Moreira 696, Rio de Janeiro", "Good"),
            Hotel("Hotel Atlantico Prime", "7/10", "$400", "Avenida Delfim Moreira 696, Rio de Janeiro", "Good")
            // Add more hotels here
        )

        // Initialize RecyclerViews
        flightRecyclerView = view.findViewById(R.id.recycler_view_flights)
        flightRecyclerView.layoutManager = LinearLayoutManager(context)
        flightRecyclerView.adapter = FlightAdapter(flights)
        Log.d("CityOverviewFragment", "Flight adapter set with ${flights.size} items: ${flightRecyclerView.adapter != null}")

        hotelRecyclerView = view.findViewById(R.id.recycler_view_hotels)
        hotelRecyclerView.layoutManager = LinearLayoutManager(context)
        hotelRecyclerView.adapter = HotelAdapter(hotels)
        Log.d("CityOverviewFragment", "Hotel adapter set with ${hotels.size} items: ${hotelRecyclerView.adapter != null}")
    }
}
