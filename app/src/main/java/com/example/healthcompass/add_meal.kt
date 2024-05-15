package com.example.healthcompass

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.data.NutritionFact.FoodItem
import com.example.healthcompass.data.NutritionFact.NutritionFactViewModel
import com.example.healthcompass.data.NutritionFact.OnRequestCompleteCallBack
import org.w3c.dom.Text
import java.util.Calendar

class add_meal : Fragment() {
    private val args by navArgs<add_mealArgs>()
    private lateinit var nutritionFactViewModel: NutritionFactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_meal, container, false)

        val tvMealType: TextView = view.findViewById(R.id.tvMealType)
        tvMealType.text = args.mealType

        // TODO: Retrieve food from api
//        val foodSpinner : Spinner = view.findViewById(R.id.tvSelectFood)
//        val foodAdapter = ArrayAdapter.createFromResource(
//            requireContext(),
//            R.layout.custom_spinner_item
//        )

//        foodAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

//        foodSpinner.adapter = foodAdapter


        // TODO: Retrieve Nutrition Fact of the food
        nutritionFactViewModel = ViewModelProvider(this).get(NutritionFactViewModel::class.java)
        val imgAddFood: ImageView = view.findViewById(R.id.imgAddFood)

        imgAddFood.setOnClickListener {
            nutritionFactViewModel.getNutritionFact(object : OnRequestCompleteCallBack {
                override fun onSuccess(list: ArrayList<FoodItem>) {
                    Toast.makeText(
                        requireContext(),
                        "Retrieved nutrition fact $list",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }, "apple")
        }

        val btnMakeMealChanges: Button = view.findViewById(R.id.btnMakeMealChanges)
        btnMakeMealChanges.setOnClickListener {
            findNavController().navigate(R.id.action_add_meal_to_nutrition)
        }

        // Pick Time
        val btnPickTime: Button = view.findViewById(R.id.btnTimePicker)
        val tvMealTime: TextView = view.findViewById(R.id.tvMealTime)

        btnPickTime.setOnClickListener {
            val c = Calendar.getInstance()

            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { view, hourOfDay, minute ->
                    btnPickTime.setText("$hourOfDay:$minute")
                },
                hour,
                minute,
                false
            )

            timePickerDialog.show()
        }

        return view
    }
}