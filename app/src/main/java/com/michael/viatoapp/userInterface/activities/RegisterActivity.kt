package com.michael.viatoapp.userInterface.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.michael.viatoapp.databinding.ActivityRegisterBinding
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.viatoapp.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.registerButton.setOnClickListener {
            userRegistration()
        }

        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userRegistration() {
        binding.registerButton.isEnabled = false

        val firstName = binding.firstNameEditText.text.toString().trim()
        val lastName = binding.lastNameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
        val defaultCurrency = "EUR"
        val defaultAirport = "Amsterdam Airport Schiphol"
        val defaultDestination = "Europe"
        val profileUrl = R.drawable.profile_pic.toString()

        if (validateInputData(firstName, lastName, email, password, confirmPassword)) {
            if (validateEmail(email)) {
                if (password == confirmPassword){
                    if (isPasswordValid(password)){
                        auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val  uid = user?.uid

                                //Creating a document with UID
                                val userDocument = firestore.collection("users").document(uid!!)
                                val userData = hashMapOf(
                                    "firstName" to firstName,
                                    "lastName" to lastName,
                                    "email" to email,
                                    "currency" to defaultCurrency,
                                    "airport" to defaultAirport,
                                    "destination" to defaultDestination,
                                    "profileUrl" to profileUrl
                                )

                                userDocument.set(userData)
                                    .addOnSuccessListener {
                                        showSuccessToast("Account Registered.")
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                    }.addOnFailureListener {
                                        showErrorToast("Registration Failed.")
                                    }
                            } else {
                                showErrorToast("Authentication failed")
                            }
                        }
                    }
                    else {
                        showErrorToast("Password must be at least 8 characters long and contain at least one uppercase letter and one lowercase letter")
                    }
                }else{
                    showErrorToast("Passwords do not match")
                }
            } else {
                showErrorToast("Invalid Email")
            }
        } else {
            showErrorToast("Please fill all fields")
        }

        binding.registerButton.isEnabled = true
    }

    private fun validateInputData(firstName: String, lastName: String, email: String, password: String, confirmPassword: String): Boolean {
        return firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    private fun validateEmail(email: String): Boolean {
        // Check if email is not empty and ends with ".com"
        return email.isNotEmpty() && email.takeLast(4) == ".com"
    }

    private fun isPasswordValid(password: String): Boolean {
        val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$".toRegex()
        return pattern.matches(password)
    }

    private fun showErrorToast(message: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.activity_toast_layout, findViewById(R.id.custom_toast_container))

        val textView = layout.findViewById<TextView>(R.id.text_view_toast)
        textView.text = message

        val imageView = layout.findViewById<ImageView>(R.id.image_view_icon)
        imageView.setImageResource(R.drawable.ic_error)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    private fun showSuccessToast(message: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.activity_toast_success, findViewById(R.id.custom_toast_container))

        val textView = layout.findViewById<TextView>(R.id.text_view_toast)
        textView.text = message

        val imageView = layout.findViewById<ImageView>(R.id.image_view_icon)
        imageView.setImageResource(R.drawable.ic_success)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}

