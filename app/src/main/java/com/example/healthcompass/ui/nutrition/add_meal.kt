package com.example.healthcompass.ui.nutrition

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R
import com.example.healthcompass.data.Nutrition.NutritionViewModel
import com.example.healthcompass.data.NutritionFact.FoodItem
import com.example.healthcompass.data.NutritionFact.Meal
import com.example.healthcompass.data.NutritionFact.NutritionFactViewModel
import com.example.healthcompass.data.NutritionFact.OnRequestCompleteCallBack
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class add_meal : Fragment() {
    private val args by navArgs<add_mealArgs>()
    private lateinit var nutritionFactViewModel: NutritionFactViewModel
    private lateinit var nutritionViewModel: NutritionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_meal, container, false)

        val tvMealType: TextView = view.findViewById(R.id.tvMealType)
        tvMealType.text = args.mealType

        var foodCounter = 1

        nutritionFactViewModel = ViewModelProvider(this).get(NutritionFactViewModel::class.java)
        nutritionViewModel = ViewModelProvider(this).get(NutritionViewModel::class.java)

        nutritionViewModel.fetchTodayMeal(object :
            NutritionViewModel.OnRequestCompleteCallBackMeal {
            override fun onSuccess(list: List<FoodItem>) {
                val foodRowContainer = view.findViewById<LinearLayout>(R.id.foodRowContainer)

                if (list.isNotEmpty()) {
                    for (food in list) {
                        val newFoodRow = layoutInflater.inflate(R.layout.food_row_layout, null)
                        val newTvFoodNo = newFoodRow.findViewById<TextView>(R.id.tvFoodNo)
                        newTvFoodNo.text = (foodCounter).toString() + ")"

                        val newTvFood = newFoodRow.findViewById<TextView>(R.id.tvSelectFood)
                        newTvFood.text = food.name

                        val newTvFoodQuantity =
                            newFoodRow.findViewById<TextView>(R.id.tvFoodQuantity)
                        newTvFoodQuantity.text = (1).toString()

                        foodRowContainer.addView(newFoodRow)

                        val newImgDeleteFood =
                            newFoodRow.findViewById<ImageView>(R.id.imgDeleteFood)

                        newImgDeleteFood.setOnClickListener {
                            foodRowContainer.removeView(newFoodRow)
                            foodCounter--
                        }

                        foodCounter++
                    }
                } else {
                    val newFoodRow = layoutInflater.inflate(R.layout.food_row_layout, null)
                    val newTvFoodNo = newFoodRow.findViewById<TextView>(R.id.tvFoodNo)
                    newTvFoodNo.text = (foodCounter).toString() + ")"

                    val newTvFood = newFoodRow.findViewById<TextView>(R.id.tvSelectFood)
                    newTvFood.text = "Enter food here..."

                    val imgDelete = newFoodRow.findViewById<ImageView>(R.id.imgDeleteFood)
                    imgDelete.isVisible = false
                    imgDelete.isClickable = false

                    foodRowContainer.addView(newFoodRow)
                }
                foodCounter--
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        }, args.mealType)

        val imgAddFood: ImageView = view.findViewById(R.id.imgAddFood)

        // Add new food row
        imgAddFood.setOnClickListener {
            foodCounter++
            val foodRowContainer = view.findViewById<LinearLayout>(R.id.foodRowContainer)
            val newFoodRow = layoutInflater.inflate(R.layout.food_row_layout, null)

            val newTvFoodNo = newFoodRow.findViewById<TextView>(R.id.tvFoodNo)
            newTvFoodNo.text = (foodCounter).toString() + ")"

            foodRowContainer.addView(newFoodRow)

            val newImgDeleteFood = newFoodRow.findViewById<ImageView>(R.id.imgDeleteFood)

            newImgDeleteFood.setOnClickListener {
                foodRowContainer.removeView(newFoodRow)
                foodCounter--
            }
        }

        // Pick Time
        val btnPickTime: Button = view.findViewById(R.id.btnTimePicker)

        btnPickTime.setOnClickListener {
            val c = Calendar.getInstance()

            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { view, hourOfDay, minute ->
                    val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                    btnPickTime.setText(formattedTime)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

        val btnMakeMealChanges: Button = view.findViewById(R.id.btnMakeMealChanges)
        val mealMap = mutableMapOf<String, Int>()

        btnMakeMealChanges.setOnClickListener {
            val foodRowContainer = view.findViewById<LinearLayout>(R.id.foodRowContainer)
            var filledDetails = true

            // Get all meals
            for (i in 0 until foodRowContainer.childCount) {
                val foodRow = foodRowContainer.getChildAt(i)
                val tvSelectedFood = foodRow.findViewById<EditText>(R.id.tvSelectFood)
                val tvFoodQuantity = foodRow.findViewById<EditText>(R.id.tvFoodQuantity)

                val foodName = tvSelectedFood.text.toString()
                val foodQuantity = tvFoodQuantity.text.toString().toIntOrNull()

                // Ensure both food name and quantity are not empty
                if (foodName.isNotEmpty() && foodName != "Enter food here..." && foodQuantity != null) {
                    mealMap[foodName] = foodQuantity
                } else {
                    filledDetails = false
                    Toast.makeText(
                        requireContext(),
                        "Please ensure food is not empty!",
                        Toast.LENGTH_LONG
                    ).show()
                    break
                }
            }

            if (btnPickTime.text == "Pick Time") {
                Toast.makeText(
                    requireContext(),
                    "Please ensure time is not empty!",
                    Toast.LENGTH_LONG
                ).show()
                filledDetails = false
            }

            if (filledDetails) {
                setNutrionFact(mealMap)
            }
        }

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun setNutrionFact(foodMap: MutableMap<String, Int>) {
        val nutritionRowContainer = view?.findViewById<LinearLayout>(R.id.nutritionFactContainer)

        val foodList = mutableListOf<FoodItem>()
        var totalCaloriesConsumption = 0.0

        // Iterate over each food item in the map
        for ((index, entry) in foodMap.entries.withIndex()) {
            val foodName = entry.key
            val quantity = entry.value

            val newNutritionRow = layoutInflater.inflate(R.layout.nutrition_row_layout, null)
            val newTvFoodNutritionNo =
                newNutritionRow.findViewById<TextView>(R.id.tvNutritionFoodNo)
            val newTvFoodNutritionNameNo = newNutritionRow.findViewById<TextView>(R.id.tvFood)
            val newTvFoodNutritionQuantity =
                newNutritionRow.findViewById<TextView>(R.id.tvFoodQuantity)
            val newTvFoodNutritionFat = newNutritionRow.findViewById<TextView>(R.id.tvFoodFat)
            val newTvFoodNutritionCarbohydrates =
                newNutritionRow.findViewById<TextView>(R.id.tvFoodCarbohydrates)
            val newTvFoodNutritionProtein =
                newNutritionRow.findViewById<TextView>(R.id.tvFoodProtein)
            val newTvFoodNutritionCalories =
                newNutritionRow.findViewById<TextView>(R.id.tvFoodCalories)

            // Retrieve Nutrition Fact of the food
            nutritionFactViewModel.getNutritionFact(object : OnRequestCompleteCallBack {
                override fun onSuccess(list: ArrayList<FoodItem>) {
                    val rs = list[0]

                    val food = FoodItem(
                        rs.sugar_g,
                        rs.fiber_g,
                        rs.serving_size_g,
                        rs.sodium_mg,
                        rs.name,
                        rs.potassium_mg,
                        rs.fat_saturated_g,
                        rs.fat_total_g,
                        rs.calories,
                        rs.cholesterol_mg,
                        rs.protein_g,
                        rs.carbohydrates_total_g
                    )
                    newTvFoodNutritionNo.text = (index + 1).toString()
                    newTvFoodNutritionNameNo.text = food.name.capitalize()
                    newTvFoodNutritionQuantity.text = quantity.toString()
                    newTvFoodNutritionFat.text = (food.fat_total_g * quantity).toString()
                    newTvFoodNutritionCarbohydrates.text =
                        (food.carbohydrates_total_g * quantity).toString()
                    newTvFoodNutritionProtein.text = (food.protein_g * quantity).toString()

                    totalCaloriesConsumption += (food.calories * quantity)

                    newTvFoodNutritionCalories.text =
                        String.format("%.2f", totalCaloriesConsumption)

                    foodList.add(food)
                    nutritionRowContainer?.addView(newNutritionRow)

                    // Check if this is the last iteration
                    if (index == foodMap.size - 1) {
                        // If this is the last iteration, create the Meal object and add it to the database
                        val date = Date()
                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        val strDate: String = formatter.format(date)
                        val meal = Meal(strDate, args.mealType, foodList, totalCaloriesConsumption)
                        addMealsToDB(meal)
                    }
                }
            }, foodName)
        }
    }

    private fun addMealsToDB(meal: Meal) {
        nutritionViewModel.addMeals(meal)
    }
}