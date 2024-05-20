package com.example.healthcompass.data.NutritionFact

data class FoodItem(
    val sugar_g: Double = 0.0,
    val fiber_g: Double = 0.0,
    val serving_size_g: Double = 0.0,
    val sodium_mg: Int = 0,
    val name: String = "",
    val potassium_mg: Int = 0,
    val fat_saturated_g: Double = 0.0,
    val fat_total_g: Double = 0.0,
    val calories: Double = 0.0,
    val cholesterol_mg: Int = 0,
    val protein_g: Double = 0.0,
    val carbohydrates_total_g: Double = 0.0
)
