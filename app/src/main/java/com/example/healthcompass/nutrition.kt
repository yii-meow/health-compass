package com.example.healthcompass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

class nutrition : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_nutrition, container, false)

        val btnEditGoal: Button = view.findViewById(R.id.btnEditGoal)
        val imgEditMealBreakfast: ImageView = view.findViewById(R.id.imgBreakfastArrow)
        val imgEditMealLunch: ImageView = view.findViewById(R.id.imgLunchArrow)
        val imgEditMealDinner: ImageView = view.findViewById(R.id.imgDinnerArrow)
        val imgAddHydration: ImageView = view.findViewById(R.id.imgAddHydration)

        val flMonday: FrameLayout = view.findViewById(R.id.flMonday)
        val flTuesday: FrameLayout = view.findViewById(R.id.flTuesday)
        val flWednesday: FrameLayout = view.findViewById(R.id.flWednesday)
        val flThursday: FrameLayout = view.findViewById(R.id.flThursday)
        val flFriday: FrameLayout = view.findViewById(R.id.flFriday)
        val flSaturday: FrameLayout = view.findViewById(R.id.flSaturday)
        val flSunday: FrameLayout = view.findViewById(R.id.flSunday)

        btnEditGoal.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_edit_goal)
        }

        imgEditMealBreakfast.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_add_meal)
        }

        imgEditMealLunch.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_add_meal)
        }

        imgEditMealDinner.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_add_meal)
        }

        imgAddHydration.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_edit_hydration)
        }

        flMonday.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_previous_day_nutrition)
        }

        flTuesday.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_previous_day_nutrition)
        }

        flWednesday.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_previous_day_nutrition)
        }

        flThursday.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_previous_day_nutrition)
        }

        flFriday.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_previous_day_nutrition)
        }

        flSaturday.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_previous_day_nutrition)
        }

        flSunday.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_previous_day_nutrition)
        }

        return view
    }
}