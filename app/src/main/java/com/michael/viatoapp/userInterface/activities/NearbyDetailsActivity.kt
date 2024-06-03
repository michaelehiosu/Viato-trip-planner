package com.michael.viatoapp.userInterface.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
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
import com.michael.viatoapp.databinding.ActivityNearbyDetailsBinding

class NearbyDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityNearbyDetailsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestLocationPermissionCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearbyDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                val anotherLocation = LatLng(52.7798, 6.9208) // Another location

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
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            requestLocationPermissionCode
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestLocationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                    getLastKnownLocation()
                }
            }
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
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
