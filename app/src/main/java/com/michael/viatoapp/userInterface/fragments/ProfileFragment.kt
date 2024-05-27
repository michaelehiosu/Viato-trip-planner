package com.michael.viatoapp.userInterface.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.michael.viatoapp.databinding.ActivityProfileBinding
import com.michael.viatoapp.userInterface.activities.LoginActivity

class ProfileFragment : Fragment() {
    private lateinit var binding : ActivityProfileBinding
    private val sharedPreferences: SharedPreferences by lazy { requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogout.setOnClickListener {
            sharedPreferences.edit{
                clear()
                apply()
            }
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}