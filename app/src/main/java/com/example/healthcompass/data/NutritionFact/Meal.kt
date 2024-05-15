package com.example.healthcompass.data.NutritionFact
data class Meal(
    val date: String,
    val mealType: String,
    val foodItem: List<FoodItem>,
    val totalCaloriesConsumption: Double
)
