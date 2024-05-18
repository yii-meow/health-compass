package com.example.healthcompass.ui.fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R
import com.example.healthcompass.data.FitnessActivity.FitnessActivity
import com.example.healthcompass.data.FitnessActivity.FitnessActivityViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class done_quick_workout : Fragment() {
    private val args by navArgs<done_quick_workoutArgs>()
    private lateinit var fitnessViewModel: FitnessActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_done_quick_workout, container, false)

        val duration = args.duration

        val tvStartTime: TextView = view.findViewById(R.id.tvStartTime)
        val tvDuration: TextView = view.findViewById(R.id.tvDuration)

        tvDuration.text = getFormattedTime(duration)

        val currentTimeInMillis = System.currentTimeMillis()
        val startTimeInMillis = currentTimeInMillis - duration * 1000
        val startTime = Calendar.getInstance()
        startTime.timeInMillis = startTimeInMillis

        val endTime = Calendar.getInstance()
        endTime.timeInMillis = currentTimeInMillis

        tvStartTime.text = getFormattedDateTime(startTime)

        val btnSave: Button = view.findViewById(R.id.btnSaveQuickWorkout)

        btnSave.setOnClickListener {
            val activityName = args.activity
            val activityDate = tvStartTime.text.toString().substring(0, 10)
            val startTime = tvStartTime.text.toString().substring(11,16)
            val endTime = getFormattedDateTime(endTime)
            val duration = tvDuration.text.toString()

            val parts = duration.split(":")
            val hours = parts[0].toDouble() + parts[1].toDouble() / 60.0

            val caloriesPerKgHour = 35
            val caloriesBurnt = (caloriesPerKgHour * hours).toInt()

            val fitnessActivity = FitnessActivity(
                activityName,
                activityDate,
                caloriesBurnt,
                startTime,
                endTime,
                duration
            )
            saveQuickWorkout(fitnessActivity)
        }

        return view
    }

    private fun saveQuickWorkout(fitnessActivity: FitnessActivity) {
        fitnessViewModel = ViewModelProvider(this).get(FitnessActivityViewModel::class.java)
        Toast.makeText(requireContext(),"$fitnessActivity",Toast.LENGTH_LONG).show()
        fitnessViewModel.postWorkoutRecord(fitnessActivity)

        val action = done_quick_workoutDirections.actionDoneQuickWorkoutToFitnessRoutinesDetails()
        action.fitnessDay = fitnessActivity.activityDate
        action.fitnessTime = fitnessActivity.startTime
        findNavController().navigate(action)
    }

    private fun getFormattedDateTime(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun getFormattedTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }
}