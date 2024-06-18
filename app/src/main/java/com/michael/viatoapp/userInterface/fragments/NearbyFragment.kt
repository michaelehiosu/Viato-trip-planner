package com.michael.viatoapp.userInterface.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.michael.viatoapp.R
import com.michael.viatoapp.api.ApiClient
import com.michael.viatoapp.api.ApiHelper
import com.michael.viatoapp.userInterface.adapter.AttractionAdapter
import com.michael.viatoapp.databinding.ActivityNearbyBinding
import com.michael.viatoapp.model.data.attraction.Attraction
import com.michael.viatoapp.model.request.attractions.AttractionsSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class NearbyFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: ActivityNearbyBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestLocationPermissionCode = 1
    private lateinit var apiClient: ApiClient
    private lateinit var apiHelper: ApiHelper
    private var kilometerRange = "15"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityNearbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiClient = ApiClient()
        apiHelper = ApiHelper()

        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Get current location and fetch attractions
        getLastKnownLocation()

        val button5km = binding.button5km
        val button10km = binding.button10km
        val button15km = binding.button15km

        binding.button5km.setOnClickListener{
            toggleButtonPressed(button5km, button10km, button15km)
            kilometerRange = "5"
            changeAttractionsRange()
        }

        binding.button10km.setOnClickListener {
            toggleButtonPressed(button10km, button5km, button15km)
            kilometerRange = "10"
            changeAttractionsRange()
        }

        binding.button15km.setOnClickListener {
            toggleButtonPressed(button15km, button5km, button10km)
            kilometerRange = "15"
            changeAttractionsRange()
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                // Use real location data
                val attractionSearch = AttractionsSearch(
                    longitude = it.longitude.toString(),
                    latitude = it.latitude.toString(),
                    distance = kilometerRange,
                    currency = "EUR",
                    dummy = false
                )
                getAttractions(attractionSearch)
            }
        }
    }

    fun getAttractions(attractionsSearch: AttractionsSearch) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val allAttractions = apiClient.getAttractions(attractionsSearch)
                withContext(Dispatchers.Main) {
                    bind(allAttractions)
                }
            } catch (e: Exception) {
                Log.e("fetchAttractions", "Error: $e")
            }
        }
    }

//    private fun updateUIWithAttractions(attractions: List<Attraction>) {
//        // Update RecyclerView
//        val activities = attractions.map {
//            Attraction(
//                ""
//            )
//        }
//        bind(activities.toMutableList())
//
//        // Add markers to Google Map
////    for (attraction in attractions) {
////        val latLng = LatLng(attraction.latitude?.toDouble() ?: 0.0, attraction.longitude?.toDouble() ?: 0.0)
////        mMap.addMarker(MarkerOptions().position(latLng).title(attraction.name))
////    }
//    }


    private fun bind(attractions: MutableList<Attraction>) {
        val adapter = AttractionAdapter(attractions)
        binding.recyclerViewActivities.adapter = adapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true

        if (isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap.isMyLocationEnabled = true
            fetchLastKnownLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun fetchLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        val context = requireContext()
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            requestLocationPermissionCode
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestLocationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                    fetchLastKnownLocation()
                }
            }
        }
    }

    private fun changeAttractionsRange() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                    val attractionSearch = AttractionsSearch(
                    longitude = it.longitude.toString(),
                    latitude = it.latitude.toString(),
                    distance = kilometerRange,
                    currency = "EUR",
                    dummy = false
                )
                getAttractions(attractionSearch)
            }
        }
    }

    private fun toggleButtonPressed(button : Button, vararg otherButtons : Button) {
        button.setBackgroundColor(resources.getColor(R.color.orange))
        button.setTextColor(resources.getColor(R.color.white))

        for (button in otherButtons) {
            button.setBackgroundColor(resources.getColor(R.color.light_gray))
            button.setTextColor(resources.getColor(R.color.white))
        }
    }
}
