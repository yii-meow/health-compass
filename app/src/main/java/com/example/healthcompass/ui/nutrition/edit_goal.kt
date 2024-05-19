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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R
import com.example.healthcompass.data.Nutrition.UserViewModel
import com.example.healthcompass.data.User.UserClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class edit_goal : Fragment() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_goal, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

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
        userViewModel.editGoal(goal)
    }
}