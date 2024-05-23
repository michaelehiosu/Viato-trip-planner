package com.michael.viatoapp.userInterface.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityMainNavigationBinding
import com.michael.viatoapp.userInterface.fragments.NearbyFragment
import com.michael.viatoapp.userInterface.fragments.ProfileFragment
import com.michael.viatoapp.userInterface.fragments.TripsFragment

class MainNavigationActivity : AppCompatActivity(), OnItemSelectedListener {
    private lateinit var binding : ActivityMainNavigationBinding;
    override fun onCreate(savedInstanceState: Bundle?) {0
        super.onCreate(savedInstanceState)
        binding = ActivityMainNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.navigation_trips -> {
            onTripsClicked()
        }
        R.id.navigation_near_me -> {
            onNearmeClicked()
        }
        R.id.navigation_profile -> {
            onProfileClicked()
        }
        else -> false
    }

    private fun onTripsClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_content, TripsFragment())
        }
        return true
    }

    private fun onProfileClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_content, ProfileFragment())
        }
        return true
    }

    private fun onNearmeClicked(): Boolean{
        supportFragmentManager.commit {
            replace(R.id.fragment_content, NearbyFragment())
        }
        return true
    }
}