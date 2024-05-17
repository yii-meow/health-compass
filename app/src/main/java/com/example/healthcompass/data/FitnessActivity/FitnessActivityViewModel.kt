package com.example.healthcompass.data.FitnessActivity

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.ui.fitness.log_workout_recordDirections
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

    fun getAllFitnessActivity(callback: OnRequestCompleteCallBack) {
        dbRef = FirebaseDatabase.getInstance().getReference("Fitness")
        val username = getUsername() ?: return

        val query = dbRef.child(username)

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
                        val extraNote = activityValues["extraNote"] as? String ?: ""

                        // Create a FitnessActivity object and add it to the list
                        val fitnessActivity = FitnessActivity(
                            activityName,
                            activityDate,
                            caloriesBurnt,
                            startTime,
                            endTime,
                            duration,
                            extraNote
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

    fun getFitnessActivityDetails(
        callback: OnRequestCompleteCallBack,
        activityDate: String,
        startTime: String
    ) {
        dbRef = FirebaseDatabase.getInstance().getReference("Fitness")
        val username = getUsername() ?: return

        dbRef.child(username).child(activityDate).child(startTime)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Check if the data exists
                    if (dataSnapshot.exists()) {
                        val fitnessActivities = arrayListOf<FitnessActivity>()
                        val activityValues = dataSnapshot.value as Map<String, Any>

                        val activityName = activityValues["activityName"] as? String ?: ""
                        val activityDate = activityValues["activityDate"] as? String ?: ""
                        val caloriesBurnt =
                            (activityValues["caloriesBurnt"] as? Number)?.toInt() ?: 0
                        val startTime = activityValues["startTime"] as? String ?: ""
                        val endTime = activityValues["endTime"] as? String ?: ""
                        val duration = activityValues["duration"] as? String ?: ""
                        val extraNote = activityValues["extraNote"] as? String ?: ""

                        val fitnessActivity = FitnessActivity(
                            activityName,
                            activityDate,
                            caloriesBurnt,
                            startTime,
                            endTime,
                            duration,
                            extraNote
                        )

                        fitnessActivities.add(fitnessActivity)
                        callback.onSuccess(fitnessActivities)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback.onFailure(databaseError)
                }
            })
    }

    fun postWorkoutRecord(fitnessActivity: FitnessActivity) {
        dbRef = FirebaseDatabase.getInstance().getReference("Fitness")
        val name = getUsername()
        dbRef.child(name!!).child(fitnessActivity.activityDate).child(fitnessActivity.startTime)
            .setValue(fitnessActivity)
            .addOnCompleteListener {
                Toast.makeText(
                    getApplication(),
                    "Added fitness record successfully!",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(getApplication(), "Failed to add fitness record!", Toast.LENGTH_LONG)
                    .show()
            }
    }

    fun fetchLatestFitnessActivity(callback: OnRequestCompleteCallBack) {
        val username = getUsername() ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("Fitness").child(username)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fitnessActivities = arrayListOf<FitnessActivity>()
                for (dateSnapshot in snapshot.children) {
                    for (timeSnapshot in dateSnapshot.children) {
                        val activity = timeSnapshot.getValue(FitnessActivity::class.java)
                        fitnessActivities.add(activity!!)
                    }
                }
                // Sort the activities by date and time
                fitnessActivities.sortWith(compareBy<FitnessActivity> { it.activityDate }.thenBy { it.startTime })

                // Get the latest two activities
                val latestTwoActivities = fitnessActivities.takeLast(2).reversed()
                callback.onSuccess(latestTwoActivities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(getApplication(),"$error",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUsername(): String? {
        val sharedPref: SharedPreferences =
            getApplication<Application>().getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}

interface OnRequestCompleteCallBack {
    fun onSuccess(list: List<FitnessActivity>)
    fun onFailure(error: DatabaseError)
}