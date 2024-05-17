package com.example.healthcompass

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healthcompass.MainActivity
import com.example.healthcompass.RegisterActivity
import com.example.healthcompass.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = getUsername()
        checkHasUsername(user)

        binding.btnLogin.setOnClickListener { login() }
        binding.txtRegister.setOnClickListener { navToRegister() }
    }

    private fun navToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun login() {
        val username : String = binding.txtUsername.text.toString().trim()

        if (username.isNotEmpty()) {
            database = FirebaseDatabase.getInstance().getReference("Users")

            val checkDb: Query = database.orderByChild("username").equalTo(username)
            checkDb.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val password : String = binding.txtPassword.text.toString().trim()
                        val passwordDb : String? = snapshot.child(username).child("password").getValue(String::class.java)

                        if (password == passwordDb) {
                            storeUsername(username)

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            binding.txtPassword.error = "Password invalid"
                            binding.txtPassword.requestFocus()
                        }
                    } else {
                        binding.txtUsername.error = "Username does not exists"
                        binding.txtUsername.requestFocus()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            binding.txtUsername.error = "Username cannot be empty"
            binding.txtUsername.requestFocus()
        }
    }

    private fun storeUsername(username : String) {
        val sharedPref : SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", username)
        editor.apply()
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