package com.example.healthcompass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nutritionView = findViewById<CardView>(R.id.cardDailyIntake)
        val fitnessRoutinesView = findViewById<CardView>(R.id.cardFitnessRoutines)

        var tvDate: TextView = findViewById(R.id.tvDate)

        // Get the current date
        val currentDate = Date()

        // Define a date format
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Format the current date
        val formattedDate = dateFormat.format(currentDate)

        // Set the formatted date to the TextView
        tvDate.text = formattedDate


        nutritionView.setOnClickListener {
            val intent = Intent(this, NutritionActivity::class.java)
            startActivity(intent)
        }

        fitnessRoutinesView.setOnClickListener {
            val intent = Intent(this,FitnessActivity::class.java)
            startActivity(intent)
        }
    }
}