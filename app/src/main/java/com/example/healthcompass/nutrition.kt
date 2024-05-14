package com.example.healthcompass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.util.Calendar

class nutrition : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_nutrition, container, false)

        val tvNutrionDate: TextView = view.findViewById(R.id.tvNutrionDate)
        val btnEditGoal: Button = view.findViewById(R.id.btnEditGoal)
        val imgEditMealBreakfast: ImageView = view.findViewById(R.id.imgBreakfastArrow)
        val imgEditMealLunch: ImageView = view.findViewById(R.id.imgLunchArrow)
        val imgEditMealDinner: ImageView = view.findViewById(R.id.imgDinnerArrow)
        val imgAddHydration: ImageView = view.findViewById(R.id.imgAddHydration)

        val calendar = Calendar.getInstance()

        val flMonday: FrameLayout = view.findViewById(R.id.flMonday)
        val flTuesday: FrameLayout = view.findViewById(R.id.flTuesday)
        val flWednesday: FrameLayout = view.findViewById(R.id.flWednesday)
        val flThursday: FrameLayout = view.findViewById(R.id.flThursday)
        val flFriday: FrameLayout = view.findViewById(R.id.flFriday)
        val flSaturday: FrameLayout = view.findViewById(R.id.flSaturday)
        val flSunday: FrameLayout = view.findViewById(R.id.flSunday)

        val addMealAction = nutritionDirections.actionNutritionToAddMeal()
        val previousNutritionAction = nutritionDirections.actionNutritionToPreviousDayNutrition()

        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        tvNutrionDate.text = "$year-$month-$dayOfMonth"

        when (dayOfWeek) {
            1 -> {
                flMonday.setBackgroundResource(R.drawable.today_nutrition_circle)
                flMonday.isEnabled = false
            }

            2 -> {
                flTuesday.setBackgroundResource(R.drawable.today_nutrition_circle)
                flTuesday.isEnabled = false
            }

            3 -> {
                flWednesday.setBackgroundResource(R.drawable.today_nutrition_circle)
                flWednesday.isEnabled = false
            }

            4 -> {
                flThursday.setBackgroundResource(R.drawable.today_nutrition_circle)
                flThursday.isEnabled = false
            }

            5 -> {
                flFriday.setBackgroundResource(R.drawable.today_nutrition_circle)
                flFriday.isEnabled = false
            }

            6 -> {
                flSaturday.setBackgroundResource(R.drawable.today_nutrition_circle)
                flSaturday.isEnabled = false
            }

            7 -> {
                flSunday.setBackgroundResource(R.drawable.today_nutrition_circle)
                flSunday.isEnabled = false
            }
        }

        btnEditGoal.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_edit_goal)
        }

        imgEditMealBreakfast.setOnClickListener {
            addMealAction.mealType = "Breakfast"
            findNavController().navigate(addMealAction)
        }

        imgEditMealLunch.setOnClickListener {
            addMealAction.mealType = "Lunch"
            findNavController().navigate(addMealAction)
        }

        imgEditMealDinner.setOnClickListener {
            addMealAction.mealType = "Dinner"
            findNavController().navigate(addMealAction)
        }

        imgAddHydration.setOnClickListener {
            findNavController().navigate(R.id.action_nutrition_to_edit_hydration)
        }

        flMonday.setOnClickListener {
            previousNutritionAction.nutritionDay = 1
            findNavController().navigate(previousNutritionAction)
        }

        flTuesday.setOnClickListener {
            previousNutritionAction.nutritionDay = 2
            findNavController().navigate(previousNutritionAction)
        }

        flWednesday.setOnClickListener {
            previousNutritionAction.nutritionDay = 3
            findNavController().navigate(previousNutritionAction)
        }

        flThursday.setOnClickListener {
            previousNutritionAction.nutritionDay = 4
            findNavController().navigate(previousNutritionAction)
        }

        flFriday.setOnClickListener {
            previousNutritionAction.nutritionDay = 5
            findNavController().navigate(previousNutritionAction)
        }

        flSaturday.setOnClickListener {
            previousNutritionAction.nutritionDay = 6
            findNavController().navigate(previousNutritionAction)
        }

        flSunday.setOnClickListener {
            previousNutritionAction.nutritionDay = 7
            findNavController().navigate(previousNutritionAction)
        }

        return view
    }
}