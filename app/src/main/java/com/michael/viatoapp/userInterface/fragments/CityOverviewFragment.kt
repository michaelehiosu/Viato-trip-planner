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
import com.michael.viatoapp.databinding.ActivityCityOverviewBinding
import com.michael.viatoapp.databinding.ActivityMoreInfoBinding
import com.michael.viatoapp.userInterface.activities.MainNavigationActivity

class CityOverviewFragment : Fragment() {
private lateinit var binding: ActivityCityOverviewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivityCityOverviewBinding.inflate(layoutInflater, container, false)
        return binding.root
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
        binding.recyclerViewFlights.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFlights.adapter = FlightAdapter(flights)

        binding.recyclerViewHotels.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHotels.adapter = HotelAdapter(hotels)


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
}
