package com.example.healthcompass.data.FitnessActivity

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class FitnessActivityViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var dbRef: DatabaseReference

    private val fitnessActivitiesLiveData = MutableLiveData<List<FitnessActivity>>()

    fun getFitnessActivitiesLiveData(): LiveData<List<FitnessActivity>> {
        return fitnessActivitiesLiveData
    }

    fun getFitnessActivity(callback: OnRequestCompleteCallBack) {
        dbRef = FirebaseDatabase.getInstance().getReference("Fitness")
        val name = "yiyi"

        val query = dbRef.child(name)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fitnessActivities = arrayListOf<FitnessActivity>()

                // Reverse the order of the children to get the latest day first
                val reversedChildren = snapshot.children.reversed()

                for (dateSnapshot in reversedChildren) {
                    val date = dateSnapshot.key // Get the date
                    for (activitySnapshot in dateSnapshot.children) {
                        val activityValues = activitySnapshot.value as Map<String, Any>
                        val activityName = activityValues["activityName"] as String
                        val activityDate = activityValues["activityDate"] as String
                        val caloriesBurnt = (activityValues["caloriesBurnt"] as Long).toInt()
                        val startTime = activityValues["startTime"] as String
                        val endTime = activityValues["endTime"] as String
                        val duration = activityValues["duration"] as String

                        // Create a FitnessActivity object and add it to the list
                        val fitnessActivity = FitnessActivity(
                            activityName,
                            activityDate,
                            caloriesBurnt,
                            startTime,
                            endTime,
                            duration
                        )
                        fitnessActivities.add(fitnessActivity)
                    }
                }
                callback?.onSuccess(fitnessActivities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(getApplication(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }
}

interface OnRequestCompleteCallBack {
    fun onSuccess(list: List<FitnessActivity>)
    fun onFailure(error: DatabaseError)
}