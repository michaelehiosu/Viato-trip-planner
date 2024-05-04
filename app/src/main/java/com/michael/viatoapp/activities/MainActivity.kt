package com.michael.viatoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.michael.viatoapp.R
import com.michael.viatoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}