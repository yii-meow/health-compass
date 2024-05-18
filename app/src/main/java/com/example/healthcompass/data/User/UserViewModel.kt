package com.example.healthcompass.data.Nutrition;

import android.app.Application;
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.healthcompass.data.User.UserClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var dbRef: DatabaseReference

    fun fetchUserDetails(callback: OnRequestCompleteCallBack<UserClass>) {
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        val username = getUsername() ?: return

        dbRef.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val users = arrayListOf<UserClass>()
                    val userData = snapshot.getValue(UserClass::class.java)
                    users.add(userData!!)
                    callback.onSuccess(users)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(getApplication(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun fetchWeightGoal(callback: OnRequestCompleteCallBack<String>) {
        val username = getUsername() ?: return

        var goals = arrayListOf<String>()
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(username)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("goal")) {
                    val goal = snapshot.child("goal").getValue(String::class.java) ?: ""
                    goals.add(goal)
                } else {
                    ""
                }
                callback.onSuccess(goals)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    getApplication(),
                    "Error fetching goal : $error",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    fun fetchHydrationIntake(callback: OnRequestCompleteCallBack<Int>) {
        val username = getUsername() ?: return
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())

        dbRef = FirebaseDatabase.getInstance().getReference("Meal").child(username).child(date)
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
        fun onSuccess(list: List<T>)
        fun onFailure(error: DatabaseError)
    }
}


