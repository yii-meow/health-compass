package com.example.healthcompass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner

class edit_goal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_goal)

        val spinner : Spinner = findViewById(R.id.spinnerSetGoal)

    }
}