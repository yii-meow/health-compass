package com.example.healthcompass

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthcompass.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = getUsername()
        checkHasUsername(user)

        binding.btnLogOut.setOnClickListener { logOut() }
    }

    private fun logOut() {
        val sharedPref : SharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", null)
        editor.apply()

        val user = getUsername()
        checkHasUsername(user)
    }

    private fun checkHasUsername(user : String?) {
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUsername() : String? {
        val sharedPref : SharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}