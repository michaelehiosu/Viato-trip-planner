package com.michael.viatoapp.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.viatoapp.databinding.ActivityNearbyBinding
import com.michael.viatoapp.model.response.Activities
import com.michael.viatoapp.userInterface.adapter.ActivityAdapter

class ViewActivityFragment : Fragment() {
    private lateinit var binding: ActivityNearbyBinding
    private lateinit var activityAdapter: ActivityAdapter
    private var activitiesList = mutableListOf<Activities>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityNearbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sample data
        activitiesList.add(Activities("image_url_1", "Christ the Redeemer", "Tijuca Forest National Park"))
        activitiesList.add(Activities("image_url_2", "Eiffel Tower", "Paris"))

        activityAdapter = ActivityAdapter(activitiesList)

        // Ensure the RecyclerView has a LayoutManager
        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewActivities.adapter = activityAdapter

        // Notify the adapter of data changes
        activityAdapter.notifyDataSetChanged()
    }
}
