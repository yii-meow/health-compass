package com.example.healthcompass

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.util.Calendar


class log_workout_record : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var tvDatePicker: TextView
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
        val tvDistancePicker: TextView = view.findViewById(R.id.distancePicker)
        tvDatePicker = view.findViewById(R.id.datePicker)
        val tvStartTimePicker: TextView = view.findViewById(R.id.startTimePicker)

        // Duration
        tvDurationPicker.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
//            val minute = calendar.get(Calendar.MINUTE)
//
//            val timePickerDialog = TimePickerDialog(
//
//            )
//
//            timePickerDialog.show()
        }

        // Distance


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
                    tvStartTimePicker.setText("$hourOfDay:$minute")
                },
                hour,
                minute,
                false
            )

            timePickerDialog.show()
        }

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        tvDatePicker.text = "$dayOfMonth/${month+1}/$year"
    }
}