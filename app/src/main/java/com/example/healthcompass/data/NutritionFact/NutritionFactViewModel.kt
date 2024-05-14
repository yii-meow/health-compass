package com.example.healthcompass.data.NutritionFact

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NutritionFactViewModel(application: Application) : AndroidViewModel(application) {
    fun getNutritionFact(callBack: OnRequestCompleteCallBack, foodName: String) {
        val apiKey = "oVoTvHEaCQNZqWcLKooNsw==571TRwPspaNGCG49"
        val call = NutritionFactRetrofitInstance.api.getFoodNutritionFact(foodName, apiKey)

        call.enqueue(object : Callback<NutritionFactRespond> {
            override fun onResponse(
                p0: Call<NutritionFactRespond>,
                p1: Response<NutritionFactRespond>
            ) {
                var foodNutritionFactList = arrayListOf<FoodItem>()
                val rs: NutritionFactRespond? = p1.body()

                val nutritionFact = FoodItem(
                    rs!!.items.get(0).sugar_g,
                    rs!!.items.get(0).fiber_g,
                    rs!!.items.get(0).serving_size_g,
                    rs!!.items.get(0).sodium_mg,
                    rs!!.items.get(0).name,
                    rs!!.items.get(0).potassium_mg,
                    rs!!.items.get(0).fat_saturated_g,
                    rs!!.items.get(0).fat_total_g,
                    rs!!.items.get(0).calories,
                    rs!!.items.get(0).cholesterol_mg,
                    rs!!.items.get(0).protein_g,
                    rs!!.items.get(0).carbohydrates_total_g
                )

                foodNutritionFactList.add(nutritionFact)

                callBack?.onSuccess(foodNutritionFactList)
            }

            override fun onFailure(p0: Call<NutritionFactRespond>, p1: Throwable) {
                Toast.makeText(
                    getApplication(),
                    "Fail to get food nutrition fact",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}

interface OnRequestCompleteCallBack {
    fun onSuccess(list: ArrayList<FoodItem>)
}