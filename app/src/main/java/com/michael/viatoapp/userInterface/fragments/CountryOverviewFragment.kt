package com.michael.viatoapp.userInterface.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.michael.viatoapp.R
import com.michael.viatoapp.api.ApiClient
import com.michael.viatoapp.api.ApiHelper
import com.michael.viatoapp.databinding.ActivityCountryOverviewBinding
import com.michael.viatoapp.model.data.SearchData
import com.michael.viatoapp.model.data.flights.City
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.request.flights.FlightCitiesSearch
import com.michael.viatoapp.model.request.flights.FlightCountriesSearch
import com.michael.viatoapp.userInterface.adapter.CityAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryOverviewFragment : Fragment() {
    private lateinit var binding: ActivityCountryOverviewBinding
    private lateinit var apiClient: ApiClient
    private lateinit var apiHelper: ApiHelper
    private lateinit var allCities: List<City>
    private var country: Country? = null
    private var countrySearch: FlightCountriesSearch? = null
    private var searchData: SearchData? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            country = it.getSerializable("country") as Country?
            countrySearch = it.getSerializable("countrySearch") as FlightCountriesSearch
            searchData = it.getSerializable("searchData") as SearchData
        }
        binding = ActivityCountryOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiClient = ApiClient()
        apiHelper = ApiHelper()
    }

    override fun onResume() {
        super.onResume()

        var citiesSearch: FlightCitiesSearch? = null

        if (countrySearch != null && country != null) {
            citiesSearch = FlightCitiesSearch(
                fromEntityId = countrySearch!!.fromEntityId,
                skyId = country!!.skyId,
                departDate = countrySearch!!.departDate,
                returnDate = countrySearch!!.returnDate,
                currency = countrySearch!!.currency,
                dummy = true
            )
        }

        if (citiesSearch != null) {
            fetchCities(citiesSearch)
        }

        updateTheTopSectionUi()
    }

    private fun fetchCities(citiesSearch: FlightCitiesSearch) {
        activateProgressBar()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val cities = apiClient.getAllCities(citiesSearch)

                withContext(Dispatchers.Main) {
                    allCities = cities
                    bindCitiesToAdapter(cities)
                }
            } catch (e: Exception) {
                Log.e("error", "$e")
            }
        }
    }

    private fun updateTheTopSectionUi() {
        Glide.with(this)
            .load(country?.imageUrl)
            .placeholder(R.drawable.brazil) // Optional placeholder
            .into(binding.countryImage)

        binding.countryTextview.text = country?.name
    }

    private fun bindCitiesToAdapter(cities: List<City>) {
        if (cities.isNotEmpty()) {
            val cityAdapter = CityAdapter(cities, countrySearch!!, searchData!!)
            binding.recyclerViewActivities.adapter = cityAdapter
            binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())
            deactivateProgressBar()
        } else {
            Log.d("bind", "No cities to display")
        }
    }

    private fun activateProgressBar() {
        binding.recyclerViewActivities.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun deactivateProgressBar() {
        binding.recyclerViewActivities.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }
}
