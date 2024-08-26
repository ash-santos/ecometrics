package com.example.carbonfootprint

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.carbonfootprint.CalculationFun.EmissionCalculation
import com.example.carbonfootprint.CalculationFun.EmissionLevelIdentifier
import com.example.carbonfootprint.database.FirebaseQuery
import com.example.carbonfootprint.databinding.DailyCheckupResultBinding
import com.example.carbonfootprint.databinding.FirstTimeSignInResultBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import java.util.Calendar


class DailyCheckUpResult : AppCompatActivity() {
    lateinit var binding: DailyCheckupResultBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DailyCheckupResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val user = auth.currentUser

        user?.let {
            FirebaseQuery().readUserDailyEmission(it, year, month, day) { emission ->

                val houseEmission = emission.houseEmission

                val foodEmission = emission.foodEmission

                val vehicleEmission = emission.vehicleEmission

                val travelEmission = emission.travelEmission

                val userEmissionData = EmissionCalculation.Emission(
                    house = houseEmission,
                    food = foodEmission,
                    vehicle = vehicleEmission,
                    travel = travelEmission
                )

                val total = EmissionCalculation().totalEmission(userEmissionData)

                binding.lblTotalEmission.text = "${"%.2f".format(total)} kg of CO2e"

                // start of pie code
                val pieChart = binding.pieChart
                pieChart.description.isEnabled = false

                val values = mutableListOf<PieEntry>()

                val color = mutableListOf<Int>()

                var emissionArray: Array<EmissionHighLevel> = arrayOf()

                if (houseEmission > 0) {
                    val percentageFormat = (houseEmission / total) * 100

                    val emissionPercent = if(percentageFormat == 100.00){
                        "${percentageFormat.toInt()}%"
                    }else{
                        "${"%.2f".format(percentageFormat)}%"
                    }

                    val resourceId =
                        getResources().getIdentifier("new_icon_home", "mipmap", getPackageName())

                    val newEntry =
                        EmissionHighLevel("House", houseEmission, emissionPercent, resourceId)

                    emissionArray = emissionArray.plus(newEntry)
                }

                if (foodEmission > 0) {
                    val percentageFormat = (foodEmission / total) * 100

                    val emissionPercent = if(percentageFormat == 100.00){
                        "${percentageFormat.toInt()}%"
                    }else{
                        "${"%.2f".format(percentageFormat)}%"
                    }

                    val resourceId =
                        getResources().getIdentifier("new_icon_food", "mipmap", getPackageName())

                    val newEntry =
                        EmissionHighLevel("Food", foodEmission, emissionPercent, resourceId)

                    emissionArray = emissionArray.plus(newEntry)
                }

                if (vehicleEmission > 0) {
                    val percentageFormat = (vehicleEmission / total) * 100

                    val emissionPercent = if(percentageFormat == 100.00){
                        "${percentageFormat.toInt()}%"
                    }else{
                        "${"%.2f".format(percentageFormat)}%"
                    }

                    val resourceId =
                        getResources().getIdentifier("new_icon_vehicle", "mipmap", getPackageName())

                    val newEntry =
                        EmissionHighLevel("Vehicle", vehicleEmission, emissionPercent, resourceId)

                    emissionArray = emissionArray.plus(newEntry)
                }

                if (travelEmission > 0) {
                    val percentageFormat = (travelEmission / total) * 100

                    val emissionPercent = if(percentageFormat == 100.00){
                        "${percentageFormat.toInt()}%"
                    }else{
                        "${"%.2f".format(percentageFormat)}%"
                    }

                    val resourceId =
                        getResources().getIdentifier("new_icon_travel", "mipmap", getPackageName())

                    val newEntry =
                        EmissionHighLevel("Travel", travelEmission, emissionPercent, resourceId)

                    emissionArray = emissionArray.plus(newEntry)
                }

                emissionArray.sortByDescending { it.emission }

                val high = ContextCompat.getColor(this, com.example.carbonfootprint.R.color.high)

                val context = applicationContext

                if (emissionArray.isNotEmpty() && emissionArray[0].emission > 0) {
                    values.add(
                        PieEntry(
                            emissionArray[0].emission.toFloat(),
                            emissionArray[0].category
                        )
                    )
                    color.add(Color.parseColor("#006d2c"))
                    binding.iconHighLevelNumber1.setImageResource(emissionArray[0].resourceID)
                    if (EmissionLevelIdentifier().emissionLevelIdentifier(
                            emissionArray[0].category,
                            "Total",
                            emissionArray[0].emission.toFloat(),
                            context
                        )
                    ) {
                        binding.txtHighLevelNumber1.setTextColor(high)
                        binding.percentHighLevelNumber1.setTextColor(high)
                    }
                    binding.txtHighLevelNumber1.text = emissionArray[0].category
                    binding.percentHighLevelNumber1.text = emissionArray[0].percentEmission
                } else {
                    binding.rowHighLevelNumber1.visibility = View.GONE
                }

                if (emissionArray.size > 1 && emissionArray[1].emission > 0) {
                    values.add(
                        PieEntry(
                            emissionArray[1].emission.toFloat(),
                            emissionArray[1].category
                        )
                    )
                    color.add(Color.parseColor("#2ca25f"))
                    binding.iconHighLevelNumber2.setImageResource(emissionArray[1].resourceID)
                    if (EmissionLevelIdentifier().emissionLevelIdentifier(
                            emissionArray[1].category,
                            "Total",
                            emissionArray[1].emission.toFloat(),
                            context
                        )
                    ) {
                        binding.txtHighLevelNumber2.setTextColor(high)
                        binding.percentHighLevelNumber2.setTextColor(high)
                    }
                    binding.txtHighLevelNumber2.text = emissionArray[1].category
                    binding.percentHighLevelNumber2.text = emissionArray[1].percentEmission
                } else {
                    binding.rowHighLevelNumber2.visibility = View.GONE
                }

                if (emissionArray.size > 2 && emissionArray[2].emission > 0) {
                    binding.iconHighLevelNumber3.setImageResource(emissionArray[2].resourceID)
                    values.add(
                        PieEntry(
                            emissionArray[2].emission.toFloat(),
                            emissionArray[2].category
                        )
                    )
                    color.add(Color.parseColor("#66c2a4"))
                    if (EmissionLevelIdentifier().emissionLevelIdentifier(
                            emissionArray[2].category,
                            "Total",
                            emissionArray[2].emission.toFloat(),
                            context
                        )
                    ) {
                        binding.txtHighLevelNumber3.setTextColor(high)
                        binding.percentHighLevelNumber3.setTextColor(high)
                    }
                    binding.txtHighLevelNumber3.text = emissionArray[2].category
                    binding.percentHighLevelNumber3.text = emissionArray[2].percentEmission
                } else {
                    binding.rowHighLevelNumber3.visibility = View.GONE
                }

                if (emissionArray.size > 3 && emissionArray[3].emission > 0) {
                    values.add(
                        PieEntry(
                            emissionArray[3].emission.toFloat(),
                            emissionArray[3].category
                        )
                    )
                    color.add(Color.parseColor("#b2e2e2"))
                    binding.iconHighLevelNumber4.setImageResource(emissionArray[3].resourceID)
                    if (EmissionLevelIdentifier().emissionLevelIdentifier(
                            emissionArray[3].category,
                            "Total",
                            emissionArray[3].emission.toFloat(),
                            context
                        )
                    ) {
                        binding.txtHighLevelNumber4.setTextColor(high)
                        binding.percentHighLevelNumber4.setTextColor(high)
                    }
                    binding.txtHighLevelNumber4.text = emissionArray[3].category
                    binding.percentHighLevelNumber4.text = emissionArray[3].percentEmission
                } else {
                    binding.rowHighLevelNumber4.visibility = View.GONE
                }

                // Create a DataSet
                val dataSet = PieDataSet(values, "")

                // pie color
                dataSet.colors = color

                // Create a PieData object
                val data = PieData(dataSet)
                pieChart.holeRadius = 0f
                pieChart.transparentCircleRadius = 0f
                data.setValueTextSize(0f)
                data.setValueTextColor(Color.WHITE)
                pieChart.legend.textSize = 16f;
                pieChart.setDrawEntryLabels(false)
                pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL

                pieChart.data = data
                pieChart.invalidate()
                //end of pie code
            }
        }

        fun emissionLayoutIdentifier(category: String) {
            when (category) {
                "House" -> {
                    user?.let {
                        FirebaseQuery().readUserDailyHouseEmission(it, year, month, day) { emission ->
                            var houseEmission = emission.electricityEmission + emission.charcoalEmission + emission.lpgEmission + emission.firewoodEmission
                            var electricityEmission = emission.electricityEmission
                            var charcoalEmission = emission.charcoalEmission
                            var lpgEmission = emission.lpgEmission
                            var firewoodEmission = emission.firewoodEmission

                            openView(
                                openView = binding.layoutResultHouse,
                                closeView = binding.layoutResult
                            )

                            binding.lblTotalHouseEmission.text =
                                "${"%.2f".format(houseEmission)} kg of CO2e"

                            // start of pie code
                            val pieChart = binding.pieChartHouse

                            pieChart.description.isEnabled = false

                            val values = mutableListOf<PieEntry>()

                            val color = mutableListOf<Int>()

                            var emissionArray: Array<EmissionHighLevel> = arrayOf()

                            if (electricityEmission > 0) {
                                val percentageFormat = (electricityEmission / houseEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_electricity",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Electricity",
                                    electricityEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (charcoalEmission > 0) {
                                val percentageFormat = (charcoalEmission / houseEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_charcoal",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Charcoal",
                                    charcoalEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (lpgEmission > 0) {
                                val percentageFormat = (lpgEmission / houseEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_lpg",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry =
                                    EmissionHighLevel(
                                        "LPG",
                                        lpgEmission,
                                        emissionPercent,
                                        resourceId
                                    )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (firewoodEmission > 0) {
                                val percentageFormat = (firewoodEmission / houseEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_firewood",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Firewood",
                                    firewoodEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            emissionArray.sortByDescending { it.emission }

                            val high =
                                ContextCompat.getColor(
                                    this,
                                    com.example.carbonfootprint.R.color.high
                                )

                            val context = applicationContext

                            if (emissionArray.isNotEmpty() && emissionArray[0].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[0].emission.toFloat(),
                                        emissionArray[0].category
                                    )
                                )
                                color.add(Color.parseColor("#006d2c"))
                                binding.iconHouseHighLevelNumber1.setImageResource(emissionArray[0].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[0].category,
                                        emissionArray[0].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtHouseHighLevelNumber1.setTextColor(high)
                                    binding.percentHouseHighLevelNumber1.setTextColor(high)
                                }
                                binding.txtHouseHighLevelNumber1.text = emissionArray[0].category
                                binding.percentHouseHighLevelNumber1.text =
                                    emissionArray[0].percentEmission
                            } else {
                                binding.rowHouseHighLevelNumber1.visibility = View.GONE
                            }

                            if (emissionArray.size > 1 && emissionArray[1].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[1].emission.toFloat(),
                                        emissionArray[1].category
                                    )
                                )
                                color.add(Color.parseColor("#2ca25f"))
                                binding.iconHouseHighLevelNumber2.setImageResource(emissionArray[1].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[1].category,
                                        emissionArray[1].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtHouseHighLevelNumber2.setTextColor(high)
                                    binding.percentHouseHighLevelNumber2.setTextColor(high)
                                }
                                binding.txtHouseHighLevelNumber2.text = emissionArray[1].category
                                binding.percentHouseHighLevelNumber2.text =
                                    emissionArray[1].percentEmission
                            } else {
                                binding.rowHouseHighLevelNumber2.visibility = View.GONE
                            }

                            if (emissionArray.size > 2 && emissionArray[2].emission > 0) {
                                binding.iconHouseHighLevelNumber3.setImageResource(emissionArray[2].resourceID)
                                values.add(
                                    PieEntry(
                                        emissionArray[2].emission.toFloat(),
                                        emissionArray[2].category
                                    )
                                )
                                color.add(Color.parseColor("#66c2a4"))
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[2].category,
                                        emissionArray[2].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtHouseHighLevelNumber3.setTextColor(high)
                                    binding.percentHouseHighLevelNumber3.setTextColor(high)
                                }
                                binding.txtHouseHighLevelNumber3.text = emissionArray[2].category
                                binding.percentHouseHighLevelNumber3.text =
                                    emissionArray[2].percentEmission
                            } else {
                                binding.rowHouseHighLevelNumber3.visibility = View.GONE
                            }

                            if (emissionArray.size > 3 && emissionArray[3].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[3].emission.toFloat(),
                                        emissionArray[3].category
                                    )
                                )
                                color.add(Color.parseColor("#b2e2e2"))
                                binding.iconHouseHighLevelNumber4.setImageResource(emissionArray[3].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[3].category,
                                        emissionArray[3].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtHouseHighLevelNumber4.setTextColor(high)
                                    binding.percentHouseHighLevelNumber4.setTextColor(high)
                                }
                                binding.txtHouseHighLevelNumber4.text = emissionArray[3].category
                                binding.percentHouseHighLevelNumber4.text =
                                    emissionArray[3].percentEmission
                            } else {
                                binding.rowHouseHighLevelNumber4.visibility = View.GONE
                            }

                            // Create a DataSet
                            val dataSet = PieDataSet(values, "")

                            // pie color
                            dataSet.colors = color

                            // Create a PieData object
                            val data = PieData(dataSet)
                            pieChart.holeRadius = 0f
                            pieChart.transparentCircleRadius = 0f
                            data.setValueTextSize(0f)
                            data.setValueTextColor(Color.WHITE)
                            pieChart.legend.textSize = 16f;
                            pieChart.setDrawEntryLabels(false)
                            pieChart.legend.horizontalAlignment =
                                Legend.LegendHorizontalAlignment.RIGHT
                            pieChart.legend.verticalAlignment =
                                Legend.LegendVerticalAlignment.CENTER
                            pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL

                            pieChart.data = data
                            pieChart.invalidate()
                        }
                    }
                }

                "Food" -> {
                    user?.let {
                        FirebaseQuery().readUserDailyFoodEmission(it, year, month, day) { emission ->
                            var foodEmission = emission.meatEmission + emission.crustaceansEmission + emission.mollusksEmission + emission.fishEmission
                            var meatEmission = emission.meatEmission
                            var crustaceansEmission = emission.crustaceansEmission
                            var fishEmission = emission.fishEmission
                            var mollusksEmission = emission.mollusksEmission

                            openView(
                                openView = binding.layoutResultFood,
                                closeView = binding.layoutResult
                            )

                            binding.lblTotalFoodEmission.text =
                                "${"%.2f".format(foodEmission)} kg of CO2e"

                            // start of pie code
                            val pieChart = binding.pieChartFood

                            pieChart.description.isEnabled = false

                            val values = mutableListOf<PieEntry>()

                            val color = mutableListOf<Int>()

                            var emissionArray: Array<EmissionHighLevel> = arrayOf()

                            if (meatEmission > 0) {
                                val percentageFormat = (meatEmission / foodEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_meat",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry =
                                    EmissionHighLevel(
                                        "Meat",
                                        meatEmission,
                                        emissionPercent,
                                        resourceId
                                    )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (fishEmission > 0) {
                                val percentageFormat = (fishEmission / foodEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_fish",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry =
                                    EmissionHighLevel(
                                        "Fish",
                                        fishEmission,
                                        emissionPercent,
                                        resourceId
                                    )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (crustaceansEmission > 0) {
                                val percentageFormat = (crustaceansEmission / foodEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_crustacean",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Crustaceans",
                                    crustaceansEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (mollusksEmission > 0) {
                                val percentageFormat = (mollusksEmission / foodEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_mollusk",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Mollusks",
                                    mollusksEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            emissionArray.sortByDescending { it.emission }

                            val high =
                                ContextCompat.getColor(
                                    this,
                                    com.example.carbonfootprint.R.color.high
                                )

                            val context = applicationContext

                            if (emissionArray.isNotEmpty() && emissionArray[0].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[0].emission.toFloat(),
                                        emissionArray[0].category
                                    )
                                )
                                color.add(Color.parseColor("#006d2c"))
                                binding.iconFoodHighLevelNumber1.setImageResource(emissionArray[0].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[0].category,
                                        emissionArray[0].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtFoodHighLevelNumber1.setTextColor(high)
                                    binding.percentFoodHighLevelNumber1.setTextColor(high)
                                }
                                binding.txtFoodHighLevelNumber1.text = emissionArray[0].category
                                binding.percentFoodHighLevelNumber1.text =
                                    emissionArray[0].percentEmission
                            } else {
                                binding.rowFoodHighLevelNumber1.visibility = View.GONE
                            }

                            if (emissionArray.size > 1 && emissionArray[1].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[1].emission.toFloat(),
                                        emissionArray[1].category
                                    )
                                )
                                color.add(Color.parseColor("#2ca25f"))
                                binding.iconFoodHighLevelNumber2.setImageResource(emissionArray[1].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[1].category,
                                        emissionArray[1].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtFoodHighLevelNumber2.setTextColor(high)
                                    binding.percentFoodHighLevelNumber2.setTextColor(high)
                                }
                                binding.txtFoodHighLevelNumber2.text = emissionArray[1].category
                                binding.percentFoodHighLevelNumber2.text =
                                    emissionArray[1].percentEmission
                            } else {
                                binding.rowFoodHighLevelNumber2.visibility = View.GONE
                            }

                            if (emissionArray.size > 2 && emissionArray[2].emission > 0) {
                                binding.iconFoodHighLevelNumber3.setImageResource(emissionArray[2].resourceID)
                                values.add(
                                    PieEntry(
                                        emissionArray[2].emission.toFloat(),
                                        emissionArray[2].category
                                    )
                                )
                                color.add(Color.parseColor("#66c2a4"))
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[2].category,
                                        emissionArray[2].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtFoodHighLevelNumber3.setTextColor(high)
                                    binding.percentFoodHighLevelNumber3.setTextColor(high)
                                }
                                binding.txtFoodHighLevelNumber3.text = emissionArray[2].category
                                binding.percentFoodHighLevelNumber3.text =
                                    emissionArray[2].percentEmission
                            } else {
                                binding.rowFoodHighLevelNumber3.visibility = View.GONE
                            }

                            if (emissionArray.size > 3 && emissionArray[3].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[3].emission.toFloat(),
                                        emissionArray[3].category
                                    )
                                )
                                color.add(Color.parseColor("#b2e2e2"))
                                binding.iconFoodHighLevelNumber4.setImageResource(emissionArray[3].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[3].category,
                                        emissionArray[3].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtFoodHighLevelNumber4.setTextColor(high)
                                    binding.percentFoodHighLevelNumber4.setTextColor(high)
                                }
                                binding.txtFoodHighLevelNumber4.text = emissionArray[3].category
                                binding.percentFoodHighLevelNumber4.text =
                                    emissionArray[3].percentEmission
                            } else {
                                binding.rowFoodHighLevelNumber4.visibility = View.GONE
                            }

                            // Create a DataSet
                            val dataSet = PieDataSet(values, "")

                            // pie color
                            dataSet.colors = color

                            // Create a PieData object
                            val data = PieData(dataSet)
                            pieChart.holeRadius = 0f
                            pieChart.transparentCircleRadius = 0f
                            data.setValueTextSize(0f)
                            data.setValueTextColor(Color.WHITE)
                            pieChart.legend.textSize = 16f;
                            pieChart.setDrawEntryLabels(false)
                            pieChart.legend.horizontalAlignment =
                                Legend.LegendHorizontalAlignment.RIGHT
                            pieChart.legend.verticalAlignment =
                                Legend.LegendVerticalAlignment.CENTER
                            pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL

                            pieChart.data = data
                            pieChart.invalidate()
                        }
                    }
                }

                "Vehicle" -> {
                    user?.let {
                        FirebaseQuery().readUserDailyVehicleEmission(it, year, month, day) { emission ->
                            var vehicleEmission = emission.premiumEmission + emission.unleadedEmission + emission.dieselEmission
                            var premiumEmission = emission.premiumEmission
                            var unleadedEmission = emission.unleadedEmission
                            var dieselEmission = emission.dieselEmission

                            openView(
                                openView = binding.layoutResultVehicle,
                                closeView = binding.layoutResult
                            )

                            binding.lblTotalVehicleEmission.text =
                                "${"%.2f".format(vehicleEmission)} kg of CO2e"

                            // start of pie code
                            val pieChart = binding.pieChartVehicle

                            pieChart.description.isEnabled = false

                            val values = mutableListOf<PieEntry>()

                            val color = mutableListOf<Int>()

                            var emissionArray: Array<EmissionHighLevel> = arrayOf()

                            if (premiumEmission > 0) {
                                val percentageFormat = (premiumEmission / vehicleEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_fuel",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Premium",
                                    premiumEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (unleadedEmission > 0) {
                                val percentageFormat = (unleadedEmission / vehicleEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_fuel",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Unleaded",
                                    unleadedEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (dieselEmission > 0) {
                                val percentageFormat = (dieselEmission / vehicleEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_fuel",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry =
                                    EmissionHighLevel(
                                        "Diesel",
                                        dieselEmission,
                                        emissionPercent,
                                        resourceId
                                    )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            emissionArray.sortByDescending { it.emission }

                            val high =
                                ContextCompat.getColor(
                                    this,
                                    com.example.carbonfootprint.R.color.high
                                )

                            val context = applicationContext

                            if (emissionArray.isNotEmpty() && emissionArray[0].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[0].emission.toFloat(),
                                        emissionArray[0].category
                                    )
                                )
                                color.add(Color.parseColor("#238b45"))
                                binding.iconVehicleHighLevelNumber1.setImageResource(emissionArray[0].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[0].category,
                                        emissionArray[0].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtVehicleHighLevelNumber1.setTextColor(high)
                                    binding.percentVehicleHighLevelNumber1.setTextColor(high)
                                }
                                binding.txtVehicleHighLevelNumber1.text = emissionArray[0].category
                                binding.percentVehicleHighLevelNumber1.text =
                                    emissionArray[0].percentEmission
                            } else {
                                binding.rowVehicleHighLevelNumber1.visibility = View.GONE
                            }

                            if (emissionArray.size > 1 && emissionArray[1].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[1].emission.toFloat(),
                                        emissionArray[1].category
                                    )
                                )
                                color.add(Color.parseColor("#66c2a4"))
                                binding.iconVehicleHighLevelNumber2.setImageResource(emissionArray[1].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[1].category,
                                        emissionArray[1].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtVehicleHighLevelNumber2.setTextColor(high)
                                    binding.percentVehicleHighLevelNumber2.setTextColor(high)
                                }
                                binding.txtVehicleHighLevelNumber2.text = emissionArray[1].category
                                binding.percentVehicleHighLevelNumber2.text =
                                    emissionArray[1].percentEmission
                            } else {
                                binding.rowVehicleHighLevelNumber2.visibility = View.GONE
                            }

                            if (emissionArray.size > 2 && emissionArray[2].emission > 0) {
                                binding.iconVehicleHighLevelNumber3.setImageResource(emissionArray[2].resourceID)
                                values.add(
                                    PieEntry(
                                        emissionArray[2].emission.toFloat(),
                                        emissionArray[2].category
                                    )
                                )
                                color.add(Color.parseColor("#b2e2e2"))
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[2].category,
                                        emissionArray[2].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtVehicleHighLevelNumber3.setTextColor(high)
                                    binding.percentVehicleHighLevelNumber3.setTextColor(high)
                                }
                                binding.txtVehicleHighLevelNumber3.text = emissionArray[2].category
                                binding.percentVehicleHighLevelNumber3.text =
                                    emissionArray[2].percentEmission
                            } else {
                                binding.rowVehicleHighLevelNumber3.visibility = View.GONE
                            }

                            // Create a DataSet
                            val dataSet = PieDataSet(values, "")

                            // pie color
                            dataSet.colors = color

                            // Create a PieData object
                            val data = PieData(dataSet)
                            pieChart.holeRadius = 0f
                            pieChart.transparentCircleRadius = 0f
                            data.setValueTextSize(0f)
                            data.setValueTextColor(Color.WHITE)
                            pieChart.legend.textSize = 16f;
                            pieChart.setDrawEntryLabels(false)
                            pieChart.legend.horizontalAlignment =
                                Legend.LegendHorizontalAlignment.RIGHT
                            pieChart.legend.verticalAlignment =
                                Legend.LegendVerticalAlignment.CENTER
                            pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL

                            pieChart.data = data
                            pieChart.invalidate()
                        }
                    }
                }

                "Travel" -> {
                    user?.let {
                        FirebaseQuery().readUserDailyTravelEmission(it, year, month, day) { emission ->
                            var travelEmission = emission.tricycleEmission + emission.jeepEmission + emission.electricJeepEmission + emission.airconditionedElectricJeepEmission + emission.busEmission + emission.economyBusEmission + emission.airconditionedBusEmission + emission.uvVanEmission + emission.uvTaxiEmission
                            var tricycleEmission = emission.tricycleEmission
                            var pUJEmission = emission.jeepEmission
                            var ePUJEmission = emission.electricJeepEmission
                            var ePUJAEmission = emission.airconditionedElectricJeepEmission
                            var pUBEmission = emission.busEmission
                            var pUBAEmission = emission.airconditionedBusEmission
                            var pUBEEmission = emission.economyBusEmission
                            var taxiEmission = emission.uvTaxiEmission
                            var uVExpressEmission = emission.uvVanEmission

                            openView(
                                openView = binding.layoutResultTravel,
                                closeView = binding.layoutResult
                            )

                            binding.lblTotalTravelEmission.text =
                                "${"%.2f".format(travelEmission)} kg of CO2e"

                            // start of pie code
                            val pieChart = binding.pieChartTravel

                            pieChart.description.isEnabled = false

                            val values = mutableListOf<PieEntry>()

                            val color = mutableListOf<Int>()

                            var emissionArray: Array<EmissionHighLevel> = arrayOf()

                            if (tricycleEmission > 0) {
                                val percentageFormat = (tricycleEmission / travelEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_tricycle",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Tricycle",
                                    tricycleEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (pUJEmission > 0) {
                                val percentageFormat = (pUJEmission / travelEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_jeep",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry =
                                    EmissionHighLevel(
                                        "Jeep",
                                        pUJEmission,
                                        emissionPercent,
                                        resourceId
                                    )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (ePUJEmission > 0) {
                                val percentageFormat = (ePUJEmission / travelEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_jeep_electric",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry =
                                    EmissionHighLevel(
                                        "E-Jeep",
                                        ePUJEmission,
                                        emissionPercent,
                                        resourceId
                                    )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (ePUJAEmission > 0) {
                                val percentageFormat = (ePUJAEmission / travelEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_jeep_aircon",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "E-Jeep w/ Aircon",
                                    ePUJAEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (pUBEmission > 0) {
                                val percentageFormat = (pUBEmission / travelEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_bus",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry =
                                    EmissionHighLevel(
                                        "Bus",
                                        pUBEmission,
                                        emissionPercent,
                                        resourceId
                                    )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (pUBEEmission > 0) {
                                val percentageFormat = (pUBEEmission / travelEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_bus_economy",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Economy Bus",
                                    pUBEEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (pUBAEmission > 0) {
                                val percentageFormat = (pUBAEmission / travelEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_bus_aircon",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "Bus w/ Aircon",
                                    pUBAEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (taxiEmission > 0) {
                                val percentageFormat = (taxiEmission / travelEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_taxi",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry =
                                    EmissionHighLevel(
                                        "Taxi",
                                        taxiEmission,
                                        emissionPercent,
                                        resourceId
                                    )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            if (uVExpressEmission > 0) {
                                val percentageFormat = (uVExpressEmission / travelEmission) * 100

                                val emissionPercent = if(percentageFormat == 100.00){
                                    "${percentageFormat.toInt()}%"
                                }else{
                                    "${"%.2f".format(percentageFormat)}%"
                                }

                                val resourceId = getResources().getIdentifier(
                                    "new_icon_uvexpress",
                                    "mipmap",
                                    getPackageName()
                                )

                                val newEntry = EmissionHighLevel(
                                    "UV Express",
                                    uVExpressEmission,
                                    emissionPercent,
                                    resourceId
                                )

                                emissionArray = emissionArray.plus(newEntry)
                            }

                            emissionArray.sortByDescending { it.emission }

                            val high =
                                ContextCompat.getColor(
                                    this,
                                    com.example.carbonfootprint.R.color.high
                                )

                            val context = applicationContext

                            if (emissionArray.isNotEmpty() && emissionArray[0].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[0].emission.toFloat(),
                                        emissionArray[0].category
                                    )
                                )
                                color.add(Color.parseColor("#00441b"))
                                binding.iconTravelHighLevelNumber1.setImageResource(emissionArray[0].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[0].category,
                                        emissionArray[0].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtTravelHighLevelNumber1.setTextColor(high)
                                    binding.percentTravelHighLevelNumber1.setTextColor(high)
                                }
                                binding.txtTravelHighLevelNumber1.text = emissionArray[0].category
                                binding.percentTravelHighLevelNumber1.text =
                                    emissionArray[0].percentEmission
                            } else {
                                binding.rowTravelHighLevelNumber1.visibility = View.GONE
                            }

                            if (emissionArray.size > 1 && emissionArray[1].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[1].emission.toFloat(),
                                        emissionArray[1].category
                                    )
                                )
                                color.add(Color.parseColor("#006d2c"))
                                binding.iconTravelHighLevelNumber2.setImageResource(emissionArray[1].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[1].category,
                                        emissionArray[1].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtTravelHighLevelNumber2.setTextColor(high)
                                    binding.percentTravelHighLevelNumber2.setTextColor(high)
                                }
                                binding.txtTravelHighLevelNumber2.text = emissionArray[1].category
                                binding.percentTravelHighLevelNumber2.text =
                                    emissionArray[1].percentEmission
                            } else {
                                binding.rowTravelHighLevelNumber2.visibility = View.GONE
                            }

                            if (emissionArray.size > 2 && emissionArray[2].emission > 0) {
                                binding.iconTravelHighLevelNumber3.setImageResource(emissionArray[2].resourceID)
                                values.add(
                                    PieEntry(
                                        emissionArray[2].emission.toFloat(),
                                        emissionArray[2].category
                                    )
                                )
                                color.add(Color.parseColor("#238b45"))
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[2].category,
                                        emissionArray[2].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtTravelHighLevelNumber3.setTextColor(high)
                                    binding.percentTravelHighLevelNumber3.setTextColor(high)
                                }
                                binding.txtTravelHighLevelNumber3.text = emissionArray[2].category
                                binding.percentTravelHighLevelNumber3.text =
                                    emissionArray[2].percentEmission
                            } else {
                                binding.rowTravelHighLevelNumber3.visibility = View.GONE
                            }

                            if (emissionArray.size > 3 && emissionArray[3].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[3].emission.toFloat(),
                                        emissionArray[3].category
                                    )
                                )
                                color.add(Color.parseColor("#41ae76"))
                                binding.iconTravelHighLevelNumber4.setImageResource(emissionArray[3].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[3].category,
                                        emissionArray[3].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtTravelHighLevelNumber4.setTextColor(high)
                                    binding.percentTravelHighLevelNumber4.setTextColor(high)
                                }
                                binding.txtTravelHighLevelNumber4.text = emissionArray[3].category
                                binding.percentTravelHighLevelNumber4.text =
                                    emissionArray[3].percentEmission
                            } else {
                                binding.rowTravelHighLevelNumber4.visibility = View.GONE
                            }

                            if (emissionArray.size > 4 && emissionArray[4].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[4].emission.toFloat(),
                                        emissionArray[4].category
                                    )
                                )
                                color.add(Color.parseColor("#66c2a4"))
                                binding.iconTravelHighLevelNumber5.setImageResource(emissionArray[4].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[4].category,
                                        emissionArray[4].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtTravelHighLevelNumber5.setTextColor(high)
                                    binding.percentTravelHighLevelNumber5.setTextColor(high)
                                }
                                binding.txtTravelHighLevelNumber5.text = emissionArray[4].category
                                binding.percentTravelHighLevelNumber5.text =
                                    emissionArray[4].percentEmission
                            } else {
                                binding.rowTravelHighLevelNumber5.visibility = View.GONE
                            }

                            if (emissionArray.size > 5 && emissionArray[5].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[5].emission.toFloat(),
                                        emissionArray[5].category
                                    )
                                )
                                color.add(Color.parseColor("#99d8c9"))
                                binding.iconTravelHighLevelNumber6.setImageResource(emissionArray[5].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[5].category,
                                        emissionArray[5].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtTravelHighLevelNumber6.setTextColor(high)
                                    binding.percentTravelHighLevelNumber6.setTextColor(high)
                                }
                                binding.txtTravelHighLevelNumber6.text = emissionArray[5].category
                                binding.percentTravelHighLevelNumber6.text =
                                    emissionArray[5].percentEmission
                            } else {
                                binding.rowTravelHighLevelNumber6.visibility = View.GONE
                            }

                            if (emissionArray.size > 6 && emissionArray[6].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[6].emission.toFloat(),
                                        emissionArray[6].category
                                    )
                                )
                                color.add(Color.parseColor("#ccece6"))
                                binding.iconTravelHighLevelNumber7.setImageResource(emissionArray[6].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[6].category,
                                        emissionArray[6].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtTravelHighLevelNumber7.setTextColor(high)
                                    binding.percentTravelHighLevelNumber7.setTextColor(high)
                                }
                                binding.txtTravelHighLevelNumber7.text = emissionArray[6].category
                                binding.percentTravelHighLevelNumber7.text =
                                    emissionArray[6].percentEmission
                            } else {
                                binding.rowTravelHighLevelNumber7.visibility = View.GONE
                            }

                            if (emissionArray.size > 7 && emissionArray[7].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[7].emission.toFloat(),
                                        emissionArray[7].category
                                    )
                                )
                                color.add(Color.parseColor("#e5f5f9"))
                                binding.iconTravelHighLevelNumber8.setImageResource(emissionArray[7].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[7].category,
                                        emissionArray[7].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtTravelHighLevelNumber8.setTextColor(high)
                                    binding.percentTravelHighLevelNumber8.setTextColor(high)
                                }
                                binding.txtTravelHighLevelNumber8.text = emissionArray[7].category
                                binding.percentTravelHighLevelNumber8.text =
                                    emissionArray[7].percentEmission
                            } else {
                                binding.rowTravelHighLevelNumber8.visibility = View.GONE
                            }

                            if (emissionArray.size > 8 && emissionArray[8].emission > 0) {
                                values.add(
                                    PieEntry(
                                        emissionArray[8].emission.toFloat(),
                                        emissionArray[8].category
                                    )
                                )
                                color.add(Color.parseColor("#f7fcfd"))
                                binding.iconTravelHighLevelNumber9.setImageResource(emissionArray[8].resourceID)
                                if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                        category,
                                        emissionArray[8].category,
                                        emissionArray[8].emission.toFloat(),
                                        context
                                    )
                                ) {
                                    binding.txtTravelHighLevelNumber9.setTextColor(high)
                                    binding.percentTravelHighLevelNumber9.setTextColor(high)
                                }
                                binding.txtTravelHighLevelNumber9.text = emissionArray[8].category
                                binding.percentTravelHighLevelNumber9.text =
                                    emissionArray[8].percentEmission
                            } else {
                                binding.rowTravelHighLevelNumber9.visibility = View.GONE
                            }

                            // Create a DataSet
                            val dataSet = PieDataSet(values, "")

                            // pie color
                            dataSet.colors = color

                            // Create a PieData object
                            val data = PieData(dataSet)
                            pieChart.holeRadius = 0f
                            pieChart.transparentCircleRadius = 0f
                            data.setValueTextSize(0f)
                            data.setValueTextColor(Color.WHITE)
                            pieChart.legend.textSize = 16f;
                            pieChart.setDrawEntryLabels(false)
                            pieChart.legend.horizontalAlignment =
                                Legend.LegendHorizontalAlignment.RIGHT
                            pieChart.legend.verticalAlignment =
                                Legend.LegendVerticalAlignment.CENTER
                            pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL

                            pieChart.data = data
                            pieChart.invalidate()
                        }
                    }
                }
            }
        }

        binding.rowHighLevelNumber1.setOnClickListener {
            emissionLayoutIdentifier(binding.txtHighLevelNumber1.text.toString())
        }

        binding.btnHouseReturn.setOnClickListener {
            openView(openView = binding.layoutResult, closeView = binding.layoutResultHouse)
        }

        binding.rowHighLevelNumber2.setOnClickListener {
            emissionLayoutIdentifier(binding.txtHighLevelNumber2.text.toString())
        }

        binding.btnFoodReturn.setOnClickListener {
            openView(openView = binding.layoutResult, closeView = binding.layoutResultFood)
        }

        binding.rowHighLevelNumber3.setOnClickListener {
            emissionLayoutIdentifier(binding.txtHighLevelNumber3.text.toString())
        }

        binding.btnVehicleReturn.setOnClickListener {
            openView(openView = binding.layoutResult, closeView = binding.layoutResultVehicle)
        }

        binding.rowHighLevelNumber4.setOnClickListener {
            emissionLayoutIdentifier(binding.txtHighLevelNumber4.text.toString())
        }

        binding.btnTravelReturn.setOnClickListener {
            openView(openView = binding.layoutResult, closeView = binding.layoutResultTravel)
        }

        binding.btnShowRecommendation.setOnClickListener {
            if (day == 1) {
                val intent = Intent(this, MonthResult::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun fadeInAnimation(view: View) {
        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = 1000 // milliseconds
        view.startAnimation(fadeIn)

        // Set the visibility of the view to VISIBLE after the animation
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    private fun openView(openView: View, closeView: View) {
        closeView.visibility = View.GONE
        openView.visibility = View.VISIBLE
    }

    private fun fadeOutAnimation(view: View) {
        val fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.duration = 200 // milliseconds
        view.startAnimation(fadeOut)

        // Set the visibility of the view to GONE after the animation
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
    }
}




