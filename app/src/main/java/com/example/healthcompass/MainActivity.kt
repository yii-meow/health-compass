package com.example.healthcompass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fitness_routines)

//        val btnEditGoal: Button = findViewById(R.id.btnEditGoal)
//
//        btnEditGoal.setOnClickListener {
//            val intent = Intent(this,edit_goal::class.java)
//            startActivity(intent)
//        }





//        var tvDate: TextView = findViewById(R.id.tvDate)
//        tvDate.text = "123"
//
//        // Get the current date
//        val currentDate = Date()
//
//        // Define a date format
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//
//        // Format the current date
//        val formattedDate = dateFormat.format(currentDate)
//
//        // Set the formatted date to the TextView
//        tvDate.text = formattedDate
    }
}