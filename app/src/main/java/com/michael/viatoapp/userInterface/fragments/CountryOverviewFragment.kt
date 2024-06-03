package com.michael.viatoapp.userInterface.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityCountryOverviewBinding
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.response.Activities
import com.michael.viatoapp.userInterface.adapter.CityAdapter

class CountryOverviewFragment : Fragment() {
    private lateinit var binding : ActivityCountryOverviewBinding
    private var country: Country? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityCountryOverviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())

        country = arguments?.getSerializable("country") as? Country
        var cities = mutableListOf<Activities>(
            Activities(R.drawable.usa, "Rio de Janiero", "Click for details"),
            Activities(R.drawable.usa, "Brazilia", "Click for details"),
            Activities(R.drawable.redeemer, "Sao Paulo", "Click for details"),
            Activities(R.drawable.redeemer, "Curitiba", "Click for details"),
            Activities(R.drawable.redeemer, "Manaus", "Click for details")
        )

        Log.d("Country", "$country")
        bind(cities);
    }

    private fun bind(cities : MutableList<Activities>) {
        val adapter = CityAdapter(cities)
        binding.recyclerViewActivities.adapter = adapter
    }
}