package com.example.healthcompass.ui.nutrition

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R
import com.example.healthcompass.data.Nutrition.NutritionViewModel
import com.example.healthcompass.data.Nutrition.UserViewModel
import com.google.firebase.database.DatabaseError
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.Calendar
import java.util.Locale

class previous_day_nutrition : Fragment() {
    private val args by navArgs<previous_day_nutritionArgs>()
    private lateinit var nutritionViewModel: NutritionViewModel
    private lateinit var tvNutritionDate: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_previous_day_nutrition, container, false)

        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        var dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)
        val daysToSubtract = if (dayOfWeek == Calendar.SUNDAY) {
            6 // If today is Sunday, go back to the previous Monday
        } else if (dayOfWeek == Calendar.MONDAY) {
            7 // If today is Monday, go back to the previous Monday (1 week ago)
        } else {
            dayOfWeek - Calendar.MONDAY // Otherwise, calculate days to the previous Monday
        }

        // Move calendar to the last Monday
        calendar.add(Calendar.DAY_OF_YEAR, -daysToSubtract)

        val flMonday: FrameLayout = view.findViewById(R.id.flMonday)
        val flTuesday: FrameLayout = view.findViewById(R.id.flTuesday)
        val flWednesday: FrameLayout = view.findViewById(R.id.flWednesday)
        val flThursday: FrameLayout = view.findViewById(R.id.flThursday)
        val flFriday: FrameLayout = view.findViewById(R.id.flFriday)
        val flSaturday: FrameLayout = view.findViewById(R.id.flSaturday)
        val flSunday: FrameLayout = view.findViewById(R.id.flSunday)

        tvNutritionDate = view.findViewById(R.id.tvNutrionDate)

        // Store the dates for the whole week starting from Monday
        val weekDates = mutableListOf<String>()
        for (i in 0..6) {
            weekDates.add(sdf.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        calendar.add(Calendar.DAY_OF_YEAR, -7)

        when (args.nutritionDay) {
            1 -> {
                tvNutritionDate.text = weekDates[0]
                flMonday.setBackgroundResource(R.drawable.today_nutrition_circle)
            }

            2 -> {
                tvNutritionDate.text = weekDates[1]
                flTuesday.setBackgroundResource(R.drawable.today_nutrition_circle)
            }

            3 -> {
                tvNutritionDate.text = weekDates[2]
                flWednesday.setBackgroundResource(R.drawable.today_nutrition_circle)
            }

            4 -> {
                tvNutritionDate.text = weekDates[3]
                flThursday.setBackgroundResource(R.drawable.today_nutrition_circle)
            }

            5 -> {
                tvNutritionDate.text = weekDates[4]
                flFriday.setBackgroundResource(R.drawable.today_nutrition_circle)
            }

            6 -> {
                tvNutritionDate.text = weekDates[5]
                flSaturday.setBackgroundResource(R.drawable.today_nutrition_circle)
            }

            7 -> {
                tvNutritionDate.text = weekDates[6]
                flSunday.setBackgroundResource(R.drawable.today_nutrition_circle)
            }
        }

        // Reset the calendar to the original position
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        Toast.makeText(requireContext(),"$dayOfWeek",Toast.LENGTH_LONG).show()

        nutritionViewModel = ViewModelProvider(this).get(NutritionViewModel::class.java)
        fetchCaloriesConsumption()
        fetchHydrationIntake()

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

    private fun fetchCaloriesConsumption() {
        nutritionViewModel.fetchCaloriesConsumption(object :
            NutritionViewModel.OnRequestCompleteCallBack<MutableMap<String, Int>> {
            override fun onSuccess(list: MutableMap<String, Int>) {
                val breakfastKcal = list.getOrDefault("Breakfast", 0)
                val lunchKcal = list.getOrDefault("Lunch", 0)
                val dinnerKcal = list.getOrDefault("Dinner", 0)

                requireActivity().findViewById<TextView>(R.id.tvBreakfastKcal).text =
                    breakfastKcal.toString()
                requireActivity().findViewById<TextView>(R.id.tvLunchKcal).text =
                    lunchKcal.toString()
                requireActivity().findViewById<TextView>(R.id.tvDinnerKcal).text =
                    dinnerKcal.toString()
                requireActivity().findViewById<TextView>(R.id.tvTotalConsumptionCalories).text =
                    (breakfastKcal + lunchKcal + dinnerKcal).toString()
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        }, tvNutritionDate.text.toString())
    }

    private fun fetchHydrationIntake() {
        nutritionViewModel.fetchHydrationIntake(object :
            UserViewModel.OnRequestCompleteCallBack<Int> {
            override fun onSuccess(list: List<Int>) {
                val hydrationTextView =
                    requireActivity().findViewById<TextView>(R.id.tvTodayHydration)

                // Update the TextView with the hydration intake value
                hydrationTextView.text = (list[0].toDouble() / 1000).toString()
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        }, tvNutritionDate.text.toString())
    }
}