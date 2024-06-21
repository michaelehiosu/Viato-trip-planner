package com.michael.viatoapp.userInterface.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.viatoapp.R
import com.michael.viatoapp.api.ApiClient
import com.michael.viatoapp.databinding.ActivityProfileBinding
import com.michael.viatoapp.model.data.flights.Airport
import com.michael.viatoapp.userInterface.activities.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var allAirports: List<Airport>
    private lateinit var apiClient: ApiClient
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences(
            "myPref",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiClient = ApiClient()
        fetchAirportsForProfile()
    }

    override fun onResume() {
        super.onResume()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val uid = sharedPreferences.getString("uid", null) ?: ""

        val document = firestore.collection("users").document(uid)
        document.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val firstName = documentSnapshot.getString("firstName")
                val lastName = documentSnapshot.getString("lastName")
                val email = documentSnapshot.getString("email")
                val currency = documentSnapshot.getString("currency")
                val airport = documentSnapshot.getString("airport")
                val destination = documentSnapshot.getString("destination")

                val fullName = "$firstName $lastName"

                binding.nameText.setText(fullName)
                binding.tvEmail.setText(email)
                binding.tvAirport.setText(airport)

                val currencyItems = arrayOf("EUR", "USD")
                val currencyAdapter = ArrayAdapter(
                    requireContext(), R.layout.spinner_item, currencyItems
                )
                currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.tvCurrency.adapter = currencyAdapter
                binding.tvCurrency.isEnabled = false

                val continentItems =
                    arrayOf("Europe", "Africa", "Asia", "Oceania", "North America", "South America")
                val continentAdapter = ArrayAdapter(
                    requireContext(), R.layout.spinner_item, continentItems
                )
                continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.tvFavDestination.adapter = continentAdapter
                binding.tvFavDestination.isEnabled = false

                val currencyIndex = currencyItems.indexOf(currency)
                val destinationIndex = continentItems.indexOf(destination)
                if (currencyIndex != -1) {
                    binding.tvCurrency.setSelection(currencyIndex)
                }
                if (destinationIndex != -1) {
                    binding.tvFavDestination.setSelection(destinationIndex)
                }

            }

            binding.buttonLogout.setOnClickListener {
                val context = requireContext()
                val dialogView =
                    LayoutInflater.from(context).inflate(R.layout.activity_alert_dialog, null)

                val alertDialog = AlertDialog.Builder(context)
                    .setView(dialogView)
                    .create()

                dialogView.findViewById<Button>(R.id.button_logout).setOnClickListener {
                    sharedPreferences.edit {
                        clear()
                        apply()
                    }
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    alertDialog.dismiss()
                }

                dialogView.findViewById<Button>(R.id.button_cancel).setOnClickListener {
                    alertDialog.dismiss()
                }

                alertDialog.show()
            }

            binding.buttonEditPreferences.setOnClickListener {
                binding.tvCurrency.isEnabled = true
                binding.tvAirport.isEnabled = true
                binding.tvFavDestination.isEnabled = true
                binding.buttonSave.visibility = View.VISIBLE
            }

            binding.buttonSave.setOnClickListener {
                val newCurrency = binding.tvCurrency.selectedItem.toString()
                val newAirport = binding.tvAirport.text.toString()
                val newDestination = binding.tvFavDestination.selectedItem.toString()

                val updates = hashMapOf<String, Any>(
                    "currency" to newCurrency,
                    "airport" to newAirport,
                    "destination" to newDestination
                )

                document.update(updates).addOnSuccessListener {
                    binding.tvCurrency.isEnabled = false
                    binding.tvAirport.isEnabled = false
                    binding.tvFavDestination.isEnabled = false
                    binding.buttonSave.visibility = View.GONE
                }
            }
        }
    }

    private fun fetchAirportsForProfile() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val airports = apiClient.getAllAirport()
                withContext(Dispatchers.Main) {
                    allAirports = airports
                    setupAirportEditText() // Call setupAirportEditText here
                }
            } catch (e: Exception) {
                Log.e("fetchAirportsForProfile", "Error: $e")
            }
        }
    }

    private fun setupAirportEditText() {
        val airportNames = allAirports.map { it.name }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            airportNames
        )
        (binding.tvAirport as? AutoCompleteTextView)?.setAdapter(adapter)

        val handler = Handler(Looper.getMainLooper())
        var isTextChanging = false

        binding.tvAirport.addTextChangedListener(object : TextWatcher {
            private var lastInput: String? = null

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                if (isTextChanging) return

                val enteredText = s.toString()
                if (enteredText == lastInput) return

                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    if (!airportNames.contains(enteredText)) {
                        isTextChanging = true
                        binding.tvAirport.setText("") // Clear invalid input
                        binding.tvAirport.error = "Please select a valid airport"
                        Toast.makeText(
                            requireContext(),
                            "Please select a valid airport",
                            Toast.LENGTH_SHORT
                        ).show()
                        isTextChanging = false
                    }
                    lastInput = enteredText
                }, 10000) // 10 second delay to check after typing stops
            }
        })
    }
}