package com.example.healthcompass.ui.fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R

class start_quick_workout : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_start_quick_workout, container, false)

        val btnStart : Button = view.findViewById(R.id.btnStart)

        btnStart.setOnClickListener{
            findNavController().navigate(R.id.action_start_quick_workout_to_quick_workout_timer)
        }

        return view
    }
}