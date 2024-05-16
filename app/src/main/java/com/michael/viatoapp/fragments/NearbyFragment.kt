package com.michael.viatoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.viatoapp.R
import com.michael.viatoapp.classes.Activities
import com.michael.viatoapp.classes.ActivityAdapter
import com.michael.viatoapp.databinding.ActivityNearbyBinding

class NearbyFragment : Fragment() {
    private lateinit var binding : ActivityNearbyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityNearbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())

        var activities = mutableListOf<Activities>(
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"),
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"),
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"),
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"),
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"))

        bind(activities);
    }

    private fun bind(activities : MutableList<Activities>) {
        val adapter = ActivityAdapter(activities)
        binding.recyclerViewActivities.adapter = adapter
    }
}