package com.michael.viatoapp.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class NearbyDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityNearbyDetailsBinding
    private lateinit var mMap : GoogleMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearbyDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true

        val targetLocation = LatLng(-22.9068, -43.1729)
        val anotherLocation = LatLng(-22.9707, -43.1824) // Another location

        mMap.addMarker(MarkerOptions().position(targetLocation).title("Rio de Janiero"))
        mMap.addMarker(MarkerOptions().position(anotherLocation).title("Another Location").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))



        val boundsBuilder = LatLngBounds.Builder()
        boundsBuilder.include(targetLocation)
        boundsBuilder.include(anotherLocation)
        val bounds = boundsBuilder.build()
        val padding = 100 // Padding in pixels
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.moveCamera(cameraUpdate)

        // Add a line connecting the two markers
        mMap.addPolyline(PolylineOptions().add(targetLocation, anotherLocation).color(Color.BLUE))
    }
}