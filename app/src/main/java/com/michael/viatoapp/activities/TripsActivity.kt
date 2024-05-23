package com.michael.viatoapp.activities

import android.R
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
//import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityTripsBinding
import java.text.SimpleDateFormat
import java.util.Locale


class TripsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTripsBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripsBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Set the content view to the root of the binding

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
            this, R.layout.simple_spinner_dropdown_item, filterItems
        )
/*
        val spinner: Spinner = findViewById(R.id.secondSpinner)
        spinner.adapter = filterAdapter*/

        binding.spinner.adapter = filterAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterItems
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

//        val secondSpinner: Spinner = findViewById(R.id.secondSpinner)

        val continentItems = arrayOf("Continent", "Africa", "Asia")

        val continentAdapter = ArrayAdapter(
            this, R.layout.simple_spinner_dropdown_item, continentItems
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
    }

    private fun showDatePicker(targetTextView: TextView) {
        val datePickerDialog = DatePickerDialog(
            this,
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
