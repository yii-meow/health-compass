package com.example.healthcompass.data
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NutritionFactRetrofitInstance {
    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(NutritionFactRestAPI.BASE_URL)
            .addConverterFactory(GsonConvertFactory.create())
            .build()
    }

    val api:NutritionFactRestAPI by lazy{
        retrofit.create(NutritionFactRestAPI::class.java)
    }
}