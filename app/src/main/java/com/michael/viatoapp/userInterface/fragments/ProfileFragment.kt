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
import android.widget.Button
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityProfileBinding
import com.michael.viatoapp.userInterface.activities.LoginActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: ActivityProfileBinding
    private val sharedPreferences: SharedPreferences by lazy { requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                requireActivity().finish() // Optional: close the current activity
                alertDialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.button_cancel).setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }
}
