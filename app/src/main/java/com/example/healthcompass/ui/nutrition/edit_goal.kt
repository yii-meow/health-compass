package com.example.healthcompass.ui.nutrition

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R
import com.example.healthcompass.data.User.UserClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class edit_goal : Fragment() {
    private lateinit var dbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_goal, container, false)

        val goalSpinner: Spinner = view.findViewById(R.id.spinnerSetGoal)

        view.findViewById<Button>(R.id.btnEditGoal).setOnClickListener {
            val goal = goalSpinner.selectedItem.toString()
            editGoal(goal)
            findNavController().navigate(R.id.action_edit_goal_to_nutrition)
        }

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun editGoal(goal: String) {
        val username = getUsername() ?: return
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(username)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // The user already has a goal, so update it
                dbRef.child("goal").setValue(goal)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Edited goal to ${goal} successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Failed to edit goal.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Failed to edit goal.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun getUsername(): String? {
        val sharedPref: SharedPreferences =
            requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}