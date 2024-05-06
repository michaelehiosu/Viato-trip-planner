package com.michael.viatoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.viatoapp.databinding.ActivityTripsBinding

class TripsFragment : Fragment() {
    private lateinit var binding : ActivityTripsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityTripsBinding.inflate(inflater, container, false)
        return binding.root
    }
}