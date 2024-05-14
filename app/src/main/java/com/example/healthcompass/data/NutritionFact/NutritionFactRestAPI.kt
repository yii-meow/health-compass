package com.example.healthcompass.data.NutritionFact

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NutritionFactRestAPI {
    companion object {
        val BASE_URL: String = "https://api.calorieninjas.com/"
    }

    @GET("v1/nutrition")
    fun getFoodNutritionFact(
        @Query("query") foodName: String,
        @Header("X-Api-Key") apiKey: String
    ): Call<NutritionFactRespond>

}