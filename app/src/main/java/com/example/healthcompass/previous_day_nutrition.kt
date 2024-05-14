package com.example.healthcompass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class previous_day_nutrition : Fragment() {
    private val args by navArgs<previous_day_nutritionArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_previous_day_nutrition, container, false)

        val flMonday: FrameLayout = view.findViewById(R.id.flMonday)
        val flTuesday: FrameLayout = view.findViewById(R.id.flTuesday)
        val flWednesday: FrameLayout = view.findViewById(R.id.flWednesday)
        val flThursday: FrameLayout = view.findViewById(R.id.flThursday)
        val flFriday: FrameLayout = view.findViewById(R.id.flFriday)
        val flSaturday: FrameLayout = view.findViewById(R.id.flSaturday)
        val flSunday: FrameLayout = view.findViewById(R.id.flSunday)

        when (args.nutritionDay) {
            1 -> flMonday.setBackgroundResource(R.drawable.today_nutrition_circle)
            2 -> flTuesday.setBackgroundResource(R.drawable.today_nutrition_circle)
            3 -> flWednesday.setBackgroundResource(R.drawable.today_nutrition_circle)
            4 -> flThursday.setBackgroundResource(R.drawable.today_nutrition_circle)
            5 -> flFriday.setBackgroundResource(R.drawable.today_nutrition_circle)
            6 -> flSaturday.setBackgroundResource(R.drawable.today_nutrition_circle)
            7 -> flSunday.setBackgroundResource(R.drawable.today_nutrition_circle)
        }

        val action = previous_day_nutritionDirections.actionPreviousDayNutritionSelf()

        // View another previous day nutrition
        flMonday.setOnClickListener{
            action.nutritionDay = 1
            findNavController().navigate(action)
        }

        flTuesday.setOnClickListener{
            action.nutritionDay = 2
            findNavController().navigate(action)
        }

        flWednesday.setOnClickListener{
            action.nutritionDay = 3
            findNavController().navigate(action)
        }

        flThursday.setOnClickListener{
            action.nutritionDay = 4
            findNavController().navigate(action)
        }

        flFriday.setOnClickListener{
            action.nutritionDay = 5
            findNavController().navigate(action)
        }

        flSaturday.setOnClickListener{
            action.nutritionDay = 6
            findNavController().navigate(action)
        }

        flSunday.setOnClickListener{
            action.nutritionDay = 7
            findNavController().navigate(action)
        }

        return view
    }
}