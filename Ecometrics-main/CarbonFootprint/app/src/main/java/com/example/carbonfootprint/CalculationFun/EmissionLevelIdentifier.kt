package com.example.carbonfootprint.CalculationFun

import android.content.Context
import com.example.carbonfootprint.ml.CategoryEmission
import com.example.carbonfootprint.ml.FoodEmission
import com.example.carbonfootprint.ml.HouseEmission
import com.example.carbonfootprint.ml.TravelEmission
import com.example.carbonfootprint.ml.VehicleEmission
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.roundToInt

class EmissionLevelIdentifier {
    fun emissionLevelIdentifier(category: String, subCategory: String, emission: Float, context: Context): Boolean {
        var array: Array<Float> = arrayOf()
        var decision = 0
        when (category) {
            "House" -> {
                if (subCategory == "Total") {
                    array = arrayOf(1.0f, 0.0f, 0.0f, 0.0f, emission)
                    decision = emissionLevelIdentifierModel(array = array, context = context)
                } else {
                    array = emissionHouseSubCategoryIdentifier(subCategory, emission)
                    decision = houseEmissionLevelIdentifierModel(array = array, context = context)
                }
            }

            "Food" -> {
                if (subCategory == "Total") {
                    array = arrayOf(0.0f, 1.0f, 0.0f, 0.0f, emission)
                    decision = emissionLevelIdentifierModel(array = array, context = context)
                } else {
                    array = emissionFoodSubCategoryIdentifier(subCategory, emission)
                    decision = foodEmissionLevelIdentifierModel(array = array, context = context)
                }

            }

            "Vehicle" -> {
                if (subCategory == "Total") {
                    array = arrayOf(0.0f, 0.0f, 1.0f, 0.0f, emission)
                    decision = emissionLevelIdentifierModel(array = array, context = context)
                } else {
                    array = emissionVehicleSubCategoryIdentifier(subCategory, emission)
                    decision = vehicleEmissionLevelIdentifierModel(array = array, context = context)
                }
            }

            "Travel" -> {
                if (subCategory == "Total") {
                    array = arrayOf(0.0f, 0.0f, 0.0f, 1.0f, emission)
                    decision = emissionLevelIdentifierModel(array = array, context = context)
                } else {
                    array = emissionTravelSubCategoryIdentifier(subCategory, emission)
                    decision = travelEmissionLevelIdentifierModel(array = array, context = context)
                }
            }
        }
        val decisions = arrayOf(false, true)

        return decisions[decision]
    }


    private fun emissionHouseSubCategoryIdentifier(subCategory: String, emission: Float): Array<Float> {
        var array: Array<Float> = arrayOf()
        when (subCategory) {
            "Electricity" -> {
                array = arrayOf(1.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "LPG" -> {
                array = arrayOf(0.0f, 1.0f, 0.0f, 0.0f, emission)
            }

            "Firewood" -> {
                array = arrayOf(0.0f, 0.0f, 1.0f, 0.0f, emission)
            }

            "Charcoal" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 1.0f, emission)
            }
        }
        return array
    }

    private fun emissionFoodSubCategoryIdentifier(subCategory: String, emission: Float): Array<Float> {
        var array: Array<Float> = arrayOf()
        when (subCategory) {
            "Meat" -> {
                array = arrayOf(1.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "Fish" -> {
                array = arrayOf(0.0f, 1.0f, 0.0f, 0.0f, emission)
            }

            "Crustaceans" -> {
                array = arrayOf(0.0f, 0.0f, 1.0f, 0.0f, emission)
            }

            "Mollusks" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 1.0f, emission)
            }
        }
        return array
    }

    private fun emissionVehicleSubCategoryIdentifier(subCategory: String, emission: Float): Array<Float> {
        var array: Array<Float> = arrayOf()
        when (subCategory) {
            "Diesel" -> {
                array = arrayOf(1.0f, 0.0f, 0.0f, emission)
            }

            "Unleaded" -> {
                array = arrayOf(0.0f, 1.0f, 0.0f, emission)
            }

            "Premium" -> {
                array = arrayOf(0.0f, 0.0f, 1.0f, emission)
            }
        }
        return array
    }

    private fun emissionTravelSubCategoryIdentifier(subCategory: String, emission: Float): Array<Float> {
        var array: Array<Float> = arrayOf()
        when (subCategory) {
            "Tricycle" -> {
                array = arrayOf(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "Jeep" -> {
                array = arrayOf(0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "E-Jeep" -> {
                array = arrayOf(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "Electric Jeep" -> {
                array = arrayOf(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "E-Jeep w/ Aircon" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "Airconditioned Electric Jeep" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "Bus" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "Economy Bus" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, emission)
            }

            "Bus w/ Aircon" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, emission)
            }

            "Airconditioned Bus" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, emission)
            }

            "Taxi" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, emission)
            }

            "UV Express Taxi" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, emission)
            }

            "UV Express" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, emission)
            }

            "UV Express Van" -> {
                array = arrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, emission)
            }
        }
        return array
    }

    private fun emissionLevelIdentifierModel(array: Array<Float>, context: Context):Int{
        val model = CategoryEmission.newInstance(context)

        val numElements = array.size

        val byteBuffer = ByteBuffer.allocateDirect(numElements * java.lang.Float.SIZE / java.lang.Byte.SIZE)
            .order(ByteOrder.nativeOrder())

        for (value in array) {
            byteBuffer.putFloat(value)
        }

        byteBuffer.rewind()

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 5), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        model.close()

        return confidences[0].roundToInt()
    }

    private fun houseEmissionLevelIdentifierModel(array: Array<Float>, context: Context):Int{
        val model = HouseEmission.newInstance(context)

        val numElements = array.size

        val byteBuffer = ByteBuffer.allocateDirect(numElements * java.lang.Float.SIZE / java.lang.Byte.SIZE)
            .order(ByteOrder.nativeOrder())

        for (value in array) {
            byteBuffer.putFloat(value)
        }

        byteBuffer.rewind()

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 5), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        model.close()

        return confidences[0].roundToInt()
    }

    private fun foodEmissionLevelIdentifierModel(array: Array<Float>, context: Context):Int{
        val model = FoodEmission.newInstance(context)

        val numElements = array.size

        val byteBuffer = ByteBuffer.allocateDirect(numElements * java.lang.Float.SIZE / java.lang.Byte.SIZE)
            .order(ByteOrder.nativeOrder())

        for (value in array) {
            byteBuffer.putFloat(value)
        }

        byteBuffer.rewind()

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 5), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        model.close()

        return confidences[0].roundToInt()
    }

    private fun vehicleEmissionLevelIdentifierModel(array: Array<Float>, context: Context):Int{
        val model = VehicleEmission.newInstance(context)

        val numElements = array.size

        val byteBuffer = ByteBuffer.allocateDirect(numElements * java.lang.Float.SIZE / java.lang.Byte.SIZE)
            .order(ByteOrder.nativeOrder())

        for (value in array) {
            byteBuffer.putFloat(value)
        }

        byteBuffer.rewind()

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 4), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        model.close()

        return confidences[0].roundToInt()
    }

    private fun travelEmissionLevelIdentifierModel(array: Array<Float>, context: Context):Int{
        val model = TravelEmission.newInstance(context)

        val numElements = array.size

        val byteBuffer = ByteBuffer.allocateDirect(numElements * java.lang.Float.SIZE / java.lang.Byte.SIZE)
            .order(ByteOrder.nativeOrder())

        for (value in array) {
            byteBuffer.putFloat(value)
        }

        byteBuffer.rewind()

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 10), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        model.close()

        return confidences[0].roundToInt()
    }
}