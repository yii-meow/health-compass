package com.example.healthcompass.ui.nutrition

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
            dbRef = FirebaseDatabase.getInstance().getReference("Users")




            Toast.makeText(
                requireContext(),
                "Edited goal to ${goalSpinner.selectedItem} successfully!",
                Toast.LENGTH_LONG
            ).show()
            findNavController().popBackStack()
        }

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }
}