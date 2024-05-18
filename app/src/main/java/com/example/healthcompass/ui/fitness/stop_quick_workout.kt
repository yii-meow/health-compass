package com.example.healthcompass.ui.fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R

class stop_quick_workout : Fragment() {
    private val args by navArgs<stop_quick_workoutArgs>()

    private lateinit var tvDuration: TextView
    private lateinit var btnResume: Button
    private lateinit var btnDone: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stop_quick_workout, container, false)

        tvDuration = view.findViewById(R.id.tvDuration)
        btnResume = view.findViewById(R.id.btnResume)
        btnDone = view.findViewById(R.id.btnDone)

        val tvWorkoutTimerActivityName : TextView = view.findViewById(R.id.tvWorkoutTimerActivityName)
        tvWorkoutTimerActivityName.text = args.activity

        val duration = args.duration

        tvDuration.text = getFormattedTime(duration)

        btnResume.setOnClickListener {
            val action = stop_quick_workoutDirections.actionStopQuickWorkoutToQuickWorkoutTimer()
            action.duration = args.duration
            findNavController().navigate(action)
        }

        btnDone.setOnClickListener {
            val action = stop_quick_workoutDirections.actionStopQuickWorkoutToDoneQuickWorkout()
            action.duration = args.duration
            action.activity = args.activity
            findNavController().navigate(action)
        }

        return view
    }

    private fun getFormattedTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }
}