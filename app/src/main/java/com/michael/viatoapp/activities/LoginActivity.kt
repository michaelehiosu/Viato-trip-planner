package com.michael.viatoapp.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.michael.viatoapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

//            if (isValidCredentials(email, password)) {
//                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
//            }

            val intent = Intent(this, MainNavigationActivity::class.java)
            startActivity(intent)
        }

        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        // This for me to be able to check if login works, this will be deleted later
        val validEmail = "jameslennox95@gmai.com"
        val validPassword = "123"

        return email == validEmail && password == validPassword
    }

}