package com.example.healthcompass.ui.nutrition

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
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
import androidx.collection.arrayMapOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R
import com.example.healthcompass.data.Nutrition.NutritionViewModel
import com.example.healthcompass.data.Nutrition.UserViewModel
import com.example.healthcompass.data.User.UserClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class nutrition : Fragment() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var tvBreakfastKcal: TextView
    private lateinit var tvLunchKcal: TextView
    private lateinit var tvDinnerKcal: TextView
    private lateinit var tvTotalConsumptionCalories: TextView
    private lateinit var tvWeightGoal: TextView
    private lateinit var tvCaloriesGoal: TextView
    private lateinit var tvRemainingCalories: TextView
    private lateinit var userViewModel: UserViewModel
    private lateinit var nutritionViewModel: NutritionViewModel

    private lateinit var user: UserClass
    private lateinit var goal: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_nutrition, container, false)

        val tvNutrionDate: TextView = view.findViewById(R.id.tvNutrionDate)
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

        val addMealAction = nutritionDirections.actionNutritionToAddMeal()
        val previousNutritionAction =
            nutritionDirections.actionNutritionToPreviousDayNutrition()

        val calendar = Calendar.getInstance()
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
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

            0 -> {
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

        tvBreakfastKcal = view.findViewById(R.id.tvBreakfastKcal)
        tvLunchKcal = view.findViewById(R.id.tvLunchKcal)
        tvDinnerKcal = view.findViewById(R.id.tvDinnerKcal)
        tvTotalConsumptionCalories = view.findViewById(R.id.tvTotalConsumptionCalories)
        tvWeightGoal = view.findViewById(R.id.tvWeightGoal)
        tvCaloriesGoal = view.findViewById(R.id.tvCaloriesGoal)
        tvRemainingCalories = view.findViewById(R.id.tvRemainingCalories)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        nutritionViewModel = ViewModelProvider(this).get(NutritionViewModel::class.java)

        fetchCaloriesConsumption()
        fetchWeightGoal()
        fetchHydrationIntake()

        return view
    }

    private fun fetchWeightGoal() {
        goal = ""

        userViewModel.fetchWeightGoal(object : UserViewModel.OnRequestCompleteCallBack<String> {
            override fun onSuccess(list: List<String>) {
                goal = list[0]
                tvWeightGoal.text = goal
                fetchCaloriesGoal()
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchCaloriesGoal() {
        userViewModel.fetchUserDetails(object : UserViewModel.OnRequestCompleteCallBack<UserClass> {
            override fun onSuccess(list: List<UserClass>) {
                user = list[0]

                val bmr = calculateBMR(user)
                val TDEE = bmr * 1.2
                var caloriesGoal = TDEE

                when (goal) {
                    "Lose Weight" -> caloriesGoal = TDEE - 500
                    "Gain Weight" -> caloriesGoal = TDEE + 250
                }
                tvCaloriesGoal.text = caloriesGoal.toInt().toString()

                if(tvTotalConsumptionCalories.text.toString().toDouble() > caloriesGoal){
                    tvRemainingCalories.text =
                        "Over " + Math.abs(caloriesGoal - tvTotalConsumptionCalories.text.toString().toDouble())
                }

                tvRemainingCalories.text =
                    (caloriesGoal - tvTotalConsumptionCalories.text.toString()
                        .toDouble()).toInt().toString() + "    remaining"
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun calculateBMR(user: UserClass): Int {
        return when (user.gender) {
            "male" -> (88.362 + (13.397 * user.weight) + (4.799 * user.height) - (5.677 * user.age)).toInt()
            else -> (447.593 + (9.247 * user.weight) + (3.098 * user.height) - (4.330 * user.age)).toInt()
        }
    }

    private fun fetchCaloriesConsumption() {
        nutritionViewModel.fetchCaloriesConsumption(object :
            NutritionViewModel.OnRequestCompleteCallBack<MutableMap<String, Int>> {
            override fun onSuccess(list: MutableMap<String, Int>) {
                val breakfastKcal = list.getOrDefault("Breakfast", 0)
                val lunchKcal = list.getOrDefault("Lunch", 0)
                val dinnerKcal = list.getOrDefault("Dinner", 0)

                tvBreakfastKcal.text = breakfastKcal.toString()
                tvLunchKcal.text = lunchKcal.toString()
                tvDinnerKcal.text = dinnerKcal.toString()

                tvTotalConsumptionCalories.text =
                    (breakfastKcal + lunchKcal + dinnerKcal).toString()
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchHydrationIntake() {
        userViewModel.fetchHydrationIntake(object : UserViewModel.OnRequestCompleteCallBack<Int> {
            override fun onSuccess(list: List<Int>) {
                displayHydrationIntake(list[0])
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayHydrationIntake(hydrationIntake: Int) {
        // Find the TextView in your nutrition.xml layout
        val hydrationTextView = requireActivity().findViewById<TextView>(R.id.tvTodayHydration)

        // Update the TextView with the hydration intake value
        hydrationTextView.text = (hydrationIntake.toDouble() / 1000).toString()
    }

    private fun getUsername(): String? {
        val sharedPref: SharedPreferences =
            requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}