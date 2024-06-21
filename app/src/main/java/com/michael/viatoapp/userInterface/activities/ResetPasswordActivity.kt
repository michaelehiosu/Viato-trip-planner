package com.michael.viatoapp.userInterface.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.michael.viatoapp.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        setCickListeners()
    }

    private fun setCickListeners() {
        binding.sendButton.setOnClickListener {
            sendResetLink()
        }
    }
    
    private fun sendResetLink() {
        val email = binding.emailEditText.text.toString()

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Reset email sent", Toast.LENGTH_LONG).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to send reset email", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error Occurred: ${exception.message}", Toast.LENGTH_LONG).show()
        }
    }
}
