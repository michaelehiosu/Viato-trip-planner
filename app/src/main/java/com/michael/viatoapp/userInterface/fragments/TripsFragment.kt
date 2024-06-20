package com.michael.viatoapp.userInterface.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.viatoapp.R
import com.michael.viatoapp.api.ApiClient
import com.michael.viatoapp.api.ApiHelper
import com.michael.viatoapp.databinding.ActivityTripsBinding
import com.michael.viatoapp.model.data.flights.Airport
import com.michael.viatoapp.model.data.flights.Country
import com.michael.viatoapp.model.request.flights.FlightCountriesSearch
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
    private lateinit var allAirports: List<Airport>
    private lateinit var allCountries: List<Country>
    private var isFlightPressed = false
    private var isHotelPressed = false
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityTripsBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiClient = ApiClient()
        apiHelper = ApiHelper()

        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())
        binding.textActivity.text = "Suggested Countries"

        // Start Date Picker
        binding.startDatePickerButton.setOnClickListener {
            showDatePicker(binding.startDatePickerButton, true)
        }

        // End Date Picker
        binding.endDatePickerButton.setOnClickListener {
            showDatePicker(binding.endDatePickerButton, false)
        }

        binding.airplane.setOnClickListener {
            val airplane = binding.airplane
            toggleButtonPressed(isFlightPressed, airplane)
            // Toggle the state
            isFlightPressed = !isFlightPressed
        }

        binding.hotel.setOnClickListener {
            val hotel = binding.hotel
            toggleButtonPressed(isHotelPressed, hotel)
            // Toggle the state
            isHotelPressed = !isHotelPressed
        }

        binding.search.setOnClickListener {
            hideKeyboard()
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

                val countrySearch = FlightCountriesSearch(
                    fromEntityId = entityId,
                    departDate = selectedStartDate.toString(),
                    returnDate = selectedEndDate.toString(),
                    currency = binding.spinner.selectedItem.toString(),
                    dummy = true
                )
                fetchCountries(countrySearch)

            } else {
                Toast.makeText(requireContext(), "Please ensure all fields are filled correctly", Toast.LENGTH_SHORT).show()
            }
        }


        // Spinner
        val filterItems = arrayOf("EUR", "USD")
        val filterAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_item, filterItems
        )
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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

        val continentItems = arrayOf("Europe", "Africa", "Asia", "Oceania", "North America", "South America")
        val continentAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_item, continentItems
        )
        continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

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
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showDatePicker(targetTextView: Button, isStartDate: Boolean) {
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
                    targetTextView.text = "From: $formattedDate"
                    targetTextView.setBackgroundColor(resources.getColor(R.color.orange))
                    targetTextView.setTextColor(resources.getColor(R.color.white))
                } else {
                    val endDate = selectedDate.timeInMillis
                    val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedStartDate)?.time ?: 0
                    if (endDate >= startDate) {
                        selectedEndDate = formattedDate
                        targetTextView.text = "To: $formattedDate"
                        targetTextView.setBackgroundColor(resources.getColor(R.color.orange))
                        targetTextView.setTextColor(resources.getColor(R.color.white))
                    } else {
                        Toast.makeText(requireContext(), "End date must be after start date", Toast.LENGTH_SHORT).show()
                    }

                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun fetchAirports() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val airports = apiClient.getAllAirport()

                withContext(Dispatchers.Main) {
                    allAirports = airports
                    fetchUserData()
                    updateAutoCompleteTextViewWithAirports(airports)
                }
            } catch (e: Exception) {
                // Handle any exceptions, e.g., network errors
                Log.e("fetchAirport", "Error: $e")
            }
        }
    }

    private fun fetchCountries(countriesSearch: FlightCountriesSearch) {
        val budget = binding.budget.text.toString()
        val continent = binding.secondSpinner.selectedItem.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val countries = apiClient.getAllCountries(countriesSearch)
                Log.d("Countries", "$countries")

                withContext(Dispatchers.Main) {
                    allCountries = countries
                    val filterCountries = apiHelper.filterCountry(countries, budget, continent)
                    Log.d("FilteredCountries", "$filterCountries")
                    updateCountriesRecyclerView(filterCountries, countriesSearch)

                    binding.textActivity.text = "Resulted Countries"
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

    private fun updateCountriesRecyclerView(countries: MutableList<Country>, countriesSearch: FlightCountriesSearch) {
        countries.map {
            binding.recyclerViewActivities.adapter = CountryAdapter(countries, countriesSearch)
        }
    }

    private fun toggleButtonPressed(condition : Boolean, button : Button) {
        if (condition) {
            button.setBackgroundColor(resources.getColor(R.color.white))
            button.setTextColor(resources.getColor(R.color.black))
        } else {
            button.setBackgroundColor(resources.getColor(R.color.orange))
            button.setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun fetchStoredCountries(destination: String, currency: String, airport: String) {
        val budget = "1500"
        val continent = destination

        val airportName = airport
        var entityId: String? = null

        for (airport in allAirports) {
            if (airport.name == airportName) {
                entityId = airport.iata
                break
            }
        }

        // Calculate dates
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.WEEK_OF_YEAR, 2)
        val departDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        val returnDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        val countrySearch = FlightCountriesSearch(
            fromEntityId = entityId.toString(), // Provide default values
            departDate = departDate,
            returnDate = returnDate,
            currency = currency,
            dummy = true
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val countries = apiClient.getAllCountries(countrySearch)
                Log.d("Countries", "$countries")

                withContext(Dispatchers.Main) {
                    allCountries = countries
                    val filterCountries = apiHelper.filterCountry(countries, budget, continent)
                    Log.d("FilteredCountries", "$filterCountries")
                    updateCountriesRecyclerView(filterCountries, countrySearch)
                }
            } catch (e: Exception) {
                // Handle any exceptions, e.g., network errors
                Log.e("fetchAirport", "Error: $e")
            }
        }
    }


    private fun fetchUserData() {
        val uid = sharedPreferences.getString("uid", null) ?: ""

        val document = firestore.collection("users").document(uid)
        document.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val destination = documentSnapshot.getString("destination")
                val currency = documentSnapshot.getString("currency")
                val airport = documentSnapshot.getString("airport")
                val name = documentSnapshot.getString("lastName")

                Log.d("UserData", "Destination: $destination")
                Log.d("UserData", "Currency: $currency")
                Log.d("UserData", "Airport: $airport")
                Log.d("UserData", "Name: $name")

                // Fetch stored countries after user data has been fetched
                if (destination != null && currency != null && airport !=null) {
                    fetchStoredCountries(destination,currency,airport)
                }
            } else {
                Log.d("UserData", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("UserData", "get failed with ", exception)
            }
    }
}
