package com.example.healthcompass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class add_meal : Fragment() {
    private val args by navArgs<add_mealArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_meal, container, false)

        val tvMealType: TextView = view.findViewById(R.id.tvMealType)
        tvMealType.text = args.mealType

        val btnMakeMealChanges: Button = view.findViewById(R.id.btnMakeMealChanges)
        btnMakeMealChanges.setOnClickListener {
            findNavController().navigate(R.id.action_add_meal_to_nutrition)
        }

        return view
    }
}