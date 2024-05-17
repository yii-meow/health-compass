package com.example.healthcompass

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SummaryFragment : Fragment() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var tvBMI: TextView
    private lateinit var tvBreakfastKcal: TextView
    private lateinit var tvLunchKcal: TextView
    private lateinit var tvDinnerKcal: TextView
    private lateinit var tvIntakeKcal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

        val nutritionView = view.findViewById<CardView>(R.id.cardDailyIntake)
        val fitnessRoutinesView = view.findViewById<CardView>(R.id.cardFitnessRoutines)
        val fitnessListView = view.findViewById<Button>(R.id.btnShowMoreFitness)

        val tvDate: TextView = view.findViewById(R.id.tvDate)
        tvBMI = view.findViewById(R.id.tvBMI)
        calculateBMI()

        tvBreakfastKcal = view.findViewById(R.id.tvBreakfastKcal)
        tvLunchKcal = view.findViewById(R.id.tvLunchKcal)
        tvDinnerKcal = view.findViewById(R.id.tvDinnerKcal)
        tvIntakeKcal = view.findViewById(R.id.tvIntakeKcal)
        fetchCaloriesConsumption()

        // Set current date
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        tvDate.text = formattedDate

        nutritionView.setOnClickListener {
            findNavController().navigate(R.id.action_summary_to_nutrition)
        }

        fitnessRoutinesView.setOnClickListener {
            findNavController().navigate(R.id.action_summary_to_fitness_routines)
        }

        fitnessListView.setOnClickListener {
            findNavController().navigate(R.id.action_summary_to_fitness_routines_list)
        }

        return view
    }

    private fun calculateBMI() {
        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val weight: Float = sharedPref.getFloat("weight", 0.0F)
        val height: Float = sharedPref.getFloat("height", 0.0F)

        if (weight > 0.0F && height > 0.0F) {
            val heightInMeters = height / 100 // convert height from cm to meters
            val bmi = weight / (heightInMeters * heightInMeters)

            tvBMI.text = String.format("%.1f", bmi)
        } else {
            tvBMI.text = "0.0"
        }
    }

    private fun fetchCaloriesConsumption() {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        dbRef = FirebaseDatabase.getInstance().getReference("Meal")

        // Retrieve the data for today from Firebase
        val username = getUsername() ?: return
        dbRef.child(username).child(currentDate)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Create a map to store the total calories consumption for each meal type
                    val caloriesConsumptionMap = mutableMapOf<String, Int>()

                    // Iterate through the dataSnapshot to calculate the total calories consumption for each meal type
                    for (mealTypeSnapshot in dataSnapshot.children) {
                        val mealType = mealTypeSnapshot.key.toString()

                        // Retrieve the total calories consumption for the current meal type
                        val totalCaloriesConsumption =
                            mealTypeSnapshot.child("totalCaloriesConsumption")
                                .getValue(Double::class.java) ?: 0.0

                        // Update the calories consumption map
                        caloriesConsumptionMap[mealType] = totalCaloriesConsumption.toInt()
                    }

                    val breakfastKcal = caloriesConsumptionMap.getOrDefault("Breakfast", 0)
                    val lunchKcal = caloriesConsumptionMap.getOrDefault("Lunch", 0)
                    val dinnerKcal = caloriesConsumptionMap.getOrDefault("Dinner", 0)
                    val totalConsumption = breakfastKcal + lunchKcal + dinnerKcal

                    tvBreakfastKcal.text = breakfastKcal.toString()
                    tvLunchKcal.text = lunchKcal.toString()
                    tvDinnerKcal.text = dinnerKcal.toString()
                    tvIntakeKcal.text = totalConsumption.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: $databaseError", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun getUsername(): String? {
        val sharedPref: SharedPreferences =
            requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}
