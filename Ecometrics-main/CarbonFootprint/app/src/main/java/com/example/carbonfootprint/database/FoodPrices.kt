package com.example.carbonfootprint.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FoodPrices {
    private lateinit var database: DatabaseReference
    data class FoodPrice(
        val foodPrice: Double
    )
    fun readFoodPrice(
        food: String,
        province: String,
        callback: (FoodPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("FoodPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val foodPrice = it.child(food+"Price").value.toString().toDouble()

                val provinceFoodPrice = FoodPrice(
                    foodPrice = foodPrice
                )
                callback.invoke(provinceFoodPrice)
            }else{
                readFoodPriceDefault(food){ price ->
                    val provinceFoodPrice = FoodPrice(
                        foodPrice = price.foodPrice
                    )
                    callback.invoke(provinceFoodPrice)
                }
            }
        }
    }

    fun readFoodPriceDefault(
        food: String,
        callback: (FoodPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("FoodPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val foodPrice = it.child(food+"Price").value.toString().toDouble()

                val provinceFoodPrice = FoodPrice(
                    foodPrice = foodPrice
                )
                callback.invoke(provinceFoodPrice)
            }
        }
    }
}