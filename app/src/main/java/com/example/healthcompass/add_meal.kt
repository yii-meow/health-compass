package com.example.healthcompass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class add_meal : Fragment() {
    private val args by navArgs<add_mealArgs>()
    private lateinit var nutritionFactViewModel : NutritionFactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_meal, container, false)

        val tvMealType: TextView = view.findViewById(R.id.tvMealType)
        tvMealType.text = args.mealType

        // TODO: Retrieve food from api
        val foodSpinner : Spinner = view.findViewById(R.id.spinnerSelectFood)


        // TODO: Retrieve Nutrition Fact of the food
        nutritionFactViewModel = ViewModelProvider(this).get(NutritionFactViewModel::class.java)
        val imgAddFood : ImageView = view.findViewById(R.id.imgAddFood)

        imgAddFood.setOnClickListener {
            nutritionFactViewModel.getNutritionFact(object: OnRequestCompleteCallBack{
                override fun onSuccess(list: ArrayList<FoodItem>) {
                    Toast.makeText(requireContext(),"Retrieved nutrition fact $list",Toast.LENGTH_LONG).show()
                }
            },"apple")
        }

        val btnMakeMealChanges: Button = view.findViewById(R.id.btnMakeMealChanges)
        btnMakeMealChanges.setOnClickListener {
            findNavController().navigate(R.id.action_add_meal_to_nutrition)
        }

        return view
    }
}