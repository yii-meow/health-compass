package com.example.healthcompass.ui.fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R

class quick_workout_timer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quick_workout_timer, container, false)

        val btnStop: Button = view.findViewById(R.id.btnStop)

        btnStop.setOnClickListener {
            findNavController().navigate(R.id.action_quick_workout_timer_to_done_quick_workout)
        }

        return view
    }
}