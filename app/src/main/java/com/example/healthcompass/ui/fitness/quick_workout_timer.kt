package com.example.healthcompass.ui.fitness

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R

class quick_workout_timer : Fragment() {
    private val args by navArgs<stop_quick_workoutArgs>()

    private lateinit var tvTimerDuration: TextView
    private lateinit var btnStop: Button

    private var seconds = 0
    private var running = false

    private val handler = Handler(Looper.getMainLooper())
    private val updateTimer = object : Runnable {
        override fun run() {
            if (running) {
                seconds++
                tvTimerDuration.text = getFormattedTime(seconds)
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quick_workout_timer, container, false)

        tvTimerDuration = view.findViewById(R.id.tvTimerDuration)

        val tvWorkoutTimerActivityName : TextView = view.findViewById(R.id.tvWorkoutTimerActivityName)
        tvWorkoutTimerActivityName.text = args.activity

        btnStop = view.findViewById(R.id.btnStop)

        btnStop.setOnClickListener {
            running = false
            val action = quick_workout_timerDirections.actionQuickWorkoutTimerToStopQuickWorkout(seconds)
            action.activity = args.activity
            findNavController().navigate(action)
        }

        seconds = args.duration

        startTimer()


        return view
    }

    private fun startTimer() {
        running = true
        handler.post(updateTimer)
    }

    private fun getFormattedTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        running = false
        handler.removeCallbacks(updateTimer)
    }
}