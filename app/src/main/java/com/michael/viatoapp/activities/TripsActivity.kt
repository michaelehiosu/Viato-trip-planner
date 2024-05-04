package com.michael.viatoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)
        bottomNavView = findViewById(R.layout.item_bottom_navbar)

        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_trips -> {

                    true
                }
                R.id.navigation_profile -> {
                    // Handle navigation to the "Profile" fragment or activity
                    true
                }
                R.id.navigation_near_me -> {
                    // Handle navigation to the "Near Me" fragment or activity
                    true
                }
                else -> false
            }
        }

    }
}