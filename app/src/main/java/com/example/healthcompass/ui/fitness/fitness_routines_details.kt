package com.example.healthcompass.ui.fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R
import com.example.healthcompass.data.FitnessActivity.FitnessActivity
import com.example.healthcompass.data.FitnessActivity.FitnessActivityViewModel
import com.example.healthcompass.data.FitnessActivity.OnRequestCompleteCallBack
import com.google.firebase.database.DatabaseError
import org.w3c.dom.Text

class fitness_routines_details : Fragment() {
    private val args by navArgs<fitness_routines_detailsArgs>()
    private lateinit var fitnessActivityViewModel: FitnessActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fitness_routines_details, container, false)

        val tvActivityName: TextView = view.findViewById(R.id.tvActivityName)
        val imgActivityName: ImageView = view.findViewById(R.id.imgActivity)
        val tvActivityTime : TextView = view.findViewById(R.id.tvActivityTime)
        val tvActivityDesc: TextView = view.findViewById(R.id.tvActivityDesc)
        val tvCaloriesBurnt: TextView = view.findViewById(R.id.tvActivityCalories)
        val tvActivityDuration: TextView = view.findViewById(R.id.tvActivityDuration)
        val tvNote : TextView = view.findViewById(R.id.tvNote)

        // Find the fitness activity
        fitnessActivityViewModel = ViewModelProvider(this).get(FitnessActivityViewModel::class.java)
        fitnessActivityViewModel.getFitnessActivityDetails(object : OnRequestCompleteCallBack {
            override fun onSuccess(list: List<FitnessActivity>) {
                val fitnessActivity: FitnessActivity = list.get(0)

                tvActivityName.text = fitnessActivity.activityName
                tvActivityDesc.text = fitnessActivity.activityName
                tvCaloriesBurnt.text = fitnessActivity.caloriesBurnt.toString()
                tvActivityDuration.text = fitnessActivity.duration
                tvActivityTime.text = fitnessActivity.activityDate + " " + fitnessActivity.startTime
                tvNote.text = fitnessActivity.extraNote
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        }, args.fitnessDay, args.fitnessTime)

        val btnAddNote: Button = view.findViewById(R.id.btnAddNote)

        btnAddNote.setOnClickListener {

        }

        return view
    }
}