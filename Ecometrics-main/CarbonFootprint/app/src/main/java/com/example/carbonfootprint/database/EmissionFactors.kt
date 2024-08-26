package com.example.carbonfootprint.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EmissionFactors {
    private lateinit var database: DatabaseReference

    data class EmissionFactorForProvince(
        val electricityEF: Double,
        val charcoalEF: Double,
        val lpgEF: Double,
        val unleadedGasEF: Double,
        val premiumGasEF: Double,
        val dieselGasEF: Double,
        val firewoodEF: Double
    )

    data class AverageCarbonEmissionForProvince(
        val electricityAverage: Double,
        val charcoalAverage: Double,
        val lpgAverage: Double,
        val unleadedGasAverage: Double,
        val premiumGasAverage: Double,
        val dieselGasAverage: Double,
        val firewoodAverage: Double
    )

    fun readEmissionFactorForProvince(callback: (EmissionFactorForProvince) -> Unit) {
        database = FirebaseDatabase.getInstance().getReference("EmissionFactor")
        database.child("Default").get().addOnSuccessListener {
            if (it.exists()) {
                val electricityEF = it.child(" Electricity").value.toString().toDouble()
                val charcoalEF = it.child("Charcoal").value.toString().toDouble()
                val lpgEF = it.child("LPG").value.toString().toDouble()
                val unleadedGasEF = it.child("UnleadedGas").value.toString().toDouble()
                val premiumGasEF = it.child("PremiumGas").value.toString().toDouble()
                val dieselGasEF = it.child("DieselGas").value.toString().toDouble()
                val firewoodEF = it.child("Firewood").value.toString().toDouble()

                val provinceData = EmissionFactorForProvince(
                    electricityEF,
                    charcoalEF,
                    lpgEF,
                    unleadedGasEF,
                    premiumGasEF,
                    dieselGasEF,
                    firewoodEF
                )
                callback.invoke(provinceData)
            } else {
                //Insert Default value here
            }
        }
    }

    fun readAverageCarbonEmissionForProvince(province: String, callback: (AverageCarbonEmissionForProvince) -> Unit) {
        database = FirebaseDatabase.getInstance().getReference("AverageCarbonEmission")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val electricityAverage = it.child("AverageElectricity").value.toString().toDouble()
                val charcoalAverage = it.child("AverageCharcoal").value.toString().toDouble()
                val lpgAverage = it.child("AverageLPGPrice").value.toString().toDouble()
                val unleadedGasAverage = it.child("AverageUnleadedGas").value.toString().toDouble()
                val premiumGasAverage = it.child("AveragePremiumGas").value.toString().toDouble()
                val dieselGasAverage = it.child("AverageDieselGas").value.toString().toDouble()
                val firewoodAverage = it.child("AverageFirewood").value.toString().toDouble()

                val provinceData = AverageCarbonEmissionForProvince(
                    electricityAverage, 
                    charcoalAverage,
                    lpgAverage,
                    unleadedGasAverage,
                    premiumGasAverage,
                    dieselGasAverage,
                    firewoodAverage
                )
                callback.invoke(provinceData)
            } else {
                readAverageCarbonEmissionDefault() {
                    val provinceData = AverageCarbonEmissionForProvince(
                        electricityAverage = it.electricityAverage,
                        charcoalAverage = it.electricityAverage,
                        lpgAverage = it.electricityAverage,
                        unleadedGasAverage = it.electricityAverage,
                        premiumGasAverage = it.electricityAverage,
                        dieselGasAverage = it.electricityAverage,
                        firewoodAverage = it.electricityAverage
                    )
                    callback.invoke(provinceData)
                }
            }
        }
    }

    fun readAverageCarbonEmissionDefault(callback: (AverageCarbonEmissionForProvince) -> Unit) {
        database = FirebaseDatabase.getInstance().getReference("AverageCarbonEmission")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val electricityAverage = it.child("AverageElectricity").value.toString().toDouble()
                val charcoalAverage = it.child("AverageCharcoal").value.toString().toDouble()
                val lpgAverage = it.child("AverageLPGPrice").value.toString().toDouble()
                val unleadedGasAverage = it.child("AverageUnleadedGas").value.toString().toDouble()
                val premiumGasAverage = it.child("AveragePremiumGas").value.toString().toDouble()
                val dieselGasAverage = it.child("AverageDieselGas").value.toString().toDouble()
                val firewoodAverage = it.child("AverageFirewood").value.toString().toDouble()

                val provinceData = AverageCarbonEmissionForProvince(
                    electricityAverage,
                    charcoalAverage,
                    lpgAverage,
                    unleadedGasAverage,
                    premiumGasAverage,
                    dieselGasAverage,
                    firewoodAverage
                )
                callback.invoke(provinceData)
            }
        }
    }
}