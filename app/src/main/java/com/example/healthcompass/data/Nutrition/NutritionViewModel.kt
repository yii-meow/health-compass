package com.example.healthcompass.data.Nutrition;

import android.app.Application;
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
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

    fun fetchCaloriesConsumption(
        callback: OnRequestCompleteCallBack<MutableMap<String, Int>>,
        date: String? = null
    ) {
        var currentDate = date
        if (date == null) {
            currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        }
        dbRef = FirebaseDatabase.getInstance().getReference("Meal")

        // Retrieve the data for today from Firebase
        val username = getUsername() ?: return
        dbRef.child(username).child(currentDate!!)
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
                    callback.onSuccess(caloriesConsumptionMap)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(getApplication(), "Error: $databaseError", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    fun fetchHydrationIntake(
        callback: UserViewModel.OnRequestCompleteCallBack<Int>,
        date: String? = null
    ) {
        var currentDate = date
        if (currentDate == null) {
            currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        }
        val username = getUsername() ?: return

        dbRef =
            FirebaseDatabase.getInstance().getReference("Meal").child(username).child(currentDate!!)
                .child("Hydration")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            val hydrations = arrayListOf<Int>()
            override fun onDataChange(snapshot: DataSnapshot) {
                val hydrationIntake = snapshot.getValue(Int::class.java)
                hydrations.add(hydrationIntake ?: 0)
                callback.onSuccess(hydrations)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    getApplication(),
                    "Error fetching hydration : $error",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun getUsername(): String? {
        val sharedPref: SharedPreferences =
            getApplication<Application>().getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }

    interface OnRequestCompleteCallBack<T> {
        fun onSuccess(list: MutableMap<String, Int>)
        fun onFailure(error: DatabaseError)
    }
}
