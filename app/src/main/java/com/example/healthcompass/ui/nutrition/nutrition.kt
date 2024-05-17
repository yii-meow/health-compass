package com.example.healthcompass.ui.nutrition

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
import com.example.healthcompass.R
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

        val calendar = Calendar.getInstance()

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

        tvBreakfastKcal = view.findViewById(R.id.tvBreakfastKcal)
        tvLunchKcal = view.findViewById(R.id.tvLunchKcal)
        tvDinnerKcal = view.findViewById(R.id.tvDinnerKcal)
        tvTotalConsumptionCalories = view.findViewById(R.id.tvTotalConsumptionCalories)

        fetchCaloriesConsumption()
        fetchHydrationIntake()

        return view
    }

    private fun fetchCaloriesConsumption() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        dbRef = FirebaseDatabase.getInstance().getReference("Meal")

        // Retrieve the data for today from Firebase
        // TODO: Replace with the username or user id
        dbRef.child("yiyi").child(currentDate)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Create a map to store the total calories consumption for each meal type
                    val caloriesConsumptionMap = mutableMapOf<String, Double>()

                    // Iterate through the dataSnapshot to calculate the total calories consumption for each meal type
                    for (mealTypeSnapshot in dataSnapshot.children) {
                        val mealType = mealTypeSnapshot.key.toString()

                        // Retrieve the total calories consumption for the current meal type
                        val totalCaloriesConsumption =
                            mealTypeSnapshot.child("totalCaloriesConsumption")
                                .getValue(Double::class.java) ?: 0.0

                        // Update the calories consumption map
                        caloriesConsumptionMap[mealType] = totalCaloriesConsumption
                    }

                    val breakfastKcal = caloriesConsumptionMap.getOrDefault("Breakfast", 0.0)
                    val lunchKcal = caloriesConsumptionMap.getOrDefault("Lunch", 0.0)
                    val dinnerKcal = caloriesConsumptionMap.getOrDefault("Dinner", 0.0)

                    val totalConsumption = breakfastKcal + lunchKcal + dinnerKcal

                    tvBreakfastKcal.text =
                        breakfastKcal.toString()
                    tvLunchKcal.text = lunchKcal.toString()
                    tvDinnerKcal.text = dinnerKcal.toString()
                    tvTotalConsumptionCalories.text = String.format("%.2f", totalConsumption)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: $databaseError", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun fetchHydrationIntake() {
        val name = "yiyi"
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())

        dbRef = FirebaseDatabase.getInstance().getReference("Meal").child(name).child(date)
            .child("Hydration")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hydrationIntake = snapshot.getValue(Int::class.java)
                displayHydrationIntake(
                    hydrationIntake ?: 0
                ) // Call function to display hydration intake
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Error fetching hydration : $error",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun displayHydrationIntake(hydrationIntake: Int) {
        // Find the TextView in your nutrition.xml layout
        val hydrationTextView = requireActivity().findViewById<TextView>(R.id.tvTodayHydration)

        // Update the TextView with the hydration intake value
        hydrationTextView.text = (hydrationIntake.toDouble() / 1000).toString()
    }

}