package com.michael.viatoapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityTripsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TripsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTripsBinding
    private lateinit var bottomNavView: BottomNavigationView

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_trips -> {
                    // "Trips" is already shown, so no need to navigate again
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.navigation_near_me -> {
                    startActivity(Intent(this, NearbyActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}