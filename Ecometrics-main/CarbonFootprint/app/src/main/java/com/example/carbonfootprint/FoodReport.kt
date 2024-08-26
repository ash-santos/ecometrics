package com.example.carbonfootprint

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.carbonfootprint.CalculationFun.EmissionLevelIdentifier
import com.example.carbonfootprint.database.FirebaseQuery
import com.example.carbonfootprint.databinding.FoodReportLayoutBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import org.osmdroid.views.overlay.Marker
import java.text.DecimalFormat
import java.util.Calendar


class FoodReport : AppCompatActivity() {

    lateinit var binding: FoodReportLayoutBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val markerList = mutableListOf<Marker>()
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FoodReportLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val user = auth.currentUser

        val tonsConverter = 1000
        val tonsUnit = "mt CO2e"
        binding.spinnerDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                user?.let {
                    binding.layoutLoading.visibility = View.VISIBLE
                    binding.layoutReport.visibility = View.GONE
                    FirebaseQuery().readUserFoodEmission(it, year, month) { emission ->
                        FirebaseQuery().readDailyFoodEmissions(it, year, month) { dailyEmission ->
                            FirebaseQuery().readUserFoodYearlyEmissions(it, year) { monthly ->
                                FirebaseQuery().readMonthlyFoodEmissions(it, year) { monthlyEmission ->
                                    var emissionArray: Array<Array<String>>
                                    var emissionData: Array<String> = Array(4) { "" }
                                    if (position == 0) {

                                        //Monthly Emission data
                                        emissionArray =
                                            FirebaseQuery().populateDailyFoodEmissionArray(dailyEmission)
                                        emissionData[0] = emission.meatEmission.toString()
                                        emissionData[1] = emission.fishEmission.toString()
                                        emissionData[2] = emission.crustaceansEmission.toString()
                                        emissionData[3] = emission.mollusksEmission.toString()

                                    } else {

                                        //Yearly Emission Data
                                        emissionArray =
                                            FirebaseQuery().populateMonthlyFoodEmissionArray(monthlyEmission)
                                        emissionData[0] = monthly.meatEmission.toString()
                                        emissionData[1] = monthly.fishEmission.toString()
                                        emissionData[2] = monthly.crustaceansEmission.toString()
                                        emissionData[3] = monthly.mollusksEmission.toString()

                                    }
                                    val dayEmission = String.format(
                                        "%.2f",
                                        (emissionData[0].toDouble() + emissionData[1].toDouble() + emissionData[2].toDouble() + emissionData[3].toDouble())
                                    ).toDouble()
                                    val array = arrayOf(
                                        arrayOf(
                                            "Meat",
                                            emissionData[0],
                                            MainActivity().percentageFormat((emissionData[0].toDouble() / dayEmission) * 100),
                                            getResources().getIdentifier("new_icon_meat", "mipmap", getPackageName()).toString()
                                        ),
                                        arrayOf(
                                            "Fish",
                                            emissionData[1],
                                            MainActivity().percentageFormat((emissionData[1].toDouble() / dayEmission) * 100),
                                            getResources().getIdentifier("new_icon_fish", "mipmap", getPackageName()).toString()
                                        ),
                                        arrayOf(
                                            "Crustaceans",
                                            emissionData[2],
                                            MainActivity().percentageFormat((emissionData[2].toDouble() / dayEmission) * 100),
                                            getResources().getIdentifier("new_icon_crustacean", "mipmap", getPackageName()).toString()
                                        ),
                                        arrayOf(
                                            "Mollusks",
                                            emissionData[3],
                                            MainActivity().percentageFormat((emissionData[3].toDouble() / dayEmission) * 100),
                                            getResources().getIdentifier("new_icon_mollusk", "mipmap", getPackageName()).toString()
                                        )
                                    )


                                    array.sortByDescending { it[1].toDouble() }

                                    val high = ContextCompat.getColor(this@FoodReport, com.example.carbonfootprint.R.color.high)

                                    val context = applicationContext

                                    val category = "Food"

                                    val category1 = array[0]
                                    val category2 = array[1]
                                    val category3 = array[2]
                                    val category4 = array[3]
                                    val progress1 = category1[2].toDouble().toInt()
                                    val progress2 = category2[2].toDouble().toInt()
                                    val progress3 = category3[2].toDouble().toInt()
                                    val progress4 = category4[2].toDouble().toInt()

                                    val changeUnit = dayEmission > MainActivity().changeUnitNumber
                                    val decimalFormat = MainActivity().decimalFormat

                                    if (changeUnit) {
                                        binding.txtMonthlyCarbonFootprint.text =
                                            decimalFormat.format(dayEmission / tonsConverter)
                                        binding.txtMonthlyCarbonFootprintUnit.text = tonsUnit
                                    } else {
                                        binding.txtMonthlyCarbonFootprint.text = decimalFormat.format(dayEmission)
                                    }

                                    if (category1[1].toDouble() > 0.0) {
                                        binding.txtEmissionNameNumber1.text = category1[0]
                                        if (changeUnit) {
                                            binding.txtEmissionEmissionNumber1.text =
                                               decimalFormat.format(
                                                    category1[1].toDouble() / tonsConverter
                                                )
                                            binding.txtEmissionUnitNumber1.text = tonsUnit
                                        } else {
                                            binding.txtEmissionEmissionNumber1.text =
                                                decimalFormat.format(category1[1].toDouble())
                                        }
                                        binding.txtEmissionIconNumber1.setImageResource(category1[3].toInt())
                                        binding.progressBarNumber1.progress = progress1
                                        binding.txtEmissionPercentageNumber1.text =
                                            category1[2] + "%"

                                        if(EmissionLevelIdentifier().emissionLevelIdentifier(
                                                category,
                                                category1[0],
                                                category1[1].toFloat(),
                                                context
                                            )){
                                            binding.txtEmissionNameNumber1.setTextColor(high)
                                            binding.txtEmissionEmissionNumber1.setTextColor(high)
                                            binding.progressBarNumber1.progressTintList = ColorStateList.valueOf(high)
                                            binding.txtEmissionUnitNumber1.setTextColor(high)
                                            binding.txtEmissionPercentageNumber1.setTextColor(high)
                                        }
                                    } else {
                                        binding.layoutEmissionNumber1.visibility = View.GONE
                                    }
                                    if (category2[1].toDouble() > 0.0) {
                                        binding.txtEmissionNameNumber2.text = category2[0]
                                        if (changeUnit) {
                                            binding.txtEmissionEmissionNumber2.text =
                                               decimalFormat.format(
                                                    category2[1].toDouble() / tonsConverter
                                                )
                                            binding.txtEmissionUnitNumber2.text = tonsUnit
                                        } else {
                                            binding.txtEmissionEmissionNumber2.text =
                                                decimalFormat.format(category2[1].toDouble())
                                        }
                                        binding.txtEmissionIconNumber2.setImageResource(category2[3].toInt())
                                        binding.progressBarNumber2.progress = progress2
                                        binding.txtEmissionPercentageNumber2.text =
                                            category2[2] + "%"

                                        if(EmissionLevelIdentifier().emissionLevelIdentifier(
                                                category,
                                                category2[0],
                                                category2[1].toFloat(),
                                                context
                                            )){
                                            binding.txtEmissionNameNumber2.setTextColor(high)
                                            binding.txtEmissionEmissionNumber2.setTextColor(high)
                                            binding.progressBarNumber2.progressTintList = ColorStateList.valueOf(high)
                                            binding.txtEmissionUnitNumber2.setTextColor(high)
                                            binding.txtEmissionPercentageNumber2.setTextColor(high)
                                        }
                                    } else {
                                        binding.layoutEmissionNumber2.visibility = View.GONE
                                    }
                                    if (category3[1].toDouble() > 0.0) {
                                        binding.txtEmissionNameNumber3.text = category3[0]
                                        if (changeUnit) {
                                            binding.txtEmissionEmissionNumber3.text =
                                               decimalFormat.format(
                                                    category3[1].toDouble() / tonsConverter
                                                )
                                            binding.txtEmissionUnitNumber3.text = tonsUnit
                                        } else {
                                            binding.txtEmissionEmissionNumber3.text =
                                                decimalFormat.format(category3[1].toDouble())
                                        }
                                        binding.txtEmissionIconNumber3.setImageResource(category3[3].toInt())
                                        binding.progressBarNumber3.progress = progress3
                                        binding.txtEmissionPercentageNumber3.text =
                                            category3[2] + "%"

                                        if(EmissionLevelIdentifier().emissionLevelIdentifier(
                                                category,
                                                category3[0],
                                                category3[1].toFloat(),
                                                context
                                            )){
                                            binding.txtEmissionNameNumber3.setTextColor(high)
                                            binding.txtEmissionEmissionNumber3.setTextColor(high)
                                            binding.progressBarNumber3.progressTintList = ColorStateList.valueOf(high)
                                            binding.txtEmissionUnitNumber3.setTextColor(high)
                                            binding.txtEmissionPercentageNumber3.setTextColor(high)
                                        }
                                    } else {
                                        binding.layoutEmissionNumber3.visibility = View.GONE
                                    }
                                    if (category4[1].toDouble() > 0.0) {
                                        binding.txtEmissionNameNumber4.text = category4[0]
                                        if (changeUnit) {
                                            binding.txtEmissionEmissionNumber4.text =
                                               decimalFormat.format(
                                                    category4[1].toDouble() / tonsConverter
                                                )
                                            binding.txtEmissionUnitNumber4.text = tonsUnit
                                        } else {
                                            binding.txtEmissionEmissionNumber4.text =
                                                decimalFormat.format(category4[1].toDouble())
                                        }
                                        binding.txtEmissionIconNumber4.setImageResource(category4[3].toInt())
                                        binding.progressBarNumber4.progress = progress4
                                        binding.txtEmissionPercentageNumber4.text =
                                            category4[2] + "%"

                                        if(EmissionLevelIdentifier().emissionLevelIdentifier(
                                                category,
                                                category4[0],
                                                category4[1].toFloat(),
                                                context
                                            )){
                                            binding.txtEmissionNameNumber4.setTextColor(high)
                                            binding.txtEmissionEmissionNumber4.setTextColor(high)
                                            binding.progressBarNumber4.progressTintList = ColorStateList.valueOf(high)
                                            binding.txtEmissionUnitNumber4.setTextColor(high)
                                            binding.txtEmissionPercentageNumber4.setTextColor(high)
                                        }
                                    } else {
                                        binding.layoutEmissionNumber4.visibility = View.GONE
                                    }

                                    binding.progressBarNumber1.progress = progress1
                                    binding.progressBarNumber2.progress = progress2
                                    binding.progressBarNumber3.progress = progress3
                                    binding.progressBarNumber4.progress = progress4

                                    val barChart = binding.barChart
                                    barChart.animateY(1500)
                                    val entries = ArrayList<BarEntry>()
                                    barChart.getAxisLeft().setDrawGridLines(false)
                                    barChart.setScaleEnabled(false)
                                    barChart.setHighlightPerTapEnabled(false)
                                    barChart.setHighlightPerDragEnabled(false)
                                    barChart.getAxisRight().setDrawGridLines(false)
                                    barChart.getXAxis().setDrawGridLines(false)
                                    barChart.axisRight.isEnabled = false
                                    barChart.setFitBars(false)
                                    barChart.legend.isEnabled = false
                                    barChart.description.isEnabled = false
                                    barChart.getXAxis().setLabelCount(31)
                                    barChart.xAxis.textSize = 10f

                                    emissionArray.forEachIndexed { index, data ->
                                        val totalEmission =
                                            data[1].toFloat() + data[2].toFloat() + data[3].toFloat() + data[4].toFloat()

                                        if (changeUnit) {
                                            if (totalEmission > 0.0f) {
                                                entries.add(
                                                    BarEntry(
                                                        index.toFloat(),
                                                        totalEmission / tonsConverter
                                                    )
                                                )
                                            }
                                        } else {
                                            if (totalEmission > 0.0f) {
                                                entries.add(
                                                    BarEntry(
                                                        index.toFloat(),
                                                        totalEmission
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    val xAxisLabelDays = ArrayList<String>()
                                    var unit: String
                                    for (i in 1..31) {
                                        if(i%10 == 1){
                                            unit = "st day"
                                        }else if(i%10 == 2){
                                            unit = "nd day"
                                        }else if(i%10 == 3){
                                            unit = "rd day"
                                        }else{
                                            unit = "th day"
                                        }
                                        xAxisLabelDays.add("${i}${unit}")
                                    }

                                    //for the 12 months of the year
                                    val xAxisLabel = ArrayList<String>()
                                    xAxisLabel.add("Jan")
                                    xAxisLabel.add("Feb")
                                    xAxisLabel.add("Mar")
                                    xAxisLabel.add("Apr")
                                    xAxisLabel.add("May")
                                    xAxisLabel.add("Jun")
                                    xAxisLabel.add("Jul")
                                    xAxisLabel.add("Aug")
                                    xAxisLabel.add("Sep")
                                    xAxisLabel.add("Oct")
                                    xAxisLabel.add("Nov")
                                    xAxisLabel.add("Dec")

                                    val xAxis: XAxis = barChart.getXAxis()
                                    val yAxis: YAxis = barChart.axisLeft
                                    xAxis.position = XAxis.XAxisPosition.BOTTOM

                                    val xFormatter: ValueFormatter = object : ValueFormatter() {
                                        override fun getFormattedValue(value: Float): String {
                                            val index = value.toInt()
                                            val selectedLabelArray =
                                                if (binding.spinnerDate.selectedItem == "Month") xAxisLabelDays else xAxisLabel
                                            return if (index >= 0 && index < selectedLabelArray.size) {
                                                selectedLabelArray[index]
                                            } else {
                                                ""
                                            }

                                        }
                                    }

                                    xAxis.granularity = 1f
                                    xAxis.yOffset = -2f
                                    xAxis.valueFormatter = xFormatter

                                    val yFormatter = LargeValueFormatter()

                                    yAxis.valueFormatter = yFormatter


                                    val barDataSet = BarDataSet(entries, "Bar Data")
                                    val colors = arrayListOf(
                                        Color.parseColor("#66c2a4")
                                    )

                                    val dataFormatter: LargeValueFormatter = object : LargeValueFormatter() {
                                        override fun getFormattedValue(value: Float): String {
                                            return if (value < 1) {
                                                DecimalFormat("0.###").format(value)
                                            } else {
                                                super.getFormattedValue(value)
                                            }
                                        }
                                    }

                                    barDataSet.colors = colors

                                    barDataSet.colors = colors
                                    val data = BarData(barDataSet)
                                    data.setValueTextSize(10f)
                                    data.setBarWidth(0.5f)
                                    data.setValueFormatter(dataFormatter)
                                    barChart.data = data

                                    barChart.setFitBars(false)
                                    barChart.invalidate()

                                    binding.layoutLoading.visibility = View.GONE
                                    binding.layoutReport.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.btnReturn.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
}
