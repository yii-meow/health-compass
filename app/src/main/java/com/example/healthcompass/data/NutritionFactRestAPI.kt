package com.example.healthcompass.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NutritionFactRestAPI {
    companion object{
        val BASE_URL : String = "https://api.calorieninjas.com/v1/nutrition"
    }

    @GET("/")
    fun getFoodNutritionFact(@Query("query") foodName : String): Call<NutritionFactRespond>

}