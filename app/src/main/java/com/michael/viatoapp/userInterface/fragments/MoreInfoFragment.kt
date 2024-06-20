package com.michael.viatoapp.userInterface.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.viatoapp.databinding.ActivityMoreInfoBinding
import com.michael.viatoapp.model.data.flights.Itinerary
import com.michael.viatoapp.model.data.stays.Hotel

class MoreInfoFragment : Fragment() {
    private lateinit var binding: ActivityMoreInfoBinding
    private var selectedHotel: Hotel? = null
    private var selectedItinerary: Itinerary? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            selectedItinerary = it.getSerializable("selectedItinerary") as? Itinerary
            selectedHotel = it.getSerializable("selectedHotel") as? Hotel
        }
        Log.d("hotel info:", "$selectedHotel")
        Log.d("flight info:", "$selectedItinerary")
        binding = ActivityMoreInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}