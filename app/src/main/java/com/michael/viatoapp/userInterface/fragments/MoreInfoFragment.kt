package com.michael.viatoapp.userInterface.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityMoreInfoBinding

class MoreInfoFragment : Fragment() {
    private lateinit var binding: ActivityMoreInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMoreInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}