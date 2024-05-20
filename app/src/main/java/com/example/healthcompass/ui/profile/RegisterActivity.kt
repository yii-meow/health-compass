package com.example.healthcompass.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.healthcompass.MainActivity
import com.example.healthcompass.data.User.UserClass
import com.example.healthcompass.databinding.ActivityRegisterBinding
import com.example.healthcompass.ui.profile.LoginActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = getUsername()
        checkHasUsername(user)

        binding.btnRegister.setOnClickListener { register() }
        binding.txtLogin.setOnClickListener{ navToLogin() }
    }

    private fun navToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun register() {
        val username : String = binding.txtUsername.text.toString().trim()

        if (username.isNotEmpty()) {
            database = FirebaseDatabase.getInstance().getReference("Users")

            database.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!(snapshot.exists())) {
                        if (checkName() && checkPassword() && checkAge() && checkGender() && checkHeight() && checkWeight()) {
                            val name : String = binding.txtName.text.toString()
                            val password : String = binding.txtPassword.text.toString().trim()
                            val age : Int = binding.txtAge.text.toString().toInt()
                            val gender : String = binding.spnGender.selectedItem.toString()
                            val height : Float = binding.txtHeight.text.toString().toFloat()
                            val weight : Float = binding.txtWeight.text.toString().toFloat()

                            // Setting user default milestones
                            val milestones = mapOf(
                                "badminton" to mapOf(
                                    "3_times" to false,
                                    "7_times" to false,
                                    "30_times" to false
                                ),
                                "walking" to mapOf(
                                    "3_times" to false,
                                    "25_times" to false,
                                    "50_times" to false
                                ),
                                "running" to mapOf(
                                    "3_times" to false,
                                    "25_times" to false,
                                    "50_times" to false
                                )
                            )

                            val userClass = UserClass(name, username, password, gender, age, height, weight,milestones)
                            database = FirebaseDatabase.getInstance().getReference("Users")
                            database.child(username).child("Profile Information").setValue(userClass)

                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        binding.txtUsername.error = "Username already exists"
                        binding.txtUsername.requestFocus()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@RegisterActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            binding.txtUsername.error = "Username cannot be empty"
            binding.txtUsername.requestFocus()
        }
    }

    private fun checkName() : Boolean {
        val name : String = binding.txtName.text.toString()

        if (name.isEmpty()) {
            binding.txtName.error = "Name cannot be empty"
            binding.txtName.requestFocus()
            return false
        }

        if (name.any { it.isDigit() }) {
            binding.txtName.error = "Name cannot have digits"
            binding.txtName.requestFocus()
            return false
        }

        return true
    }

    private fun checkPassword() : Boolean {
        val password : String = binding.txtPassword.text.toString().trim()
        val confirmPassword : String = binding.txtConfirmPassword.text.toString().trim()

        var hasChar = false
        var hasDigit = false
        var hasSpecialChar = false

        if (password.isEmpty()) {
            binding.txtPassword.error = "Password cannot be empty"
            binding.txtPassword.requestFocus()
            return false
        }

        if (password.length < 8) {
            binding.txtPassword.error = "Password length must at least 8"
            binding.txtPassword.requestFocus()
            return false
        }

        for (char in password) {
            if (char.isLetter()) {
                hasChar = true
            } else if (char.isDigit()) {
                hasDigit = true
            } else if (char in "!@#$%^&*()_+-={}[]|\\:;\"'<>,.?/") {
                hasSpecialChar = true
            }
        }

        if (!(hasChar) || !(hasDigit) || !(hasSpecialChar)) {
            binding.txtPassword.error = "Password must contains at least 1 character, 1 digit, and 1 special character"
            binding.txtPassword.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            binding.txtConfirmPassword.error = "Confirm password cannot be empty"
            binding.txtConfirmPassword.requestFocus()
            return false
        }

        if (confirmPassword != password) {
            binding.txtConfirmPassword.error = "Confirm password must be same with password"
            binding.txtConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    private fun checkAge() : Boolean {
        val ageString : String = binding.txtAge.text.toString()

        if (ageString.isEmpty()) {
            binding.txtAge.error = "Age cannot be empty"
            binding.txtAge.requestFocus()
            return false
        }

        val age : Int = binding.txtAge.text.toString().toInt()

        if (age <= 0) {
            binding.txtAge.error = "Age must larger than 0"
            binding.txtAge.requestFocus()
            return false
        }

        return true
    }

    private fun checkGender() : Boolean {
        val gender : String = binding.spnGender.selectedItem.toString()

        if (gender == "Gender") {
            (binding.spnGender.selectedView as? TextView)?.error = "Select a gender"
            binding.spnGender.requestFocus()
            return false
        }

        return true
    }

    private fun checkHeight() : Boolean {
        val heightString : String = binding.txtHeight.text.toString()

        if (heightString.isEmpty()) {
            binding.txtHeight.error = "Height cannot be empty"
            binding.txtHeight.requestFocus()
            return false
        }

        val height : Float = binding.txtHeight.text.toString().toFloat()

        if (height <= 0) {
            binding.txtHeight.error = "Height must larger than 0"
            binding.txtHeight.requestFocus()
            return false
        }

        return true
    }

    private fun checkWeight() : Boolean {
        val weightString : String = binding.txtWeight.text.toString()

        if (weightString.isEmpty()) {
            binding.txtWeight.error = "Weight cannot be empty"
            binding.txtWeight.requestFocus()
            return false
        }

        val weight : Float = binding.txtWeight.text.toString().toFloat()

        if (weight <= 0) {
            binding.txtWeight.error = "Weight must larger than 0"
            binding.txtWeight.requestFocus()
            return false
        }

        return true
    }

    private fun checkHasUsername(user : String?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUsername() : String? {
        val sharedPref : SharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}