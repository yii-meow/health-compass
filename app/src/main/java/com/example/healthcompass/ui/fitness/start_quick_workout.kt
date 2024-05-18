package com.example.healthcompass.ui.fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R
import com.example.healthcompass.ui.nutrition.previous_day_nutritionArgs

class start_quick_workout : Fragment() {
    private val args by navArgs<start_quick_workoutArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_start_quick_workout, container, false)

        val btnStart: Button = view.findViewById(R.id.btnStart)

        val tvActivity: TextView = view.findViewById(R.id.tvStartWorkoutActivityName)
        val imgQuickWorkout: ImageView = view.findViewById(R.id.imgQuickWorkout)

        tvActivity.text = args.activity
        imgQuickWorkout.setBackgroundResource(getQuickWorkoutImage())

        btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_start_quick_workout_to_quick_workout_timer)
        }

        return view
    }

    private fun getQuickWorkoutImage(): Int {
        return when (args.activity) {
            "Running" -> R.drawable.quick_workout_running
            "Walking" -> R.drawable.quick_workout_walking
            "Treadmill" -> R.drawable.quick_workout_treadmill
            else -> R.drawable.quick_workout_cycling
        }
    }
}