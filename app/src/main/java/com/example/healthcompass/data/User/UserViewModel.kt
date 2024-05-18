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

