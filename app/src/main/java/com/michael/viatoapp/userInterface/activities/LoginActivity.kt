package com.michael.viatoapp.userInterface.activities


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.viatoapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val myPreferences: SharedPreferences by lazy {
        getSharedPreferences("myPref", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        navigateToHomeIfLoginActive()
        setOnClickListeners()
    }

    private fun navigateToHomeIfLoginActive() {
        val uid = myPreferences.getString("uid", null)
        if (uid != null) {
            val intent = Intent(this, MainNavigationActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
    }

    private fun setOnClickListeners() {
        binding.loginButton.setOnClickListener {
            executeLogin()
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

    private fun executeLogin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be Empty", Toast.LENGTH_SHORT).show()
            return
        }

        binding.loginButton.isEnabled = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val uid = auth.currentUser?.uid

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    myPreferences.edit {
                        putString("uid", uid)
                    }
                    val intent = Intent(this, MainNavigationActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    binding.loginButton.isEnabled = true
                }
            }

        binding.loginButton.isEnabled = true
    }

}