package com.michael.viatoapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michael.viatoapp.databinding.ActivityCountryOverviewBinding

class CountryOverviewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCountryOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}