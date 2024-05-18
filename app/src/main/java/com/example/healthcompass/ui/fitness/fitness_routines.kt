package com.example.healthcompass.ui.fitness

import android.graphics.Color
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
        val flWalking: FrameLayout = view.findViewById(R.id.flWalking)
        val flTreadmill: FrameLayout = view.findViewById(R.id.flTreadmill)
        val flCycling: FrameLayout = view.findViewById(R.id.flCycling)

        val tvCaloriesBurnt: TextView = view.findViewById(R.id.tvCaloriesBurnt)
        val tvTotalWorkoutsThisWeek: TextView = view.findViewById(R.id.tvTotalWorkoutsThisWeek)
        val tvTotalWorkoutDuration: TextView = view.findViewById(R.id.tvTotalWorkoutDuration)
        val tvFitnessStatus: TextView = view.findViewById(R.id.tvFitnessStatus)

        fitnessActivityViewModel = ViewModelProvider(this).get(FitnessActivityViewModel::class.java)
        fitnessActivityViewModel.fetchWeeklyFitnessDetails(object :
            FitnessActivityViewModel.OnCaloriesCalculationCallback<FitnessActivityViewModel.WeeklyFitnessSummary> {
            override fun onCaloriesSuccess(result: FitnessActivityViewModel.WeeklyFitnessSummary) {
                tvCaloriesBurnt.text = result.totalCaloriesBurnt.toString()
                tvTotalWorkoutsThisWeek.text = result.totalWorkouts.toString()
                tvTotalWorkoutDuration.text = result.totalDuration.toString()

                if (result.totalDuration > 150) {
                    tvFitnessStatus.text = "SUFFICIENT"
                } else {
                    tvFitnessStatus.text = "INSUFFICIENT"
                    tvFitnessStatus.setTextColor(Color.RED)
                }
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })

        fitnessActivityViewModel.fetchLatestFitnessActivity(object :
            FitnessActivityViewModel.OnRequestCompleteCallBack {
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

        val action = fitness_routinesDirections.actionFitnessRoutinesToStartQuickWorkout()

        lblViewMore.setOnClickListener {
            findNavController().navigate(R.id.action_fitness_routines_to_fitness_routines_list)
        }

        flRunning.setOnClickListener {
            action.activity = "Running"
            findNavController().navigate(action)
        }

        flWalking.setOnClickListener {
            action.activity = "Walking"
            findNavController().navigate(action)
        }

        flTreadmill.setOnClickListener {
            action.activity = "Treadmill"
            findNavController().navigate(action)
        }

        flCycling.setOnClickListener {
            action.activity = "Cycling"
            findNavController().navigate(action)
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