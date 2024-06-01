package com.michael.viatoapp.userInterface.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.viatoapp.R
import com.michael.viatoapp.api.ApiClient
import com.michael.viatoapp.api.ApiHelper
import com.michael.viatoapp.databinding.ActivityTripsBinding
import com.michael.viatoapp.model.response.Activities
import com.michael.viatoapp.model.data.flights.Airport
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.request.flights.FlighCountriesSearch
import com.michael.viatoapp.userInterface.adapter.CountryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class TripsFragment : Fragment() {
    private lateinit var binding: ActivityTripsBinding
    private var calendar = Calendar.getInstance()
    private lateinit var apiClient: ApiClient
    private lateinit var apiHelper: ApiHelper
    private var selectedStartDate: String? = null
    private var selectedEndDate: String? = null
    private lateinit var allAirports : List<Airport>
    private lateinit var allCountries : List<Country>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiClient = ApiClient()
        apiHelper = ApiHelper()

        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())

        // Start Date Picker
        binding.startDatePickerButton.setOnClickListener {
            showDatePicker(binding.startDatePickerButton, true)
        }

        // End Date Picker
        binding.endDatePickerButton.setOnClickListener {
            showDatePicker(binding.endDatePickerButton, false)
        }

        binding.search.setOnClickListener {
            val airportName = binding.airportAutoCompleteTextView.text.toString() ?: return@setOnClickListener
            var entityId: String? = null

            for (airport in allAirports) {
                if (airport.name == airportName) {
                    entityId = airport.iata
                    break
                }
            }
            if (entityId != null &&
                binding.budget.text != null &&
                binding.spinner.selectedItem.toString() != "Currency" &&
                binding.secondSpinner.selectedItem != "Continent" &&
                selectedStartDate != null &&
                selectedEndDate != null) {

                val countrySearch = FlighCountriesSearch(
                    fromEntityId = entityId,
                    departDate = selectedStartDate.toString(),
                    returnDate = selectedEndDate.toString(),
                    currency = binding.spinner.selectedItem.toString(),
                    dummy = true
                )
                fetchCountries(countrySearch)

                // Proceed with the countrySearch object (e.g., send a network request, update UI, etc.)
            } else {
                // Handle the case where entityId is null or other conditions are not met
                Toast.makeText(requireContext(), "Please ensure all fields are filled correctly", Toast.LENGTH_SHORT).show()
            }
        }


        // Spinner
        val filterItems = arrayOf("Currency", "USD", "EUR")
        val filterAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, filterItems
        )
        binding.spinner.adapter = filterAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filterItems
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        val continentItems = arrayOf("Continent", "Africa", "Asia")
        val continentAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, continentItems
        )
        binding.secondSpinner.adapter = continentAdapter
        binding.secondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                continentItems
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        fetchAirports()

//        val activities = mutableListOf(
//            Activities(R.drawable.usa, "United States", "From $500"),
//            Activities(R.drawable.usa, "United States", "From $500"),
//            Activities(R.drawable.usa, "United States", "From $500"),
//            Activities(R.drawable.usa, "United States", "From $500")
//        )


    }

    private fun showDatePicker(targetTextView: TextView, isStartDate : Boolean) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                if (isStartDate) {
                    selectedStartDate = formattedDate
                    targetTextView.text = "Start Date: $formattedDate"
                } else {
                    selectedEndDate = formattedDate
                    targetTextView.text = "End Date: $formattedDate"
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun fetchAirports() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val airports = apiClient.getAllAirport()

                withContext(Dispatchers.Main) {
                    allAirports = airports
                    updateAutoCompleteTextViewWithAirports(airports)
                }
            } catch (e: Exception) {
                // Handle any exceptions, e.g., network errors
                Log.e("fetchAirport", "Error: $e")
            }
        }
    }

    private fun fetchCountries(countriesSearch: FlighCountriesSearch) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val countries = apiClient.getAllCountries(countriesSearch)
                Log.d("Countries", "$countries")

                withContext(Dispatchers.Main) {
                    allCountries = countries
                    updateCountriesRecyclerView(countries)
                }
            } catch (e: Exception) {
                // Handle any exceptions, e.g., network errors
                Log.e("fetchAirport", "Error: $e")
            }
        }
    }

    private fun updateAutoCompleteTextViewWithAirports(airports: List<Airport>) {
        val airportNames = airports.map { it.name ?: "Unknown Airport" }
        val airportAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_dropdown_item_1line, airportNames
        )
        binding.airportAutoCompleteTextView.setAdapter(airportAdapter)
        Log.d("updateAutoCompleteTextViewWithAirports", "AutoCompleteTextView updated with airports: $airportNames")
    }

    private fun updateCountriesRecyclerView(countries: MutableList<Country>) {
//        var displayedCountries = apiHelper.filterCountry(countries)
       countries.map {
           binding.recyclerViewActivities.adapter = CountryAdapter(countries)
       }
    }
}
