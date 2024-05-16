package com.example.healthcompass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.navigation.fragment.findNavController

class log_workout_record : Fragment() {
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

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }
}