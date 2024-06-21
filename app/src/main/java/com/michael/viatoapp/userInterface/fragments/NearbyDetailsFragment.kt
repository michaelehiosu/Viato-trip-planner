package com.michael.viatoapp.userInterface.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityNearbyBinding
import com.michael.viatoapp.databinding.ActivityNearbyDetailsBinding
import com.michael.viatoapp.model.data.attraction.Attraction
import com.michael.viatoapp.model.data.flights.City
import com.michael.viatoapp.model.request.flights.FlightCountriesSearch

class NearbyDetailsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: ActivityNearbyDetailsBinding
    private lateinit var mMap: GoogleMap
    private var attraction: Attraction? = null
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestLocationPermissionCode = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            attraction = it.getSerializable("attraction") as Attraction?
        }
        binding = ActivityNearbyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI(attraction)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun updateUI(attraction: Attraction?) {
        binding.attractionName.text = attraction?.name ?: "Nil"
        binding.attractionReview.text = attraction?.numReviews ?: "Nil"
        binding.attractionAddress.text = attraction?.address ?: "Nil"
        binding.attractionCategory.text = attraction?.subCategory ?: "Nil"

        val websiteUrl = attraction?.website
        if (websiteUrl != null) {
            val textViewWebsite = binding.attractionWebsite

            Linkify.addLinks(binding.attractionWebsite, Linkify.WEB_URLS)

            textViewWebsite.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
                startActivity(intent)
            }
        } else {
            binding.attractionWebsite.setText("Website not found for this attraction")
            binding.attractionWebsite.setTextColor(Color.parseColor("#2E1F08"))
        }


        val tripAdvisorUrl = attraction?.tripAdvisorLink
        if (tripAdvisorUrl != null) {
            val textViewTripAdvisor = binding.attractionMoreInfo

            Linkify.addLinks(binding.attractionMoreInfo, Linkify.WEB_URLS)

            textViewTripAdvisor.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tripAdvisorUrl))
                startActivity(intent)
            }
        } else {
            binding.attractionMoreInfo.setText("Link to more information not found")
            binding.attractionMoreInfo.setTextColor(Color.parseColor("#2E1F08"))
        }

        Glide.with(this)
            .load(attraction?.image)
            .placeholder(R.drawable.tijuca) // Optional placeholder
            .into(binding.attractionImage)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
//                val anotherLocation = LatLng(52.7798, 6.9208) // Another location
                val anotherLocation = LatLng(attraction?.latitude?.toDouble()!!, attraction?.longitude?.toDouble()!!)

                mMap.addMarker(MarkerOptions().position(currentLatLng).title("Your Location"))
                mMap.addMarker(
                    MarkerOptions().position(anotherLocation).title("Another Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                )

                val boundsBuilder = LatLngBounds.Builder()
                boundsBuilder.include(currentLatLng)
                boundsBuilder.include(anotherLocation)
                val bounds = boundsBuilder.build()
                val padding = 100 // Padding in pixels
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                mMap.moveCamera(cameraUpdate)

                // Add a line connecting the two markers
                mMap.addPolyline(PolylineOptions().add(currentLatLng, anotherLocation).color(Color.BLUE))

                // Calculate the distance between the two locations
                val results = FloatArray(1)
                Location.distanceBetween(
                    it.latitude, it.longitude,
                    anotherLocation.latitude, anotherLocation.longitude,
                    results
                )
            }
        }
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
                    getLastKnownLocation()
                }
            }
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
                val anotherLocation = LatLng(-22.9707, -43.1824) // Another location

                mMap.addMarker(MarkerOptions().position(currentLatLng).title("Your Location"))
                mMap.addMarker(
                    MarkerOptions().position(anotherLocation).title("Another Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                )

                val boundsBuilder = LatLngBounds.Builder()
                boundsBuilder.include(currentLatLng)
                boundsBuilder.include(anotherLocation)
                val bounds = boundsBuilder.build()
                val padding = 100 // Padding in pixels
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                mMap.moveCamera(cameraUpdate)

                // Add a line connecting the two markers
                mMap.addPolyline(PolylineOptions().add(currentLatLng, anotherLocation).color(Color.BLUE))

                // Calculate the distance between the two locations
                val results = FloatArray(1)
                Location.distanceBetween(
                    it.latitude, it.longitude,
                    anotherLocation.latitude, anotherLocation.longitude,
                    results
                )
            }
        }
    }
}
