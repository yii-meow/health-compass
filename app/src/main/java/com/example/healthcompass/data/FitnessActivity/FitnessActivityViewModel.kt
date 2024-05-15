package com.example.healthcompass.data.FitnessActivity

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.healthcompass.data.NutritionFact.OnRequestCompleteCallBack
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

    fun getFitnessActivity() {
        dbRef = FirebaseDatabase.getInstance().getReference("Meal")
        val name = "yiyi"
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())

        val query = dbRef.child(name).child(date).child("Hydration")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fitnessActivities = mutableListOf<FitnessActivity>()
                for (childSnapshot in snapshot.children) {
                    val hydration = childSnapshot.getValue(Int::class.java)
                    Toast.makeText(getApplication(), "$hydration", Toast.LENGTH_LONG).show()
//                    val fitnessActivity = FitnessActivity("Hydration", hydration ?: 0)
//                    fitnessActivities.add(fitnessActivity)
                }
                fitnessActivities.add(FitnessActivity("1", "1", 1.0, "1", "1", "1", 1.0))
                fitnessActivitiesLiveData.postValue(fitnessActivities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(getApplication(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }
}