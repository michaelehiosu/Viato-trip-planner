package com.michael.viatoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.viatoapp.databinding.ActivityNearbyBinding

class NearbyFragment : Fragment() {
    private lateinit var binding : ActivityNearbyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityNearbyBinding.inflate(inflater, container, false)
        return binding.root
    }
}