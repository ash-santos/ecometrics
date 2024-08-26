package com.example.carbonfootprint.CalculationFun

import com.example.carbonfootprint.database.EmissionFactors
import com.example.carbonfootprint.database.FoodEmission
import com.example.carbonfootprint.database.FoodPrices
import com.example.carbonfootprint.database.ProductPrices

class EmissionCalculation {

    data class Emission(
        var house: Double,
        var food: Double,
        var vehicle: Double,
        var travel: Double
    )

    data class AverageEmission(
        var house: Boolean,
        var food: Boolean,
        var vehicle: Boolean,
        var travel: Boolean
    )

    fun totalEmission(userEmission: Emission): Double {
        return userEmission.house + userEmission.food + userEmission.vehicle + userEmission.travel
    }

    //Calculation for Electricity Carbon Emission base on amount of Electricity Used
    fun calculateElectricityAmountEmissions(
        province: String,
        amount: Double,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince {
            val emissionFactor = it.electricityEF

            val quantity = amount

            val result = emissionFactor * quantity

            callback.invoke(result)
        }
    }

    //Calculation for Electricity Carbon Emission base on Electricity Bill
    fun calculateElectricityBillEmissions(
        province: String,
        amount: Double,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readEmissionPriceForProvince(province) {
                val emissionFactor = ef.electricityEF

                var amountPerKwh = it.electricityPrice

                val quantity = amount / amountPerKwh

                val result = emissionFactor * quantity

                callback.invoke(result)
            }
        }
    }

    fun calculateLPGEmissions(province: String, amount: Double, callback: (Double) -> Unit) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readEmissionPriceForProvince(province) {
                val emissionFactor = ef.lpgEF

                var amountPerKg = it.lpgPrice


                val quantity = amount / amountPerKg

                val result = emissionFactor * quantity
                callback.invoke(result)
            }
        }
    }

    fun calculateCharcoalEmissions(province: String, amount: Double, callback: (Double) -> Unit) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readEmissionPriceForProvince(province) {
                val emissionFactor = ef.charcoalEF

                var amountPerKg = it.charcoalPrice

                val quantity = amount / amountPerKg

                val result = emissionFactor * quantity

                callback.invoke(result)
            }
        }
    }

    fun calculateFirewoodEmissions(province: String, kg: Double, callback: (Double) -> Unit) {
        EmissionFactors().readEmissionFactorForProvince {
            val emissionFactor = it.firewoodEF

            val result = emissionFactor * kg

            callback.invoke(result)
        }
    }

    fun calculateUnleadedGasEmissions(
        province: String,
        amount: Double,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readEmissionPriceForProvince(province) {
                val emissionFactor = ef.unleadedGasEF

                var amountPerLiter = it.unleadedGasPrice

                val quantity = amount / amountPerLiter

                var result = emissionFactor * quantity

                callback.invoke(result)
            }
        }
    }

    fun calculatePUBUnleadedGasEmissions(
        province: String,
        amount: Double,
        discount: Boolean,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readPUBPriceForProvince(province) {
                val emissionFactor = ef.unleadedGasEF
                var consumptionPerKM = 0.2
                var firstKm = it.minimumPrice
                var perKm = it.ratePrice
                var km = 5.0

                if (discount) {
                    firstKm = it.disctMinimumPrice
                    perKm = it.disctRatePrice
                }

                var calculatedAmount = amount - firstKm

                km += calculatedAmount / perKm

                val consumption = consumptionPerKM * km

                var result = (emissionFactor * consumption) / 30

                if(amount < it.minimumPrice){
                    result = -1.0
                }

                if(discount && amount < it.disctMinimumPrice){
                    result = -1.0
                }

                callback.invoke(result)
            }
        }
    }

    fun calculatePUBEUnleadedGasEmissions(
        province: String,
        amount: Double,
        discount: Boolean,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readPUBAPriceForProvince(province) {
                val emissionFactor = ef.unleadedGasEF
                var consumptionPerKM = 0.2
                var firstKm = it.minimumPrice
                var perKm = it.ratePrice
                var km = 5.0

                if (discount) {
                    firstKm = it.disctMinimumPrice
                    perKm = it.disctRatePrice
                }

                var calculatedAmount = amount - firstKm

                km += calculatedAmount / perKm

                val consumption = consumptionPerKM * km

                var result = (emissionFactor * consumption) / 45

                if(amount < it.minimumPrice){
                    result = -1.0
                }

                if(discount && amount < it.disctMinimumPrice){
                    result = -1.0
                }

                callback.invoke(result)
            }
        }
    }

    fun calculatePUBAUnleadedGasEmissions(
        province: String,
        amount: Double,
        discount: Boolean,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readPUBAPriceForProvince(province) {
                val emissionFactor = ef.unleadedGasEF
                var consumptionPerKM = 0.2
                var firstKm = it.minimumPrice
                var perKm = it.ratePrice
                var km = 5.0

                if (discount) {
                    firstKm = it.disctMinimumPrice
                    perKm = it.disctRatePrice
                }

                var calculatedAmount = amount - firstKm

                km += calculatedAmount / perKm

                val consumption = consumptionPerKM * km

                var result = (emissionFactor * consumption) / 45

                if(amount < it.minimumPrice){
                    result = -1.0
                }

                if(discount && amount < it.disctMinimumPrice){
                    result = -1.0
                }

                callback.invoke(result)
            }
        }
    }

    fun calculateTrikeUnleadedGasEmissions(
        province: String,
        amount: Double,
        discount: Boolean,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readTricyclePriceForProvince(province) {
                val emissionFactor = ef.unleadedGasEF
                var consumptionPerKM = 0.4
                var firstKm = it.minimumPrice
                var perKm = it.ratePrice
                var km = 2.0

                if (discount) {
                    firstKm = it.disctMinimumPrice
                    perKm = it.disctRatePrice
                }

                var calculatedAmount = amount - firstKm

                km += (calculatedAmount / perKm)

                val consumption = consumptionPerKM * km

                var result = (emissionFactor * consumption) / 3

                if(amount < it.minimumPrice){
                    result = -1.0
                }

                if(discount && amount < it.disctMinimumPrice){
                    result = -1.0
                }

                callback.invoke(result)
            }
        }
    }

    fun calculatePremiumGasEmissions(province: String, amount: Double, callback: (Double) -> Unit) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readEmissionPriceForProvince(province) {
                val emissionFactor = ef.premiumGasEF

                var amountPerLiter = it.premiumGasPrice

                val quantity = amount / amountPerLiter

                var result = emissionFactor * quantity

                callback.invoke(result)
            }
        }
    }

    fun calculateDieselEmissions(province: String, amount: Double, callback: (Double) -> Unit) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readEmissionPriceForProvince(province) {
                val emissionFactor = ef.dieselGasEF

                var amountPerLiter = it.dieselGasPrice

                val conversionFactor = 0.63

                val quantity = amount / amountPerLiter

                val result = emissionFactor * quantity * conversionFactor

                callback.invoke(result)
            }
        }
    }

    fun calculateDieselEmissions(
        province: String,
        amount: Double,
        discount: Boolean,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readPUJPriceForProvince(province) { price ->
                var consumptionPerKM = 0.15

                val emissionFactor = ef.dieselGasEF
                var firstKm = price.minimumPrice
                var perKm = price.ratePrice
                var km = 4.0

                // Adjust values based on discount
                if (discount) {
                    firstKm = price.disctMinimumPrice
                    perKm = price.disctRatePrice
                }

                var calculatedAmount = (amount - firstKm)

                km += (calculatedAmount / perKm)

                val consumption = consumptionPerKM * km

                var result = (emissionFactor * consumption) / 16

                if(amount < price.minimumPrice){
                    result = -1.0
                }

                if(discount && amount < price.disctMinimumPrice){
                    result = -1.0
                }

                callback.invoke(result)
            }
        }
    }

    fun calculateFoodEmissions(province: String,food: String, category: String, amount: Double, customFoodPrice: Boolean, foodPrice: Int, callback: (Double) -> Unit){
        FoodEmission().readFoodEmission(category = category){emission ->
            FoodPrices().readFoodPrice(province = province, food = food){price ->
                val emissionFactor = emission.foodEmission

                val amountPerKG = if(customFoodPrice){
                    foodPrice
                }else{
                    price.foodPrice
                }

                val quantity = amount / amountPerKG.toDouble()

                val result = emissionFactor * quantity

                callback.invoke(result)
            }
        }
    }

    fun calculateElectricPUJEmission(
        province: String,
        amount: Double,
        discount: Boolean,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readEPUJPriceForProvince(province) {
                var consumptionPerKM = 1.0

                val emissionFactor = ef.electricityEF

                var firstKm = it.minimumPrice
                var amountPerKm = it.ratePrice
                var km = 4

                if (discount) {
                    firstKm = it.disctMinimumPrice
                    amountPerKm = it.disctRatePrice
                }

                var calculatedAmount = amount - firstKm

                km += (calculatedAmount / amountPerKm).toInt()

                val consumption = consumptionPerKM * km

                var result = (emissionFactor * consumption) / 20

                if(amount < it.minimumPrice){
                    result = -1.0
                }

                if(discount && amount < it.disctMinimumPrice){
                    result = -1.0
                }

                callback.invoke(result)
            }
        }
    }

    fun calculateElectricPUJAEmission(
        province: String,
        amount: Double,
        discount: Boolean,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince { ef ->
            ProductPrices().readEPUJAPriceForProvince(province) {
                var consumptionPerKM = 1.0

                val emissionFactor = ef.electricityEF

                var firstKm = it.minimumPrice
                var amountPerKm = it.ratePrice
                var km = 4

                if (discount) {
                    firstKm = it.disctMinimumPrice
                    amountPerKm = it.disctRatePrice
                }

                var calculatedAmount = amount - firstKm

                km += (calculatedAmount / amountPerKm).toInt()

                val consumption = consumptionPerKM * km

                var result = (emissionFactor * consumption) / 20

                if(amount < it.minimumPrice){
                    result = -1.0
                }

                if(discount && amount < it.disctMinimumPrice){
                    result = -1.0
                }

                callback.invoke(result)
            }
        }
    }

    fun calculatePremiumGasEmissions(
        province: String,
        km: Double,
        efficiency: Double,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince {
            val emissionFactor = it.premiumGasEF

            var consumptionPerKM = efficiency

            val consumption = consumptionPerKM * km

            val result = emissionFactor * consumption
            callback.invoke(result)
        }
    }

    fun calculateUnleadedGasEmissions(
        province: String,
        km: Double,
        efficiency: Double,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince {
            val emissionFactor = it.unleadedGasEF

            var consumptionPerKM = efficiency

            val consumption = consumptionPerKM * km

            val result = emissionFactor * consumption
            callback.invoke(result)
        }
    }

    fun calculateDieselGasEmissions(
        province: String,
        km: Double,
        efficiency: Double,
        callback: (Double) -> Unit
    ) {
        EmissionFactors().readEmissionFactorForProvince {
            val emissionFactor = it.dieselGasEF

            var consumptionPerKM = efficiency

            val consumption = consumptionPerKM * km

            val result = emissionFactor * consumption
            callback.invoke(result)
        }
    }
}