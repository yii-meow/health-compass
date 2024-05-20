package com.example.healthcompass

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.healthcompass.ui.fitness.fitness_routines
import com.example.healthcompass.ui.nutrition.nutrition
import com.example.healthcompass.ui.profile.LoginActivity
import com.example.healthcompass.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = getUsername()
        checkHasUsername(user)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_summary -> {
                    findNavController(R.id.fragmentContainer).navigate(R.id.action_global_summary)
                    true
                }

                R.id.bottom_fitness -> {
                    findNavController(R.id.fragmentContainer).navigate(R.id.action_global_fitness_routines)
                    true
                }

                R.id.bottom_nutrition -> {
                    findNavController(R.id.fragmentContainer).navigate(R.id.action_global_nutrition)
                    true
                }

                R.id.bottom_profile -> {
                    findNavController(R.id.fragmentContainer).navigate(R.id.action_global_profile)
                    true
                }

                else -> false
            }
        }
    }

    private fun checkHasUsername(user: String?) {
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUsername(): String? {
        val sharedPref: SharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}