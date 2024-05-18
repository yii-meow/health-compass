package com.example.healthcompass.ui.nutrition

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R
import java.util.Calendar

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

        val calendar = Calendar.getInstance()
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1

        val action =
            previous_day_nutritionDirections.actionPreviousDayNutritionSelf()

        // View another previous day nutrition
        flMonday.setOnClickListener {
            if (dayOfWeek == 1) {
                findNavController().navigate(R.id.action_previous_day_nutrition_to_nutrition)
            } else {
                action.nutritionDay = 1
                findNavController().navigate(action)
            }
        }

        flTuesday.setOnClickListener {
            if (dayOfWeek == 2) {
                findNavController().navigate(R.id.action_previous_day_nutrition_to_nutrition)
            } else {
                action.nutritionDay = 2
                findNavController().navigate(action)
            }
        }

        flWednesday.setOnClickListener {
            if (dayOfWeek == 3) {
                findNavController().navigate(R.id.action_previous_day_nutrition_to_nutrition)
            } else {
                action.nutritionDay = 3
                findNavController().navigate(action)
            }
        }

        flThursday.setOnClickListener {
            if (dayOfWeek == 4) {
                findNavController().navigate(R.id.action_previous_day_nutrition_to_nutrition)
            } else {
                action.nutritionDay = 4
                findNavController().navigate(action)
            }
        }

        flFriday.setOnClickListener {
            if (dayOfWeek == 5) {
                findNavController().navigate(R.id.action_previous_day_nutrition_to_nutrition)
            } else {
                action.nutritionDay = 5
                findNavController().navigate(action)
            }
        }

        flSaturday.setOnClickListener {
            if (dayOfWeek == 6) {
                findNavController().navigate(R.id.action_previous_day_nutrition_to_nutrition)
            } else {
                action.nutritionDay = 6
                findNavController().navigate(action)
            }
        }

        flSunday.setOnClickListener {
            if (dayOfWeek == 0) {
                findNavController().navigate(R.id.action_previous_day_nutrition_to_nutrition)
            } else {
                action.nutritionDay = 7
                findNavController().navigate(action)
            }
        }
        return view
    }
}