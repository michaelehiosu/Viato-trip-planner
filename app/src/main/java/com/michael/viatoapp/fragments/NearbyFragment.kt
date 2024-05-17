package com.michael.viatoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.michael.viatoapp.R
import com.michael.viatoapp.classes.Activities
import com.michael.viatoapp.classes.ActivityAdapter
import com.michael.viatoapp.databinding.ActivityNearbyBinding

class NearbyFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding : ActivityNearbyBinding
    private lateinit var mMap : GoogleMap
    private lateinit var mapView: MapView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityNearbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        var activities = mutableListOf<Activities>(
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"),
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"),
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"),
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"),
            Activities(R.drawable.redeemer, "Christ the Redeemer", "Type: Monuments and Memorials"))

        bind(activities);
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        val targetLocation = LatLng(-22.9068, -43.1729)
        val zoomLevel = 15f
        mMap.addMarker(MarkerOptions().position(targetLocation).title("Rio de Janiero"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, zoomLevel))
    }


    private fun bind(activities : MutableList<Activities>) {
        val adapter = ActivityAdapter(activities)
        binding.recyclerViewActivities.adapter = adapter
    }
}