package com.example.healthcompass.ui.fitness

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R
import com.example.healthcompass.data.FitnessActivity.FitnessActivity
import com.example.healthcompass.data.FitnessActivity.FitnessActivityViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar


class log_workout_record : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var tvDatePicker: TextView
    private lateinit var fitnessViewModel: FitnessActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_workout_record, container, false)

        val fitnessActivitiesSpinner = view.findViewById<Spinner>(R.id.spinnerFitnessActivities)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.fitnessActivities,
            R.layout.custom_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        fitnessActivitiesSpinner.adapter = adapter

        val tvDurationPicker: TextView = view.findViewById(R.id.durationPicker)
        tvDatePicker = view.findViewById(R.id.datePicker)
        val tvStartTimePicker: TextView = view.findViewById(R.id.startTimePicker)

        // Create an instance of OnDurationSetListener
        val onDurationSetListener = object : DurationPickerDialogFragment.OnDurationSetListener {
            override fun onDurationSet(hours: Int, minutes: Int, seconds: Int) {
                // Handle the duration set event here
                val durationString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                tvDurationPicker.text = durationString
            }
        }

        // Duration
        tvDurationPicker.setOnClickListener {
            // Create and show the duration picker dialog
            val dialog = DurationPickerDialogFragment.newInstance()
            dialog.setOnDurationSetListener(onDurationSetListener)
            dialog.show(parentFragmentManager, "DurationPickerDialog")
        }

        // Date
        tvDatePicker.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        // Start Time
        tvStartTimePicker.setOnClickListener {
            val c = Calendar.getInstance()

            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { view, hourOfDay, minute ->
                    val formattedHour = String.format("%02d", hourOfDay)
                    val formattedMinute = String.format("%02d", minute)
                    tvStartTimePicker.setText("$formattedHour:$formattedMinute")
                },
                hour,
                minute,
                false
            )

            timePickerDialog.show()
        }

        val btnLogRecord: Button = view.findViewById(R.id.btnLogRecord)

        btnLogRecord.setOnClickListener {
            // Default is Running
            val activityName: String =
                fitnessActivitiesSpinner.selectedItem.toString()

            val duration: String = tvDurationPicker.text.toString()
            val activityDate: String = tvDatePicker.text.toString()
            val tvStartTime: String = tvStartTimePicker.text.toString()

            var validation = true

            if (duration == "Pick duration...") {
                Toast.makeText(
                    requireContext(),
                    "Please fill out the activity duration!",
                    Toast.LENGTH_SHORT
                ).show()
                validation = false
            }

            // Validate all details are filled
            if (activityDate == "Choose date...") {
                Toast.makeText(
                    requireContext(),
                    "Please fill out the activity date!",
                    Toast.LENGTH_SHORT
                ).show()
                validation = false
            }

            if (tvStartTime == "Choose time...") {
                Toast.makeText(
                    requireContext(),
                    "Please fill out the activity start time!",
                    Toast.LENGTH_SHORT
                ).show()
                validation = false
            }

            if (validation) {
                val endTime = calculateEndTime(tvStartTime, duration)

                val fitnessActivity = FitnessActivity(
                    activityName,
                    activityDate,
                    calculateWorkoutCalories(duration),
                    tvStartTime,
                    endTime,
                    duration,
                    "" // default note is null
                )
                logWorkoutRecord(fitnessActivity)
            }
        }

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun calculateEndTime(tvStartTime: String, duration: String): String {
        val startTimeParts = tvStartTime.split(":")
        val durationParts = duration.split(":")
        val startHour = startTimeParts[0].toInt()
        val startMinute = startTimeParts[1].toInt()
        val startSecond = if (startTimeParts.size > 2) startTimeParts[2].toInt() else 0

        val durationHour = durationParts[0].toInt()
        val durationMinute = durationParts[1].toInt()
        val durationSecond = if (durationParts.size > 2) durationParts[2].toInt() else 0

        val endSecond = (startSecond + durationSecond) % 60
        val carrySecond = (startSecond + durationSecond) / 60

        val endMinute = (startMinute + durationMinute + carrySecond) % 60
        val carryMinute = (startMinute + durationMinute + carrySecond) / 60

        val endHour = (startHour + durationHour + carryMinute) % 24

        val endTime = String.format("%02d:%02d:%02d", endHour, endMinute, endSecond)

        return endTime
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val formattedMonth = String.format("%02d", month + 1)
        val formattedDayOfMonth = String.format("%02d", dayOfMonth)
        tvDatePicker.text = "$year-$formattedMonth-$formattedDayOfMonth"
    }

    private fun logWorkoutRecord(fitnessActivity: FitnessActivity) {
        fitnessViewModel = ViewModelProvider(this).get(FitnessActivityViewModel::class.java)
        fitnessViewModel.postWorkoutRecord(fitnessActivity)
        val action = log_workout_recordDirections.actionLogWorkoutRecordToFitnessRoutinesDetails()
        action.fitnessDay = fitnessActivity.activityDate
        action.fitnessTime = fitnessActivity.startTime
        findNavController().navigate(action)
    }

    private fun calculateWorkoutCalories(duration: String): Int {
        val parts = duration.split(":")
        val hours = parts[0].toDouble() + parts[1].toDouble() / 60.0

        // default weight
        val weight = 50
        val MET = 7.0

        val caloriesPerKgHour = weight * MET
        return (caloriesPerKgHour * hours).toInt()
    }


}