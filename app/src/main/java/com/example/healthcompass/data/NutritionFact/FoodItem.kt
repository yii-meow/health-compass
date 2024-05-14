package com.example.healthcompass.data.NutritionFact

data class FoodItem(
    val sugar_g: Double,
    val fiber_g: Double,
    val serving_size_g: Double,
    val sodium_mg: Int,
    val name: String,
    val potassium_mg: Int,
    val fat_saturated_g: Double,
    val fat_total_g: Double,
    val calories: Double,
    val cholesterol_mg: Int,
    val protein_g: Double,
    val carbohydrates_total_g: Double
)
