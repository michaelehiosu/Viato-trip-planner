package com.michael.viatoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.michael.viatoapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

         binding.registerButton.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString().trim()
            val lastName = binding.lastNameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

//            if (validateInputData(firstName, lastName, email, password)) {
//                if (!validateEmail(email)){
//                    Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
//            }

             val intent = Intent(this, LoginActivity::class.java)
             startActivity(intent)
        }

        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun validateInputData(firstName: String, lastName: String, email: String, password: String): Boolean {
        return firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    private fun validateEmail(email: String): Boolean {
        // Check if email is not empty and ends with ".com"
        return email.isNotEmpty() && email.takeLast(4) == ".com"
    }
}