package com.michael.viatoapp.userInterface.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityProfileBinding
import com.michael.viatoapp.userInterface.activities.LoginActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val sharedPreferences: SharedPreferences by lazy { requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

                val currencyItems = arrayOf("Euro", "USD")
                val currencyAdapter = ArrayAdapter(
                    requireContext(), R.layout.spinner_item, currencyItems
                )
                currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.tvCurrency.adapter = currencyAdapter

                val continentItems = arrayOf("Europe", "Africa", "Asia", "Oceania", "North America", "South America")
                val continentAdapter = ArrayAdapter(
                    requireContext(), R.layout.spinner_item, continentItems
                )
                continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.tvFavDestination.adapter = continentAdapter

            }

            binding.buttonLogout.setOnClickListener {
                val context = requireContext()
                val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_alert_dialog, null)

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

            binding.buttonEditPassword.setOnClickListener {
                binding.tvPassword.isEnabled = true
                binding.buttonSave.visibility = View.VISIBLE
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
                val newPassword = binding.tvPassword.text.toString()

                val updates = hashMapOf<String, Any>(
                    "currency" to newCurrency,
                    "airport" to newAirport,
                    "destination" to newDestination
                )

                if (newPassword.isNotEmpty()) {
                    updates["password"] = newPassword
                }

                document.update(updates).addOnSuccessListener {
                    binding.tvCurrency.isEnabled = false
                    binding.tvAirport.isEnabled = false
                    binding.tvFavDestination.isEnabled = false
                    binding.tvPassword.isEnabled = false
                }
            }
        }
    }

}