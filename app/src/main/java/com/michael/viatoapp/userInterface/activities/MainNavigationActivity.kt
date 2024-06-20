package com.michael.viatoapp.userInterface.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityMainNavigationBinding
import com.michael.viatoapp.model.data.SearchData
import com.michael.viatoapp.model.data.flights.City
import com.michael.viatoapp.userInterface.fragments.CityOverviewFragment
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.request.flights.FlightCountriesSearch
import com.michael.viatoapp.userInterface.fragments.CountryOverviewFragment
import com.michael.viatoapp.userInterface.fragments.MoreInfoFragment
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

    fun navigateToCityOverviewFragment(city: City, countrySearch: FlightCountriesSearch, searchData: SearchData) {
        val bundle = Bundle().apply {
            putSerializable("city", city)
            putSerializable("countrySearch", countrySearch)
            putSerializable("searchData", searchData)
        }
        val cityOverviewFragment = CityOverviewFragment().apply {
            arguments = bundle
        }
        supportFragmentManager.commit {
            replace(R.id.fragment_content, cityOverviewFragment)
            addToBackStack(null)
        }
    }

    fun navigateToCountryOverviewFragment(country : Country, countrySearch: FlightCountriesSearch, searchData: SearchData) {

        val bundle = Bundle().apply {
            putSerializable("country", country)
            putSerializable("countrySearch", countrySearch)
            putSerializable("searchData", searchData)
        }
        val countryOverviewFragement = CountryOverviewFragment().apply {
            arguments = bundle
        }
        supportFragmentManager.commit {
            replace(R.id.fragment_content, countryOverviewFragement)
            addToBackStack(null)
        }
    }

    fun navigateToMoreInfoFragment(bundle: Bundle) {
        val moreInfoFragment = MoreInfoFragment().apply {
            arguments = bundle
        }
        supportFragmentManager.commit {
            replace(R.id.fragment_content, moreInfoFragment)
            addToBackStack(null)
        }
    }
}