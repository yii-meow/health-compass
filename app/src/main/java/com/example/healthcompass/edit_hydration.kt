package com.example.healthcompass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class edit_hydration : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_hydration, container, false)
        val btnDone : Button = view.findViewById(R.id.btnDone)
        btnDone.setOnClickListener {
            findNavController().navigate(R.id.action_edit_hydration_to_nutrition)
        }
        return view
    }
}