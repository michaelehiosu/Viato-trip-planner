package com.michael.viatoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.viatoapp.R
import com.michael.viatoapp.classes.Activities
import com.michael.viatoapp.classes.CountryAdapter
import com.michael.viatoapp.databinding.ActivityTripsBinding

class TripsFragment : Fragment() {
    private lateinit var binding: ActivityTripsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())

        val activities = mutableListOf(
            Activities(R.drawable.usa, "United States", "From $500"),
            Activities(R.drawable.usa, "United States", "From $500"),
            Activities(R.drawable.usa, "United States", "From $500"),
            Activities(R.drawable.usa, "United States", "From $500")
        )

        binding.recyclerViewActivities.adapter = CountryAdapter(activities)
    }
}
