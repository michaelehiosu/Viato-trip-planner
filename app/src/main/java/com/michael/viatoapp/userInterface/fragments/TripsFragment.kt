package com.michael.viatoapp.userInterface.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.michael.viatoapp.model.response.Activities
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityTripsBinding
import com.michael.viatoapp.userInterface.adapter.CountryAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class TripsFragment : Fragment() {
    private lateinit var binding: ActivityTripsBinding
    private val calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(requireContext())

        // Start Date Picker
        binding.startDatePickerButton.setOnClickListener {
            showDatePicker(binding.startDatePickerButton)
        }

        // End Date Picker
        binding.endDatePickerButton.setOnClickListener {
            showDatePicker(binding.endDatePickerButton)
        }

        // Spinner
        val filterItems = arrayOf("$", "USD", "EURO")
        val filterAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, filterItems
        )
        binding.spinner.adapter = filterAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                continentItems
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        val airportItem = arrayOf("Airport", "Schiphol", "Abuja", "Istanbul")
        val airportAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, airportItem
        )
        binding.airportLayout.adapter = continentAdapter

        val activities = mutableListOf(
            Activities(R.drawable.usa, "United States", "From $500"),
            Activities(R.drawable.usa, "United States", "From $500"),
            Activities(R.drawable.usa, "United States", "From $500"),
            Activities(R.drawable.usa, "United States", "From $500")
        )

        binding.recyclerViewActivities.adapter = CountryAdapter(activities)
    }


    private fun showDatePicker(targetTextView: TextView) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                targetTextView.text = "Selected Date: $formattedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
