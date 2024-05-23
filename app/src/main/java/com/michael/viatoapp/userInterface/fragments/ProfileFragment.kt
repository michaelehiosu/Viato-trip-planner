package com.michael.viatoapp.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.viatoapp.databinding.ActivityProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding : ActivityProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}