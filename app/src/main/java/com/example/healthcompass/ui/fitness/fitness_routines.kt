package com.example.healthcompass.ui.fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R

class fitness_routines : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fitness_routines, container, false)

        val lblViewMore: TextView = view.findViewById(R.id.lblViewMore)
        val btnLogActivity: Button = view.findViewById(R.id.btnLogActivity)

        lblViewMore.setOnClickListener {
            findNavController().navigate(R.id.action_fitness_routines_to_fitness_routines_list)
        }

        btnLogActivity.setOnClickListener {
            findNavController().navigate(R.id.action_fitness_routines_to_log_workout_record)
        }

        return view
    }
}