package com.example.healthcompass.ui.fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R
import com.example.healthcompass.data.FitnessActivity.FitnessActivity
import com.example.healthcompass.data.FitnessActivity.FitnessActivityViewModel
import com.example.healthcompass.data.FitnessActivity.OnRequestCompleteCallBack
import com.google.firebase.database.DatabaseError

class fitness_routines : Fragment() {
    private lateinit var fitnessActivityViewModel: FitnessActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fitness_routines, container, false)

        val lblViewMore: TextView = view.findViewById(R.id.lblViewMore)
        val btnLogActivity: Button = view.findViewById(R.id.btnLogActivity)

        val flRunning: FrameLayout = view.findViewById(R.id.flRunning)

        fitnessActivityViewModel = ViewModelProvider(this).get(FitnessActivityViewModel::class.java)

        fitnessActivityViewModel.fetchLatestFitnessActivity(object : OnRequestCompleteCallBack {
            override fun onSuccess(list: List<FitnessActivity>) {
                // Set text view data
                view.findViewById<TextView>(R.id.tvSport1Type).text = list[0].activityName
                view.findViewById<TextView>(R.id.tvSport1Date).text = list[0].activityDate
                view.findViewById<TextView>(R.id.tvBurntCalories1).text =
                    list[0].caloriesBurnt.toString()
                view.findViewById<TextView>(R.id.tvSport1Time).text =
                    list[0].startTime.substring(0, 5) + " - " + list[0].endTime.substring(0, 5)
                view.findViewById<TextView>(R.id.tvSport1Duration).text =
                    list[0].duration
                view.findViewById<ImageView>(R.id.imgSport1)
                    .setBackgroundResource(getImgFitness(list[0].activityName))

                view.findViewById<TextView>(R.id.tvSport2Type).text = list[1].activityName
                view.findViewById<TextView>(R.id.tvSport2Date).text = list[1].activityDate
                view.findViewById<TextView>(R.id.tvBurntCalories2).text =
                    list[1].caloriesBurnt.toString()
                view.findViewById<TextView>(R.id.tvSport2Time).text =
                    list[1].startTime.substring(0, 5) + " - " + list[1].endTime.substring(0, 5)
                view.findViewById<TextView>(R.id.tvSport2Duration).text =
                    list[1].duration
                view.findViewById<ImageView>(R.id.imgSport2)
                    .setBackgroundResource(getImgFitness(list[1].activityName))
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })

        lblViewMore.setOnClickListener {
            findNavController().navigate(R.id.action_fitness_routines_to_fitness_routines_list)
        }

        flRunning.setOnClickListener {
            findNavController().navigate(R.id.action_fitness_routines_to_start_quick_workout)
        }

        btnLogActivity.setOnClickListener {
            findNavController().navigate(R.id.action_fitness_routines_to_log_workout_record)
        }

        return view
    }

    fun getImgFitness(acitivtyName: String): Int {
        return when (acitivtyName) {
            "Running", "Jogging", "Treadmill" -> R.drawable.running
            "Walking" -> R.drawable.walking
            "Badminton" -> R.drawable.badminton
            else -> R.drawable.cycling
        }
    }
}