package com.example.carbonfootprint.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FoodEmission {
    private lateinit var database: DatabaseReference
    data class FoodEmission(
        val foodEmission: Double
    )
    fun readFoodEmission(
        category: String,
        callback: (FoodEmission) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("FoodEmissionFactor")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val foodEmission = it.child(category+"EF").value.toString().toDouble()

                val provinceFoodEmission = FoodEmission(
                    foodEmission = foodEmission
                )
                callback.invoke(provinceFoodEmission)
            }
        }
    }
}