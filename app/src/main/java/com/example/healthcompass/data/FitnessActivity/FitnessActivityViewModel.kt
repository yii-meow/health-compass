package com.example.healthcompass.data.FitnessActivity

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.healthcompass.data.Nutrition.UserViewModel
import com.example.healthcompass.data.User.UserClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

                val userRef = FirebaseDatabase.getInstance().getReference("Users").child(name)
                userRef.get().addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(UserClass::class.java)
                    if (user != null) {
                        user?.let {
                            val milestones = it.milestones ?: mutableMapOf()

                            when (fitnessActivity.activityName.toLowerCase()) {
                                "badminton" -> {
                                    // Calculate the total number of walking activities and update milestones
                                    calculateTotalActivities("badminton",
                                        object : ActivityCountCallback {
                                            override fun onActivityCountRetrieved(count: Int) {
                                                if (count >= 3) milestones["badminton"]?.set(
                                                    "3_times",
                                                    true
                                                )
                                                if (count >= 7) milestones["badminton"]?.set(
                                                    "7_times",
                                                    true
                                                )
                                                if (count >= 30) milestones["badminton"]?.set(
                                                    "30_times",
                                                    true
                                                )
                                                userRef.child("milestones").child("badminton")
                                                    .setValue(milestones["badminton"])
                                            }
                                        })
                                }

                                "walking" -> {
                                    // Calculate the total number of walking activities and update milestones
                                    calculateTotalActivities("walking",
                                        object : ActivityCountCallback {
                                            override fun onActivityCountRetrieved(count: Int) {
                                                if (count >= 3) milestones["walking"]?.set(
                                                    "3_times",
                                                    true
                                                )
                                                if (count >= 25) milestones["walking"]?.set(
                                                    "25_times",
                                                    true
                                                )
                                                if (count >= 50) milestones["walking"]?.set(
                                                    "50_times",
                                                    true
                                                )
                                                userRef.child("milestones").child("walking")
                                                    .setValue(milestones["walking"])
                                            }
                                        })
                                }

                                "running" -> {
                                    // Calculate the total number of running activities and update milestones
                                    calculateTotalActivities(
                                        "running",
                                        object : ActivityCountCallback {
                                            override fun onActivityCountRetrieved(count: Int) {
                                                if (count >= 3) milestones["running"]?.set(
                                                    "3_times",
                                                    true
                                                )
                                                if (count >= 25) milestones["running"]?.set(
                                                    "25_times",
                                                    true
                                                )
                                                if (count >= 50) milestones["running"]?.set(
                                                    "50_times",
                                                    true
                                                )
                                                userRef.child("milestones").child("running")
                                                    .setValue(milestones["running"])
                                            }
                                        })
                                }
                            }
                        }
                    }
                }
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
                Toast.makeText(getApplication(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun fetchWeeklyFitnessDetails(callback: OnCaloriesCalculationCallback<WeeklyFitnessSummary>) {
        val username = getUsername() ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("Fitness").child(username)

        val calendar = Calendar.getInstance()
        // Set the calendar to the start of the current week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val startDate = calendar.time
        // Set the calendar to the end of the current week
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val endDate = calendar.time

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fitnessActivities = arrayListOf<FitnessActivity>()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                for (dateSnapshot in snapshot.children) {
                    val dateStr = dateSnapshot.key
                    val date = dateFormat.parse(dateStr)
                    if (date != null && !date.before(startDate) && !date.after(endDate)) {
                        for (timeSnapshot in dateSnapshot.children) {
                            val activity = timeSnapshot.getValue(FitnessActivity::class.java)
                            activity?.let { fitnessActivities.add(it) }
                        }
                    }
                }

                // Calculate total workouts
                val totalWorkouts = fitnessActivities.size

                // Total calories burnt
                val totalCalories = fitnessActivities.sumOf { it.caloriesBurnt }

                // Calculate total workout duration
                var totalDuration = 0
                fitnessActivities.forEach {
                    val parts = it.duration.split(":")
                    val hours = parts[0].toInt()
                    val minutes = parts[1].toInt()
                    totalDuration += hours * 60 + minutes
                }

                val summary = WeeklyFitnessSummary(
                    totalWorkouts,
                    totalDuration,
                    totalCalories
                )

                callback.onCaloriesSuccess(summary)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(getApplication(), "$error", Toast.LENGTH_LONG).show()
                callback.onFailure(error)
            }
        })
    }

    fun updateFitnessActivityNote(fitnessActivity: FitnessActivity, note: String) {
        val username = getUsername() ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("Fitness").child(username)

        val noteRef = dbRef.child(fitnessActivity.activityDate).child(fitnessActivity.startTime)
            .child("extraNote")
        noteRef.setValue(note)
            .addOnCompleteListener {
                Toast.makeText(getApplication(), "updated note successfully", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(getApplication(), "$error", Toast.LENGTH_LONG).show()
            }
    }

    private fun getUsername(): String? {
        val sharedPref: SharedPreferences =
            getApplication<Application>().getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }

    interface OnRequestCompleteCallBack {
        fun onSuccess(list: List<FitnessActivity>)
        fun onFailure(error: DatabaseError)
    }

    interface OnCaloriesCalculationCallback<T> {
        fun onCaloriesSuccess(result: T)
        fun onFailure(error: DatabaseError)
    }

    data class WeeklyFitnessSummary(
        val totalWorkouts: Int,
        val totalDuration: Int,
        val totalCaloriesBurnt: Int
    )

    private fun calculateTotalActivities(activityName: String, callback: ActivityCountCallback) {
        val username = getUsername() ?: return
        var totalActivities = 0
        val userRef = FirebaseDatabase.getInstance().getReference("Fitness")
        userRef.child(username).get().addOnSuccessListener { dataSnapshot ->
            for (dateSnapshot in dataSnapshot.children) {
                for (timeSnapshot in dateSnapshot.children) {
                    val activity = timeSnapshot.getValue(FitnessActivity::class.java)
                    if (activity != null && activity.activityName.toLowerCase() == activityName) {
                        totalActivities++
                    }
                }
            }
            callback.onActivityCountRetrieved(totalActivities)
        }

    }

    interface ActivityCountCallback {
        fun onActivityCountRetrieved(count: Int)
    }
}