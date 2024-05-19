package com.example.healthcompass

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.data.FitnessActivity.FitnessActivity
import com.example.healthcompass.data.FitnessActivity.FitnessActivityViewModel
import com.example.healthcompass.data.Nutrition.NutritionViewModel
import com.example.healthcompass.data.Nutrition.UserViewModel
import com.example.healthcompass.data.User.UserClass
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SummaryFragment : Fragment(), SensorEventListener {
    private lateinit var tvBMI: TextView
    private lateinit var tvBreakfastKcal: TextView
    private lateinit var tvLunchKcal: TextView
    private lateinit var tvDinnerKcal: TextView
    private lateinit var tvIntakeKcal: TextView
    private lateinit var imgBMI: ImageView
    private lateinit var tvBMIStatus: TextView
    private lateinit var tvBMRKcal: TextView
    private lateinit var fitnessActivityViewModel: FitnessActivityViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var nutritionViewModel: NutritionViewModel
    private lateinit var tvMealStatus: TextView

    private lateinit var tvSport1Type: TextView
    private lateinit var tvSport1Date: TextView
    private lateinit var tvSport1Calories: TextView
    private lateinit var tvSport1Duration: TextView
    private lateinit var imgSport1: ImageView

    private lateinit var tvSport2Type: TextView
    private lateinit var tvSport2Date: TextView
    private lateinit var tvSport2Calories: TextView
    private lateinit var tvSport2Duration: TextView
    private lateinit var imgSport2: ImageView

    private lateinit var tvStep: TextView
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var isSensorPresent = false
    private var totalSteps = 0
    private var previousStepCount = 0
    private var isInitialStepCountSet = false

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
                // Reset total steps on device reboot
                totalSteps = 0
                previousStepCount = 0
                isInitialStepCountSet = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        isSensorPresent = stepCounterSensor != null

        // Register BroadcastReceiver for ACTION_BOOT_COMPLETED
        val filter = IntentFilter(Intent.ACTION_BOOT_COMPLETED)
        requireContext().registerReceiver(broadcastReceiver, filter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

        val nutritionView = view.findViewById<CardView>(R.id.cardDailyIntake)
        val fitnessRoutinesView = view.findViewById<CardView>(R.id.cardFitnessRoutines)
        val fitnessListView = view.findViewById<Button>(R.id.btnShowMoreFitness)
        val tvDate: TextView = view.findViewById(R.id.tvDate)

        tvBMI = view.findViewById(R.id.tvBMI)
        tvBreakfastKcal = view.findViewById(R.id.tvBreakfastKcal)
        tvLunchKcal = view.findViewById(R.id.tvLunchKcal)
        tvDinnerKcal = view.findViewById(R.id.tvDinnerKcal)
        tvIntakeKcal = view.findViewById(R.id.tvIntakeKcal)

        // Set current date
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        tvDate.text = formattedDate

        imgBMI = view.findViewById(R.id.imgBMI)
        tvBMIStatus = view.findViewById(R.id.tvBMIStatus)
        tvBMRKcal = view.findViewById(R.id.tvBMRKcal)
        tvMealStatus = view.findViewById(R.id.tvMealStatus)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        fetchUserDetails()

        nutritionViewModel = ViewModelProvider(this).get(NutritionViewModel::class.java)
        fetchCaloriesConsumption()

        // Latest two records
        tvSport1Type = view.findViewById(R.id.tvSport1Type)
        tvSport1Date = view.findViewById(R.id.tvSport1Date)
        tvSport1Calories = view.findViewById(R.id.tvSport1Calories)
        tvSport1Duration = view.findViewById(R.id.tvSport1Duration)
        imgSport1 = view.findViewById(R.id.imgSport1)

        tvSport2Type = view.findViewById(R.id.tvSport2Type)
        tvSport2Date = view.findViewById(R.id.tvSport2Date)
        tvSport2Calories = view.findViewById(R.id.tvSport2Calories)
        tvSport2Duration = view.findViewById(R.id.tvSport2Duration)
        imgSport2 = view.findViewById(R.id.imgSport2)

        fitnessActivityViewModel = ViewModelProvider(this).get(FitnessActivityViewModel::class.java)
        fetchLatestFitnessActivities()

        checkPermissions()
        tvStep = view.findViewById(R.id.tvStep)

        nutritionView.setOnClickListener {
            findNavController().navigate(R.id.action_summary_to_nutrition)
        }

        fitnessRoutinesView.setOnClickListener {
            findNavController().navigate(R.id.action_summary_to_fitness_routines)
        }

        fitnessListView.setOnClickListener {
            findNavController().navigate(R.id.action_summary_to_fitness_routines_list)
        }

        return view
    }

    private fun calculateBMI(weight: Float, height: Float) {
        if (weight > 0.0F && height > 0.0F) {
            val heightInMeters = height / 100 // convert height from cm to meters
            val bmi: Float = weight / (heightInMeters * heightInMeters)

            tvBMI.text = String.format("%.1f", bmi)

            when {
                bmi < 18.5F -> {
                    imgBMI.setBackgroundResource(R.drawable.underweight)
                    tvBMIStatus.text = "Underweight"
                    tvBMIStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }

                bmi in 18.5F..24.9F -> {
                    imgBMI.setBackgroundResource(R.drawable.normal_weight)
                    tvBMIStatus.text = "Normal"
                }

                bmi >= 25F -> {
                    imgBMI.setBackgroundResource(R.drawable.overweight)
                    tvBMIStatus.text = "Overweight"
                    tvBMIStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }
            }
        } else {
            tvBMI.text = "0.0"
        }
    }

    private fun calculateBMR(weight: Float, height: Float, gender: String, age: Int) {
        tvBMRKcal.text = when (gender) {
            "male" -> (88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)).toInt()
                .toString()

            else -> (447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)).toInt()
                .toString()
        }
    }

    private fun fetchUserDetails() {
        userViewModel.fetchUserDetails(object : UserViewModel.OnRequestCompleteCallBack<UserClass> {
            override fun onSuccess(list: List<UserClass>) {
                val user = list[0]
                calculateBMI(user!!.weight, user!!.height)
                calculateBMR(
                    user!!.weight, user!!.height, user!!.gender, user!!.age
                )
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchCaloriesConsumption() {
        nutritionViewModel.fetchCaloriesConsumption(object :
            NutritionViewModel.OnRequestCompleteCallBack<MutableMap<String, Int>> {
            override fun onSuccess(list: MutableMap<String, Int>) {
                val breakfastKcal = list.getOrDefault("Breakfast", 0)
                val lunchKcal = list.getOrDefault("Lunch", 0)
                val dinnerKcal = list.getOrDefault("Dinner", 0)
                val totalConsumption = breakfastKcal + lunchKcal + dinnerKcal

                tvBreakfastKcal.text = breakfastKcal.toString()
                tvLunchKcal.text = lunchKcal.toString()
                tvDinnerKcal.text = dinnerKcal.toString()
                tvIntakeKcal.text = totalConsumption.toString()

                // Normal intake range
                val bmrText = tvBMRKcal.text.toString()
                val TDEE = if (bmrText.isNotEmpty()) {
                    bmrText.toDouble() * 1.2
                } else {
                    0.0 // or any default value you want
                }

                if (totalConsumption > TDEE - 100 && totalConsumption < TDEE + 500) {
                    tvMealStatus.text = "NORMAL"
                } else {
                    tvMealStatus.text = "ABNORMAL"
                    tvMealStatus.setTextColor(Color.RED)
                }
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchLatestFitnessActivities() {
        fitnessActivityViewModel.fetchLatestFitnessActivity(object :
            FitnessActivityViewModel.OnRequestCompleteCallBack {
            override fun onSuccess(list: List<FitnessActivity>) {
                // Set text view data
                tvSport1Type.text = list[0].activityName
                tvSport1Date.text = list[0].activityDate
                tvSport1Calories.text = list[0].caloriesBurnt.toString()
                tvSport1Duration.text =
                    list[0].startTime.substring(0, 5) + " - " + list[0].endTime.substring(0, 5)
                imgSport1.setBackgroundResource(getImgFitness(list[0].activityName))

                tvSport2Type.text = list[1].activityName
                tvSport2Date.text = list[1].activityDate
                tvSport2Calories.text = list[1].caloriesBurnt.toString()
                tvSport2Duration.text =
                    list[1].startTime.substring(0, 5) + " - " + list[1].endTime.substring(0, 5)
                imgSport2.setBackgroundResource(getImgFitness(list[1].activityName))
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getImgFitness(acitivtyName: String): Int {
        return when (acitivtyName) {
            "Running", "Jogging", "Treadmill" -> R.drawable.running
            "Walking" -> R.drawable.walking
            "Badminton" -> R.drawable.badminton
            else -> R.drawable.cycling
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isSensorPresent) {
            sensorManager.registerListener(
                this,
                stepCounterSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isSensorPresent) {
            sensorManager.unregisterListener(this)
        }
    }

    private val REQUEST_CODE_ACTIVITY_RECOGNITION = 1002

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACTIVITY_RECOGNITION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                REQUEST_CODE_ACTIVITY_RECOGNITION
            )
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, initialize step counter
//            } else {
//                // Permission denied, handle accordingly
//            }
//        }
//    }

    override fun onSensorChanged(event: SensorEvent?) {
        Toast.makeText(requireContext(),"hi",Toast.LENGTH_LONG).show()
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val currentStepCount = event.values[0].toInt()

            if (!isInitialStepCountSet) {
                // Set initial step count on first sensor event
                previousStepCount = currentStepCount
                isInitialStepCountSet = true
            }

            // Calculate steps since last sensor event
            val stepsSinceLastEvent = currentStepCount - previousStepCount
            previousStepCount = currentStepCount

            // Update total steps
            totalSteps += stepsSinceLastEvent
            tvStep.text = totalSteps.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(broadcastReceiver)
    }
}
