package com.example.healthcompass.data.Nutrition;

import android.app.Application;
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.healthcompass.data.FitnessActivity.FitnessActivity
import com.example.healthcompass.data.User.UserClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NutritionViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var dbRef: DatabaseReference

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

//                    tvBreakfastKcal.text = breakfastKcal.toString()
//                    tvLunchKcal.text = lunchKcal.toString()
//                    tvDinnerKcal.text = dinnerKcal.toString()
//                    tvIntakeKcal.text = totalConsumption.toString()
//
//                    val TDEE = tvBMRKcal.text.toString().toDouble() * 1.2
//
//                    // Normal intake range
//                    if (totalConsumption > TDEE - 100 && totalConsumption < TDEE + 500) {
//                        tvMealStatus.text = "NORMAL"
//                    } else {
//                        tvMealStatus.text = "ABNORMAL"
//                        tvMealStatus.setTextColor(Color.RED)
//                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(getApplication(), "Error: $databaseError", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun getUsername(): String? {
        val sharedPref: SharedPreferences =
            getApplication<Application>().getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}
