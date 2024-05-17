package com.example.healthcompass

import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.data.User.UserClass
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
    private lateinit var imgBMI: ImageView
    private lateinit var tvBMIStatus: TextView
    private lateinit var tvBMRKcal: TextView

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
        fetchUserDetails()

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

        imgBMI = view.findViewById(R.id.imgBMI)
        tvBMIStatus = view.findViewById(R.id.tvBMIStatus)

        tvBMRKcal = view.findViewById(R.id.tvBMRKcal)

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

    private fun calculateBMI(weight: Float, height: Float) {
        if (weight > 0.0F && height > 0.0F) {
            val heightInMeters = height / 100 // convert height from cm to meters
            val bmi: Float = weight / (heightInMeters * heightInMeters)

            tvBMI.text = String.format("%.1f", bmi)

            when {
                bmi < 18.5F -> {
                    imgBMI.setBackgroundResource(R.drawable.underweight)
                    tvBMIStatus.text = "Underweight"
                    tvBMIStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }

                bmi in 18.5F..24.9F -> {
                    imgBMI.setBackgroundResource(R.drawable.normal_weight)
                    tvBMIStatus.text = "Normal"
                }

                bmi >= 25F -> {
                    imgBMI.setBackgroundResource(R.drawable.overweight)
                    tvBMIStatus.text = "Overweight"
                    tvBMIStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }
            }
        } else {
            tvBMI.text = "0.0"
        }
    }

    private fun calculateBMR(weight: Float, height: Float, gender: String, age: Int) {
        tvBMRKcal.text = when (gender) {
            "male" -> (66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age)).toInt().toString()
            else -> (655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age)).toInt().toString()
        }
    }

    private fun fetchUserDetails() {
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        val username = getUsername() ?: return

        dbRef.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserClass::class.java)
                    calculateBMI(userData!!.weight, userData!!.height)
                    calculateBMR(
                        userData!!.weight,
                        userData!!.height,
                        userData!!.gender,
                        userData!!.age
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })
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
