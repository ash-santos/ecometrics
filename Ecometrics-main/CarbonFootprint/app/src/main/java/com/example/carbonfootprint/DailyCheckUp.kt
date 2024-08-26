package com.example.carbonfootprint

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carbonfootprint.CalculationFun.ButtonChangingColor
import com.example.carbonfootprint.CalculationFun.EmissionCalculation
import com.example.carbonfootprint.CalculationFun.EmissionLevelIdentifier
import com.example.carbonfootprint.adapter.DataModel
import com.example.carbonfootprint.adapter.TableAdapter
import com.example.carbonfootprint.database.FirebaseQuery
import com.example.carbonfootprint.database.FoodData
import com.example.carbonfootprint.databinding.DailyCheckupBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.Calendar
import kotlin.random.Random

class DailyCheckUp : AppCompatActivity(), TableAdapter.OnItemClickListener {
    lateinit var binding: DailyCheckupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DailyCheckupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val user = auth.currentUser
        val userId = user?.uid.toString()
        var electricBill = false
        var vehicleUsage = false
        var foodPrice = false

        var houseEmission = 0.0
        var electricityEmission = 0.0
        var charcoalEmission = 0.0
        var lpgEmission = 0.0
        var firewoodEmission = 0.0

        var foodEmission = 0.0
        val foodMeatEmissionList = ArrayList<Double>()
        var meatEmission = 0.0
        val foodCrustaceansEmissionList = ArrayList<Double>()
        var crustaceansEmission = 0.0
        val foodFishEmissionList = ArrayList<Double>()
        var fishEmission = 0.0
        val foodMollusksEmissionList = ArrayList<Double>()
        var mollusksEmission = 0.0


        var vehicleEmission = 0.0
        var premiumEmission = 0.0
        var unleadedEmission = 0.0
        var dieselEmission = 0.0

        var travelEmission = 0.0
        val travelTricycleEmissionList = ArrayList<Double>()
        var tricycleEmission = 0.0
        val travelPUJEmissionList = ArrayList<Double>()
        var pUJEmission = 0.0
        val travelEPUJEmissionList = ArrayList<Double>()
        var ePUJEmission = 0.0
        val travelEPUJAEmissionList = ArrayList<Double>()
        var ePUJAEmission = 0.0
        val travelPUBEmissionList = ArrayList<Double>()
        var pUBEmission = 0.0
        val travelPUBAEmissionList = ArrayList<Double>()
        var pUBAEmission = 0.0
        val travelPUBEEmissionList = ArrayList<Double>()
        var pUBEEmission = 0.0
        val travelTaxiEmissionList = ArrayList<Double>()
        var taxiEmission = 0.0
        val travelUVExpressEmissionList = ArrayList<Double>()
        var uVExpressEmission = 0.0

        val recyclerViewFood = binding.recyclerViewFood
        val layoutManagerFood = LinearLayoutManager(this)
        recyclerViewFood.layoutManager = layoutManagerFood

        val recyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val foodList = ArrayList<DataModel>()
        val foodEmissionList = ArrayList<Double>()
        val foodCategoryList = ArrayList<String>()

        val transportationList = ArrayList<DataModel>()
        val transportationEmissionList = ArrayList<Double>()
        val transportationCategoryList = ArrayList<String>()



        binding.btnElectricityBillYes.setOnClickListener {
            fadeInAnimation(binding.tblHouseQuestion2);
            fadeInAnimation(binding.tblHouseQuestionYesNo);
            electricBill = true
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnElectricityBillNo,
                buttonYes = binding.btnElectricityBillYes,
                this
            )
        }

        binding.btnElectricityBillNo.setOnClickListener {
            fadeOutAnimation(binding.tblHouseQuestion2);
            fadeOutAnimation(binding.tblHouseQuestionYesNo);
            fadeOutAnimation(binding.tblRowElectricity1);
            fadeOutAnimation(binding.tblRowElectricity2);
            fadeOutAnimation(binding.tblRowElectricity3);
            fadeOutAnimation(binding.tblRowElectricity4);
            electricBill = false
            binding.lblElectricityEmission.text = ""
            binding.txtElectricityBill.text.clear()
            binding.lblElectricityEmission.text = ""
            binding.txtElectricityAmount.text.clear()
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnElectricityBillNo,
                buttonYes = binding.btnElectricityBillYes,
                this
            )
            ButtonChangingColor().setButtonBackgroundColorSelectDefault(
                buttonNo = binding.btnElectricityNo,
                buttonYes = binding.btnElectricityYes,
                this
            )
        }

        binding.btnElectricityYes.setOnClickListener {
            fadeInAnimation(binding.tblRowElectricity1);
            fadeInAnimation(binding.tblRowElectricity2);
            fadeOutAnimation(binding.tblRowElectricity3);
            fadeOutAnimation(binding.tblRowElectricity4);
            binding.lblElectricityEmission.text = ""
            binding.txtElectricityBill.text.clear()
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnElectricityNo,
                buttonYes = binding.btnElectricityYes,
                this
            )
        }

        binding.btnElectricityNo.setOnClickListener {
            fadeOutAnimation(binding.tblRowElectricity1);
            fadeOutAnimation(binding.tblRowElectricity2);
            fadeInAnimation(binding.tblRowElectricity3);
            fadeInAnimation(binding.tblRowElectricity4);
            binding.lblElectricityEmission.text = ""
            binding.txtElectricityAmount.text.clear()
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnElectricityNo,
                buttonYes = binding.btnElectricityYes,
                this
            )
        }

        binding.btnCoalYes.setOnClickListener {
            fadeInAnimation(binding.tblRowCoal1);
            fadeInAnimation(binding.tblRowCoal2);
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnCoalNo,
                buttonYes = binding.btnCoalYes,
                this
            )
        }

        binding.btnCoalNo.setOnClickListener {
            fadeOutAnimation(binding.tblRowCoal1);
            fadeOutAnimation(binding.tblRowCoal2);
            binding.lblCharcoalEmission.text = ""
            binding.txtCharcoalBill.text.clear()
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnCoalNo,
                buttonYes = binding.btnCoalYes,
                this
            )
        }

        binding.btnFirewoodYes.setOnClickListener {
            fadeInAnimation(binding.tblRowFirewood1);
            fadeInAnimation(binding.tblRowFirewood2);
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnFirewoodNo,
                buttonYes = binding.btnFirewoodYes,
                this
            )
        }

        binding.btnFirewoodNo.setOnClickListener {
            fadeOutAnimation(binding.tblRowFirewood1);
            fadeOutAnimation(binding.tblRowFirewood2);
            binding.lblFirewoodEmission.text = ""
            binding.txtFirewoodBill.text.clear()
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnFirewoodNo,
                buttonYes = binding.btnFirewoodYes,
                this
            )
        }

        binding.btnLpgYes.setOnClickListener {
            fadeInAnimation(binding.tblRowLpg1);
            fadeInAnimation(binding.tblRowLpg2);
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnLpgNo,
                buttonYes = binding.btnLpgYes,
                this
            )
        }

        binding.btnLpgNo.setOnClickListener {
            fadeOutAnimation(binding.tblRowLpg1);
            fadeOutAnimation(binding.tblRowLpg2);
            binding.lblLPGEmission.text = ""
            binding.txtLpgBill.text.clear()
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnLpgNo,
                buttonYes = binding.btnLpgYes,
                this
            )
        }

        binding.txtElectricityBill.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                FirebaseQuery().readUserProfile(userId) {
                    val enteredText = editable.toString()
                    if (enteredText.isEmpty()) {
                        binding.txtElectricityBill.error = "Complete this field first"
                        binding.lblElectricityEmission.text = ""
                    }
                    binding.txtElectricityBill.error = null
                    EmissionCalculation().calculateElectricityBillEmissions(
                        province = it.province,
                        amount = enteredText.toDouble()
                    ) { emission ->
                        var result = emission / it.houseMember
                        binding.lblElectricityEmission.text = "${result}"
                    }
                }
            }
        })

        binding.txtElectricityAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                FirebaseQuery().readUserProfile(userId) {
                    val enteredText = editable.toString()
                    if (enteredText.isEmpty()) {
                        binding.txtElectricityAmount.error = "Complete this field first"
                        binding.lblElectricityEmission.text = ""
                    } else {
                        binding.txtElectricityAmount.error = null
                        EmissionCalculation().calculateElectricityAmountEmissions(
                            province = it.province,
                            amount = enteredText.toDouble(),
                        ) { emission ->
                            var result = emission / it.houseMember
                            binding.lblElectricityEmission.text = "${result}"
                        }
                    }
                }
            }
        })

        binding.txtLpgBill.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                FirebaseQuery().readUserProfile(userId) {
                    val enteredText = editable.toString()
                    if (enteredText.isEmpty()) {
                        binding.txtLpgBill.error = "Complete this field first"
                        binding.lblLPGEmission.text = ""
                    } else {
                        binding.lblLPGEmission.error = null
                        EmissionCalculation().calculateLPGEmissions(
                            province = it.province,
                            amount = enteredText.toDouble()
                        ) { emission ->
                            var result = emission / it.houseMember
                            binding.lblLPGEmission.text = "${result}"
                        }
                    }
                }
            }
        })

        binding.txtCharcoalBill.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                FirebaseQuery().readUserProfile(userId) {
                    val enteredText = editable.toString()
                    if (enteredText.isEmpty()) {
                        binding.txtCharcoalBill.error = "Complete this field first"
                        binding.lblCharcoalEmission.text = ""
                    } else {
                        binding.txtCharcoalBill.error = null
                        EmissionCalculation().calculateCharcoalEmissions(
                            province = it.province,
                            amount = enteredText.toDouble()
                        ) { emission ->
                            var result = emission / it.houseMember
                            binding.lblCharcoalEmission.text = "${result}"
                        }
                    }
                }
            }
        })

        binding.txtFirewoodBill.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                FirebaseQuery().readUserProfile(userId) {
                    val enteredText = editable.toString()
                    if (enteredText.isEmpty()) {
                        binding.txtFirewoodBill.error = "Complete this field first"
                        binding.lblFirewoodEmission.text = ""
                    } else {
                        binding.txtFirewoodBill.error = null
                        EmissionCalculation().calculateFirewoodEmissions(
                            province = it.province,
                            kg = enteredText.toDouble()
                        ) { emission ->
                            var result = emission / it.houseMember
                            binding.lblFirewoodEmission.text = "${result}"
                        }
                    }
                }
            }
        })

        binding.btnSubmitHouse.setOnClickListener {
            if (electricBill) {
                if (binding.lblElectricityEmission.text.isNotEmpty()) {
                    electricityEmission = binding.lblElectricityEmission.text.toString().toDouble()
                    if (binding.lblHouseTotalEmission.text.isNotEmpty()) {
                        binding.lblHouseTotalEmission.text = "${
                            binding.lblHouseTotalEmission.text.toString()
                                .toDouble() + binding.lblElectricityEmission.text.toString()
                                .toDouble()
                        }"
                    } else {
                        binding.lblHouseTotalEmission.text =
                            binding.lblElectricityEmission.text.toString()
                    }
                }
            }

            if (binding.lblLPGEmission.text.isNotEmpty()) {
                lpgEmission = binding.lblLPGEmission.text.toString().toDouble()
                if (binding.lblHouseTotalEmission.text.isNotEmpty()) {
                    binding.lblHouseTotalEmission.text = "${
                        binding.lblHouseTotalEmission.text.toString()
                            .toDouble() + binding.lblLPGEmission.text.toString().toDouble()
                    }"
                } else {
                    binding.lblHouseTotalEmission.text = binding.lblLPGEmission.text.toString()
                }
            }

            if (binding.lblCharcoalEmission.text.isNotEmpty()) {
                charcoalEmission = binding.lblCharcoalEmission.text.toString().toDouble()
                if (binding.lblHouseTotalEmission.text.isNotEmpty()) {
                    binding.lblHouseTotalEmission.text = "${
                        binding.lblHouseTotalEmission.text.toString()
                            .toDouble() + binding.lblCharcoalEmission.text.toString().toDouble()
                    }"
                } else {
                    binding.lblHouseTotalEmission.text = binding.lblCharcoalEmission.text.toString()
                }
            }

            if (binding.lblFirewoodEmission.text.isNotEmpty()) {
                firewoodEmission = binding.lblFirewoodEmission.text.toString().toDouble()
                if (binding.lblHouseTotalEmission.text.isNotEmpty()) {
                    binding.lblHouseTotalEmission.text = "${
                        binding.lblHouseTotalEmission.text.toString()
                            .toDouble() + binding.lblFirewoodEmission.text.toString().toDouble()
                    }"
                } else {
                    binding.lblHouseTotalEmission.text = binding.lblFirewoodEmission.text.toString()
                }
            }
            binding.layoutHouse.visibility = View.GONE
            binding.layoutFood.visibility = View.VISIBLE
            binding.appIcon.setImageResource(
                getResources().getIdentifier(
                    "ecometrics_first_time_food",
                    "drawable",
                    getPackageName()
                )
            )
        }

        binding.spinnerFoodCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Handle the selected item change here
                    val food = parent?.getItemAtPosition(position).toString()

                    if (food == "Choose Category") {
                        binding.tblFoodQuestions.visibility = View.GONE
                        binding.tblFoodList.visibility = View.VISIBLE
                    } else {
                        var icon = 0

                        when (food) {
                            "Meat" -> icon = getResources().getIdentifier(
                                "new_icon_meat",
                                "mipmap",
                                getPackageName()
                            )

                            "Fish" -> icon = getResources().getIdentifier(
                                "new_icon_fish",
                                "mipmap",
                                getPackageName()
                            )

                            "Crustaceans" -> icon = getResources().getIdentifier(
                                "new_icon_crustacean",
                                "mipmap",
                                getPackageName()
                            )

                            "Mollusks" -> icon = getResources().getIdentifier(
                                "new_icon_mollusk",
                                "mipmap",
                                getPackageName()
                            )
                        }

                        binding.tblFoodQuestions.visibility = View.VISIBLE
                        binding.tblFoodList.visibility = View.GONE
                        binding.iconFoodCategory.setImageResource(icon)
                        binding.txtFoodBill.text.clear()
                        binding.lblFoodEmission.text = ""

                        // Assuming getProvinceData returns a list of province names based on the selected model
                        val foods = mutableListOf("Choose Food")
                        foods.addAll(FoodData().getFoodData(food))

                        val adapterFood = ArrayAdapter(
                            this@DailyCheckUp,
                            R.layout.simple_spinner_item,
                            foods
                        )
                        adapterFood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerFoodName.adapter = adapterFood

                        // Remove the default item after it's selected
                        // Remove the "Choose Province" item from the dropdown list after it's selected
                        if (position == 0) {
                            foods.removeAt(0)
                            adapterFood.notifyDataSetChanged()
                        }

                        val adapter = TableAdapter(foodList, this@DailyCheckUp)
                        recyclerViewFood.adapter = adapter
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case where nothing is selected
                }
            }

        binding.spinnerFoodName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val food = parent?.getItemAtPosition(position).toString()
                    binding.txtFoodBill.text.clear()
                    if (food == "Choose Food") {
                        binding.rowPriceQuestion.visibility = View.GONE
                        binding.rowPriceAnswer.visibility = View.GONE
                        binding.rowFoodPrice.visibility = View.GONE
                        binding.tblRowQuestionAmountFood.visibility = View.GONE
                        binding.tblRowFoodAmount.visibility = View.GONE
                        binding.btnInsertFood.visibility = View.GONE
                        return
                    }
                    binding.rowPriceQuestion.visibility = View.VISIBLE
                    binding.rowPriceAnswer.visibility = View.VISIBLE
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case where nothing is selected
                }
            }

        binding.btnFoodPriceNo.setOnClickListener {
            binding.rowFoodPrice.visibility = View.GONE
            binding.tblRowQuestionAmountFood.visibility = View.GONE
            foodPrice = false
            binding.txtFoodBill.text.clear()
            binding.txtFoodPrice.text.clear()
            binding.tblRowQuestionAmountFood.visibility = View.VISIBLE
            binding.tblRowFoodAmount.visibility = View.VISIBLE
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnFoodPriceNo,
                buttonYes = binding.btnFoodPriceYes,
                this
            )
        }

        binding.btnFoodPriceYes.setOnClickListener {
            binding.rowFoodPrice.visibility = View.VISIBLE
            binding.tblRowQuestionAmountFood.visibility = View.VISIBLE
            foodPrice = true
            binding.txtFoodBill.text.clear()
            binding.txtFoodPrice.text.clear()
            binding.tblRowQuestionAmountFood.visibility = View.GONE
            binding.tblRowFoodAmount.visibility = View.GONE
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnFoodPriceNo,
                buttonYes = binding.btnFoodPriceYes,
                this
            )
        }

        binding.txtFoodPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                val input = editable.toString()

                if (input == "") {
                    binding.txtFoodPrice.error = "Complete this field first"
                    binding.lblFoodEmission.text = ""
                    binding.txtFoodBill.text.clear()
                    binding.tblRowQuestionAmountFood.visibility = View.GONE
                    binding.tblRowFoodAmount.visibility = View.GONE
                    return
                }

                binding.txtFoodPrice.error = null

                if (input.toInt() < 0) {
                    binding.txtFoodPrice.error = "Food Price must higher than zero"
                    binding.lblFoodEmission.text = ""
                    return
                }

                binding.txtFoodPrice.error = null

                binding.tblRowQuestionAmountFood.visibility = View.VISIBLE
                binding.tblRowFoodAmount.visibility = View.VISIBLE
            }
        })



        binding.txtFoodBill.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                var category = binding.spinnerFoodCategory.selectedItem.toString()
                var food = binding.spinnerFoodName.selectedItem.toString()

                if (food == "Choose Food") {
                    Toast.makeText(this@DailyCheckUp, "Please Choose a Food.", Toast.LENGTH_SHORT)
                        .show()
                    binding.lblFoodEmission.text = ""
                    return
                }

                food = FoodData().getIDData(categoryName = category, foodName = food)!!

                if (category == "Meat") {
                    category = food
                }

                val enteredText = editable.toString()
                if (enteredText.isEmpty()) {
                    binding.txtFoodBill.error = "Complete this field first"
                    binding.lblFoodEmission.text = ""
                    binding.btnInsertFood.visibility = View.GONE
                } else {
                    FirebaseQuery().readUserProfile(userId) {
                        val amount = enteredText.toDouble()
                        binding.txtFoodBill.error = null

                        val price = if (binding.txtFoodPrice.text.isEmpty()) {
                            0
                        } else {
                            binding.txtFoodPrice.text.toString().toInt()
                        }

                        EmissionCalculation().calculateFoodEmissions(
                            province = it.province,
                            food = food,
                            category = category,
                            amount = amount,
                            customFoodPrice = foodPrice,
                            foodPrice = price
                        ) { result ->
                            binding.lblFoodEmission.text = "${result}"
                            binding.btnInsertFood.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })

        binding.btnInsertFood.setOnClickListener {
            if (binding.txtFoodBill.text.isEmpty()) {
                binding.txtFoodBill.error = "Complete this Field"
                return@setOnClickListener
            }
            if (binding.spinnerFoodName.selectedItem.toString() == "Choose Food") {
                Toast.makeText(this, "Please select a food category.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.txtFoodBill.error = null
            val category = binding.spinnerFoodCategory.selectedItem.toString()
            val food = binding.spinnerFoodName.selectedItem.toString()
            val bill = "₱${binding.txtFoodBill.text}"
            val emission = binding.lblFoodEmission.text.toString().toDoubleOrNull() ?: 0.0

            foodList.add(DataModel(food, bill))
            foodEmissionList.add(emission)
            foodCategoryList.add(category)

            when (category) {
                "Meat" -> foodMeatEmissionList.add(emission)
                "Fish" -> foodFishEmissionList.add(emission)
                "Crustaceans" -> foodCrustaceansEmissionList.add(emission)
                "Mollusks" -> foodMollusksEmissionList.add(emission)
            }

            binding.lblFoodTotalEmission.text = "${
                computeTotalFoodEmission(
                    foodEmissionList
                )
            }"
            foodPrice = false
            ButtonChangingColor().setButtonBackgroundColorSelectDefault(
                buttonNo = binding.btnFoodPriceNo,
                buttonYes = binding.btnFoodPriceYes,
                this
            )

            binding.txtFoodBill.text.clear()
            binding.lblFoodEmission.text = ""

            binding.spinnerFoodCategory.setSelection(0)
        }

        binding.txtFoodNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                val int = editable.toString()

                if (foodList.isEmpty()) {
                    binding.txtFoodNumber.text.clear()
                    Toast.makeText(this@DailyCheckUp, "The List is Empty.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                if (binding.txtFoodNumber.text.toString() != "" && int.toInt() > foodList.size) {
                    Toast.makeText(
                        this@DailyCheckUp,
                        "You can only input from number 1 - ${foodList.size}.",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.txtFoodNumber.text.clear()
                    return
                }
            }
        })

        binding.btnDeleteFood.setOnClickListener {
            if (foodList.isEmpty()) {
                Toast.makeText(this@DailyCheckUp, "The List is Empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.txtFoodNumber.text.toString() == "") {
                Toast.makeText(this@DailyCheckUp, "Select an Item in the List.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val index = binding.txtFoodNumber.text.toString().toInt() - 1

            foodList.removeAt(index)
            val category = foodCategoryList.elementAt(index)
            foodCategoryList.removeAt(index)
            val emission = foodEmissionList.elementAt(index)
            foodEmissionList.removeAt(index)

            when (category) {
                "Meat" -> foodMeatEmissionList.remove(emission)
                "Fish" -> foodFishEmissionList.remove(emission)
                "Crustaceans" -> foodCrustaceansEmissionList.remove(emission)
                "Mollusks" -> foodMollusksEmissionList.remove(emission)
            }

            val adapter = TableAdapter(foodList, this)
            recyclerViewFood.adapter = adapter

            binding.lblFoodTotalEmission.text = "${
                computeTotalFoodEmission(
                    foodEmissionList
                )
            }"

            binding.txtFoodNumber.text.clear()
        }

        binding.btnSubmitFood.setOnClickListener {
            meatEmission = computeTotalFoodEmission(
                foodMeatEmissionList
            )

            fishEmission = computeTotalFoodEmission(
                foodFishEmissionList
            )

            crustaceansEmission = computeTotalFoodEmission(
                foodCrustaceansEmissionList
            )

            mollusksEmission = computeTotalFoodEmission(
                foodMollusksEmissionList
            )

            FirebaseQuery().readUserProfile(userId) {
                if (it.carNumber != 0) {
                    FirebaseQuery().readUserVehicles(userId) { userVehicles ->
                        binding.layoutFood.visibility = View.GONE
                        binding.layoutVehicle.visibility = View.VISIBLE
                        binding.appIcon.setImageResource(
                            getResources().getIdentifier(
                                "ecometrics_first_time_vehicle",
                                "drawable",
                                getPackageName()
                            )
                        )
                        var carArray = FirebaseQuery().populateCarArray(userVehicles)
                        binding.vehicleNumber.text = "${it.carNumber}"
                        binding.vehicleIndex.text = "0"
                        var carIndex = binding.vehicleIndex.text.toString().toInt()
                        binding.txtVehicleName.text = carArray[carIndex][1]
                        binding.txtVehicleModel.text = carArray[carIndex][2]
                        binding.txtVehicleTransmisison.text = carArray[carIndex][3]
                        binding.txtVehicleSize.text = carArray[carIndex][4]
                        binding.txtFuelType.text = carArray[carIndex][5]
                        binding.txtEfficiency.text = carArray[carIndex][6]
                        binding.txtOldOdometer.text = carArray[carIndex][7]
                    }
                } else {
                    binding.appIcon.setImageResource(
                        getResources().getIdentifier(
                            "ecometrics_first_time_transpo",
                            "drawable",
                            getPackageName()
                        )
                    )
                }
            }
        }

        binding.btnVehicleYes.setOnClickListener {
            binding.lblVehicleUsage.text = "true"
            fadeInAnimation(binding.rowVehicle1);
            fadeInAnimation(binding.rowVehicle2);
            fadeInAnimation(binding.rowVehicle3);
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnVehicleNo,
                buttonYes = binding.btnVehicleYes,
                this
            )
        }

        binding.btnVehicleNo.setOnClickListener {
            binding.lblVehicleUsage.text = "false"
            binding.txtNewOdometer.text.clear()
            fadeOutAnimation(binding.rowVehicle1);
            fadeOutAnimation(binding.rowVehicle2);
            fadeOutAnimation(binding.rowVehicle3);
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnVehicleNo,
                buttonYes = binding.btnVehicleYes,
                this
            )
        }

        binding.txtNewOdometer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                var vehicleGasType = binding.txtFuelType.text.toString()
                var vehicleEfficiency = binding.txtEfficiency.text.toString().toDouble()
                var stringNewOdometer = editable.toString()

                if (validateCarFields(
                        newOdometer = stringNewOdometer,
                        oldOdometer = binding.txtOldOdometer.text.toString()
                    )
                ) {
                    var oldOdometer = binding.txtOldOdometer.text.toString().toInt()
                    var newOdometer = stringNewOdometer.toInt() - oldOdometer
                    FirebaseQuery().readUserProfile(userId) {
                        when {
                            vehicleGasType == "Diesel" -> {
                                EmissionCalculation().calculateDieselGasEmissions(
                                    province = it.province,
                                    km = newOdometer.toDouble(),
                                    vehicleEfficiency
                                ) { result ->
                                    binding.lblVehicleEmission.text = "${result}"
                                }
                            }

                            vehicleGasType == "Unleaded" -> {
                                EmissionCalculation().calculateUnleadedGasEmissions(
                                    province = it.province,
                                    km = newOdometer.toDouble(),
                                    vehicleEfficiency
                                ) { result ->
                                    binding.lblVehicleEmission.text = "${result}"
                                }
                            }

                            vehicleGasType == "Premium" -> {
                                EmissionCalculation().calculatePremiumGasEmissions(
                                    province = it.province,
                                    km = newOdometer.toDouble(),
                                    vehicleEfficiency
                                ) { result ->
                                    binding.lblVehicleEmission.text = "${result}"
                                }
                            }
                        }
                    }
                }
            }
        })


        binding.btnSubmitVehicle.setOnClickListener {
            FirebaseQuery().readUserVehicles(userId) { userVehicles ->
                var carArray = FirebaseQuery().populateCarArray(userVehicles)
                // Ensure that carIndex is incremented only once per click
                if (binding.vehicleNumber.text.toString()
                        .toInt() > binding.vehicleIndex.text.toString().toInt()
                ) {
                    if (binding.lblVehicleUsage.text.toString().toBoolean()) {
                        if (validateCarFields(
                                oldOdometer = binding.txtOldOdometer.text.toString(),
                                newOdometer = binding.txtNewOdometer.text.toString()
                            )
                        ) {
                            val emission = binding.lblVehicleEmission.text.toString()

                            if (binding.lblVehicleTotalEmission.text.isEmpty()) {
                                binding.lblVehicleTotalEmission.text = emission
                            } else {
                                binding.lblVehicleTotalEmission.text = "${
                                    binding.lblVehicleTotalEmission.text.toString()
                                        .toDouble() + emission.toDouble()
                                }"
                            }
                            val fuel = binding.txtFuelType.text.toString()
                            when (fuel) {
                                "Diesel" -> {
                                    if (binding.lblDieselEmission.text.isEmpty()) {
                                        binding.lblDieselEmission.text = emission
                                    } else {
                                        binding.lblDieselEmission.text = "${
                                            binding.lblDieselEmission.text.toString()
                                                .toDouble() + emission.toDouble()
                                        }"
                                    }
                                }

                                "Unleaded" -> {
                                    if (binding.lblUnleadedEmission.text.isEmpty()) {
                                        binding.lblUnleadedEmission.text = emission
                                    } else {
                                        binding.lblUnleadedEmission.text = "${
                                            binding.lblUnleadedEmission.text.toString()
                                                .toDouble() + emission.toDouble()
                                        }"
                                    }
                                    println(binding.lblUnleadedEmission.text.toString())
                                }

                                "Premium" -> {
                                    if (binding.lblPremiumEmission.text.isEmpty()) {
                                        binding.lblPremiumEmission.text = emission
                                    } else {
                                        binding.lblPremiumEmission.text = "${
                                            binding.lblPremiumEmission.text.toString()
                                                .toDouble() + emission.toDouble()
                                        }"
                                    }
                                }
                            }

                            val newOdometer = binding.txtNewOdometer.text.toString()

                            val carIndex = binding.vehicleIndex.text.toString().toInt()

                            user?.let {
                                updateUserVehicleOdometer(it, newOdometer, carArray[carIndex][0])
                            }
                        } else {
                            return@readUserVehicles
                        }
                    }
                    binding.lblVehicleUsage.text = "false"
                    binding.vehicleIndex.text =
                        "${binding.vehicleIndex.text.toString().toInt() + 1}"
                    if (binding.vehicleNumber.text.toString()
                            .toInt() != binding.vehicleIndex.text.toString().toInt()
                    ) {
                        val carIndex = binding.vehicleIndex.text.toString().toInt()
                        binding.txtVehicleName.text = carArray[carIndex][1]
                        binding.txtVehicleModel.text = carArray[carIndex][2]
                        binding.txtVehicleTransmisison.text = carArray[carIndex][3]
                        binding.txtVehicleSize.text = carArray[carIndex][4]
                        binding.txtFuelType.text = carArray[carIndex][5]
                        binding.txtEfficiency.text = carArray[carIndex][6]
                        binding.txtOldOdometer.text = carArray[carIndex][7]
                        binding.txtVehiclePassengerCount.text.clear()
                        binding.txtNewOdometer.text.clear()
                        fadeOutAnimation(binding.rowVehicle1);
                        fadeOutAnimation(binding.rowVehicle2);
                        fadeOutAnimation(binding.rowVehicle3);
                        ButtonChangingColor().setButtonBackgroundColorSelectDefault(
                            binding.btnVehicleNo,
                            binding.btnVehicleYes,
                            this
                        )
                    }
                }
            }
            if (binding.vehicleNumber.text.toString()
                    .toInt() == binding.vehicleIndex.text.toString().toInt() + 1
            ) {
                binding.layoutVehicle.visibility = View.GONE
                binding.layoutTransportation.visibility = View.VISIBLE
                binding.appIcon.setImageResource(
                    getResources().getIdentifier(
                        "ecometrics_first_time_transpo",
                        "drawable",
                        getPackageName()
                    )
                )
            }
        }

        binding.spinnerTransportation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Handle the selected item change here
                    val transportation = parent?.getItemAtPosition(position).toString()

                    if (transportation == "Choose a Transportation") {
                        binding.tblQuestions.visibility = View.GONE
                        binding.tblTransportationList.visibility = View.VISIBLE
                    } else {
                        var icon = 0

                        when (binding.spinnerTransportation.selectedItem.toString()) {
                            "Tricycle" -> {
                                icon = getResources().getIdentifier(
                                    "new_icon_tricycle",
                                    "mipmap",
                                    getPackageName()
                                )
                            }

                            "Jeep" -> {
                                icon = getResources().getIdentifier(
                                    "new_icon_jeep",
                                    "mipmap",
                                    getPackageName()
                                )
                            }

                            "Electric Jeep" -> {
                                icon = getResources().getIdentifier(
                                    "new_icon_jeep_electric",
                                    "mipmap",
                                    getPackageName()
                                )
                            }

                            "Airconditioned Electric Jeep" -> {
                                icon = getResources().getIdentifier(
                                    "new_icon_jeep_aircon",
                                    "mipmap",
                                    getPackageName()
                                )
                            }

                            "Bus" -> {
                                icon = getResources().getIdentifier(
                                    "new_icon_bus",
                                    "mipmap",
                                    getPackageName()
                                )
                            }

                            "Economy Bus" -> {
                                icon = getResources().getIdentifier(
                                    "new_icon_bus_economy",
                                    "mipmap",
                                    getPackageName()
                                )
                            }

                            "Airconditioned Bus" -> {
                                icon = getResources().getIdentifier(
                                    "new_icon_bus_aircon",
                                    "mipmap",
                                    getPackageName()
                                )
                            }

                            "UV Express Van" -> {
                                icon = getResources().getIdentifier(
                                    "new_icon_uvexpress",
                                    "mipmap",
                                    getPackageName()
                                )
                            }

                            "UV Express Taxi" -> {
                                icon = getResources().getIdentifier(
                                    "new_icon_taxi",
                                    "mipmap",
                                    getPackageName()
                                )
                            }
                        }

                        binding.tblQuestions.visibility = View.VISIBLE
                        binding.tblTransportationList.visibility = View.GONE
                        binding.iconTravelCategory.setImageResource(icon)
                        binding.txtTranportationBill.text.clear()
                        binding.lblTransportationEmission.text = ""
                        val adapter = TableAdapter(transportationList, this@DailyCheckUp)
                        recyclerView.adapter = adapter
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case where nothing is selected
                }
            }

        binding.txtTranportationBill.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                FirebaseQuery().readUserProfile(userId) {
                    var userType = it.userType
                    val province = it.province
                    val enteredText = editable.toString()
                    var discount = false
                    if (userType == "Student" || userType == "Senior" || userType == "PWD") {
                        discount = true
                    }


                    if (enteredText.isEmpty()) {
                        binding.txtTranportationBill.error = "Complete this field first"
                        binding.lblTransportationEmission.text = ""
                    } else {
                        val amount = enteredText.toDouble()
                        binding.txtTranportationBill.error = null
                        when (binding.spinnerTransportation.selectedItem.toString()) {
                            "Tricycle" -> {
                                EmissionCalculation().calculateTrikeUnleadedGasEmissions(
                                    province = province,
                                    discount = discount,
                                    amount = amount
                                ) { emission ->
                                    binding.lblTransportationEmission.text = "${emission}"
                                }
                            }

                            "Jeep" -> {
                                EmissionCalculation().calculateDieselEmissions(
                                    province = province,
                                    discount = discount,
                                    amount = amount
                                ) { emission ->
                                    binding.lblTransportationEmission.text = "${emission}"
                                }
                            }

                            "Electric Jeep" -> {
                                EmissionCalculation().calculateElectricPUJEmission(
                                    province = province,
                                    discount = discount,
                                    amount = amount
                                ) { emission ->
                                    binding.lblTransportationEmission.text = "${emission}"
                                }
                            }

                            "Airconditioned Electric Jeep" -> {
                                EmissionCalculation().calculateElectricPUJAEmission(
                                    province = province,
                                    discount = discount,
                                    amount = amount
                                ) { emission ->
                                    binding.lblTransportationEmission.text = "${emission}"
                                }
                            }

                            "Bus" -> {
                                EmissionCalculation().calculatePUBUnleadedGasEmissions(
                                    province = province,
                                    discount = discount,
                                    amount = amount
                                ) { emission ->
                                    binding.lblTransportationEmission.text = "${emission}"
                                }
                            }

                            "Economy Bus" -> {
                                EmissionCalculation().calculatePUBEUnleadedGasEmissions(
                                    province = province,
                                    discount = discount,
                                    amount = amount
                                ) { emission ->
                                    binding.lblTransportationEmission.text = "${emission}"
                                }
                            }

                            "Airconditioned Bus" -> {
                                EmissionCalculation().calculatePUBAUnleadedGasEmissions(
                                    province = province,
                                    discount = discount,
                                    amount = amount
                                ) { emission ->
                                    binding.lblTransportationEmission.text = "${emission}"
                                }
                            }

                            "UV Express Van" -> {
                                EmissionCalculation().calculatePUBUnleadedGasEmissions(
                                    province = province,
                                    discount = discount,
                                    amount = amount
                                ) { emission ->
                                    binding.lblTransportationEmission.text = "${emission}"
                                }
                            }

                            "UV Express Taxi" -> {
                                EmissionCalculation().calculatePUBUnleadedGasEmissions(
                                    province = province,
                                    discount = discount,
                                    amount = amount
                                ) { emission ->
                                    binding.lblTransportationEmission.text = "${emission}"
                                }
                            }
                        }
                    }
                }
            }
        })

        binding.btnInsertTransportation.setOnClickListener {
            if (binding.txtTranportationBill.text.isEmpty()) {
                binding.txtTranportationBill.error = "Complete this Field"
                return@setOnClickListener
            }
            if (binding.lblTransportationEmission.text.toString().toDouble() == -1.0) {
                binding.txtTranportationBill.error = "The fare you enter is below the minimum fare"
                return@setOnClickListener
            }
            binding.txtTranportationBill.error = null


            val transportation = binding.spinnerTransportation.selectedItem.toString()
            val fare = binding.txtTranportationBill.text.toString()
            val emission = binding.lblTransportationEmission.text.toString().toDouble()
            transportationList.add(DataModel(transportation, fare))
            transportationEmissionList.add(emission)
            transportationCategoryList.add(transportation)

            when (transportation) {
                "Tricycle" -> travelTricycleEmissionList.add(emission)
                "Jeep" -> travelPUJEmissionList.add(emission)
                "Electric Jeep" -> travelEPUJEmissionList.add(emission)
                "Airconditioned Electric Jeep" -> travelEPUJAEmissionList.add(emission)
                "Bus" -> travelPUBEmissionList.add(emission)
                "Economy Bus" -> travelPUBEEmissionList.add(emission)
                "Airconditioned Bus" -> travelPUBAEmissionList.add(emission)
                "UV Express Van" -> travelUVExpressEmissionList.add(emission)
                "UV Express Taxi" -> travelTaxiEmissionList.add(emission)
            }

            binding.lblTransportationTotalEmission.text = "${
                computeTotalTransportationEmission(
                    transportationEmissionList
                )
            }"

            binding.txtTranportationBill.text.clear()
            binding.lblTransportationEmission.text = ""

            binding.spinnerTransportation.setSelection(0)
        }

        binding.txtItemNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                val int = editable.toString()

                if (transportationList.isEmpty()) {
                    binding.txtItemNumber.text.clear()
                    Toast.makeText(this@DailyCheckUp, "The List is Empty.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                if (binding.txtItemNumber.text.toString() != "" && int.toInt() > transportationList.size) {
                    Toast.makeText(
                        this@DailyCheckUp,
                        "You can only input from number 1 - ${transportationList.size}.",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.txtItemNumber.text.clear()
                    return
                }
            }
        })

        binding.btnDeleteTransportation.setOnClickListener {
            if (transportationList.isEmpty()) {
                Toast.makeText(this@DailyCheckUp, "The List is Empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.txtItemNumber.text.toString() == "") {
                Toast.makeText(this@DailyCheckUp, "Select an Item in the List.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


            val index = binding.txtItemNumber.text.toString().toInt() - 1

            transportationList.removeAt(index)
            val category = transportationCategoryList.elementAt(index)
            transportationCategoryList.removeAt(index)
            val emission = transportationEmissionList.elementAt(index)
            transportationEmissionList.removeAt(index)

            when (category) {
                "Tricycle" -> travelTricycleEmissionList.remove(emission)
                "Jeep" -> travelPUJEmissionList.remove(emission)
                "Electric Jeep" -> travelEPUJEmissionList.remove(emission)
                "Airconditioned Electric Jeep" -> travelEPUJAEmissionList.remove(emission)
                "Bus" -> travelPUBEmissionList.remove(emission)
                "Economy Bus" -> travelPUBEEmissionList.remove(emission)
                "Airconditioned Bus" -> travelPUBAEmissionList.remove(emission)
                "UV Express Van" -> travelUVExpressEmissionList.remove(emission)
                "UV Express Taxi" -> travelTaxiEmissionList.remove(emission)
            }

            val adapter = TableAdapter(transportationList, this)
            recyclerView.adapter = adapter
            binding.lblTransportationTotalEmission.text = "${
                computeTotalTransportationEmission(
                    transportationEmissionList
                )
            }"

            binding.txtItemNumber.text.clear()
        }
        binding.btnSubmitTravel.setOnClickListener {
            if (binding.lblDieselEmission.text.isNotEmpty()) {
                dieselEmission = binding.lblDieselEmission.text.toString().toDouble()
            }

            if (binding.lblUnleadedEmission.text.isNotEmpty()) {
                unleadedEmission = binding.lblUnleadedEmission.text.toString().toDouble()
            }

            if (binding.lblPremiumEmission.text.isNotEmpty()) {
                premiumEmission = binding.lblPremiumEmission.text.toString().toDouble()
            }

            if (binding.lblVehicleTotalEmission.text.isNotEmpty()) {
                vehicleEmission = binding.lblVehicleTotalEmission.text.toString().toDouble()
            }

            tricycleEmission = computeTotalTransportationEmission(
                travelTricycleEmissionList
            )

            pUJEmission = computeTotalTransportationEmission(
                travelPUJEmissionList
            )

            ePUJEmission = computeTotalTransportationEmission(
                travelEPUJEmissionList
            )

            ePUJAEmission = computeTotalTransportationEmission(
                travelEPUJAEmissionList
            )

            pUBEmission = computeTotalTransportationEmission(
                travelPUBEmissionList
            )

            pUBAEmission = computeTotalTransportationEmission(
                travelPUBAEmissionList
            )

            pUBEEmission = computeTotalTransportationEmission(
                travelPUBEEmissionList
            )

            taxiEmission = computeTotalTransportationEmission(
                travelTaxiEmissionList
            )

            uVExpressEmission = computeTotalTransportationEmission(
                travelUVExpressEmissionList
            )

            if (binding.lblHouseTotalEmission.text.isNotEmpty()) {
                houseEmission = binding.lblHouseTotalEmission.text.toString().toDouble()
            }


            if (binding.lblFoodTotalEmission.text.isNotEmpty()) {
                foodEmission = binding.lblFoodTotalEmission.text.toString().toDouble()
            }


            if (binding.lblVehicleTotalEmission.text.isNotEmpty()) {
                vehicleEmission = binding.lblVehicleTotalEmission.text.toString().toDouble()
            }


            if (binding.lblTransportationTotalEmission.text.isNotEmpty()) {
                travelEmission = binding.lblTransportationTotalEmission.text.toString().toDouble()
            }

            user?.let {
                //Update Daily House Emission category and sub-category
                updateUserDailyHomeEmissionDatabase(
                    it,
                    houseEmission
                )
                updateUserDailyHome(
                    it,
                    electricityEmission,
                    charcoalEmission,
                    firewoodEmission,
                    lpgEmission
                )

                //Update Daily Food Emission category and sub-category
                updateUserDailyFoodEmission(
                    it,
                    foodEmission
                )
                updateUserFoodEmission(
                    it,
                    meatEmission,
                    fishEmission,
                    crustaceansEmission,
                    mollusksEmission
                )

                //Update Daily Vehicle Emission category and sub-category
                updateUserVehicleEmissionDatabase(it, vehicleEmission)
                updateVehicleEmission(it, premiumEmission, dieselEmission, unleadedEmission)

                //Update Daily Travel Emission category and sub-category
                updateUserDailyTravelEmissionDatabase(it, travelEmission)
                updateDailyTravelEmission(
                    it,
                    tricycleEmission,
                    pUJEmission,
                    ePUJEmission,
                    ePUJAEmission,
                    pUBEmission,
                    pUBEEmission,
                    pUBEEmission,
                    uVExpressEmission,
                    taxiEmission
                )

                //Update User Monthly Emission
                getTotalMonthlyEmissionForUser(user, year, month)
                getHomeTotalMonthlyEmissionForUser(user, year, month)
                getFoodTotalMonthlyEmissionForUser(user, year, month)
                getVehicleTotalMonthlyEmissionForUser(user, year, month)
                getTravelTotalMonthlyEmissionForUser(user, year, month)

                //Retrieve User Profile data
                FirebaseQuery().readUserProfile(userId) { profile ->
                    val region = profile.region
                    val province = profile.province
                    val municipality = profile.municipality
                    //Retrieve location emission data
                    FirebaseQuery().readLocationEmission(
                        region,
                        province,
                        municipality,
                        year,
                        month
                    ) { locationEmission ->
                        val regionEmission = locationEmission.regionEmission
                        val provinceEmission = locationEmission.provinceEmission
                        val municipalityEmission = locationEmission.municipalityEmission

                        //Update Region, Province, and Municipality Total Emission for current year and month
                        updateRegionEmission(
                            regionEmission,
                            year,
                            month,
                            region,
                            houseEmission,
                            foodEmission,
                            vehicleEmission,
                            travelEmission
                        )
                        updateProvinceEmission(
                            provinceEmission,
                            year,
                            month,
                            region,
                            province,
                            houseEmission, foodEmission, vehicleEmission, travelEmission
                        )
                        updateMunicipalityEmission(
                            municipalityEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            houseEmission, foodEmission, vehicleEmission, travelEmission
                        )


                        //Update Region, Province, and Municipality House sub-category Emission for current year and month
                        updateRegionCategoryEmission(
                            electricityEmission,
                            year,
                            month,
                            region,
                            "Electricity",
                            "House"
                        )
                        updateRegionCategoryEmission(
                            charcoalEmission,
                            year,
                            month,
                            region,
                            "Charcoal",
                            "House"
                        )
                        updateRegionCategoryEmission(
                            firewoodEmission,
                            year,
                            month,
                            region,
                            "Firewood",
                            "House"
                        )
                        updateRegionCategoryEmission(
                            lpgEmission,
                            year,
                            month,
                            region,
                            "LPG",
                            "House"
                        )
                        updateProvinceCategoryEmission(
                            electricityEmission,
                            year,
                            month,
                            region,
                            province,
                            "Electricity",
                            "House"
                        )
                        updateProvinceCategoryEmission(
                            charcoalEmission,
                            year,
                            month,
                            region,
                            province,
                            "Charcoal",
                            "House"
                        )
                        updateProvinceCategoryEmission(
                            firewoodEmission,
                            year,
                            month,
                            region,
                            province,
                            "Firewood",
                            "House"
                        )
                        updateProvinceCategoryEmission(
                            lpgEmission,
                            year,
                            month,
                            region,
                            province,
                            "LPG",
                            "House"
                        )
                        updateMunicipalityCategoryEmission(
                            electricityEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Electricity",
                            "House"
                        )
                        updateMunicipalityCategoryEmission(
                            charcoalEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Charcoal",
                            "House"
                        )
                        updateMunicipalityCategoryEmission(
                            firewoodEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Firewood",
                            "House"
                        )
                        updateMunicipalityCategoryEmission(
                            lpgEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "LPG",
                            "House"
                        )

                        //Update House Emission on MapEmission table
                        updateMapCategoryEmission(
                            electricityEmission,
                            region,
                            province,
                            municipality,
                            "Electricity",
                            "House"
                        )
                        updateMapCategoryEmission(
                            charcoalEmission,
                            region,
                            province,
                            municipality,
                            "Charcoal",
                            "House"
                        )
                        updateMapCategoryEmission(
                            firewoodEmission,
                            region,
                            province,
                            municipality,
                            "Firewood",
                            "House"
                        )
                        updateMapCategoryEmission(
                            lpgEmission,
                            region,
                            province,
                            municipality,
                            "LPG",
                            "House"
                        )

                        /*getTotalMonthlyHouseEmissionForUser(
                            user,
                            year,
                            month,
                            region,
                            province,
                            municipality
                        )*/

                        //Update Region, Province, and Municipality Food sub-category Emission for current year and month
                        updateRegionCategoryEmission(
                            meatEmission,
                            year,
                            month,
                            region,
                            "Meat",
                            "Food"
                        )
                        updateRegionCategoryEmission(
                            fishEmission,
                            year,
                            month,
                            region,
                            "Fish",
                            "Food"
                        )
                        updateRegionCategoryEmission(
                            crustaceansEmission,
                            year,
                            month,
                            region,
                            "Crustaceans",
                            "Food"
                        )
                        updateRegionCategoryEmission(
                            mollusksEmission,
                            year,
                            month,
                            region,
                            "Mollusk",
                            "Food"
                        )
                        updateProvinceCategoryEmission(
                            meatEmission,
                            year,
                            month,
                            region,
                            province,
                            "Meat",
                            "Food"
                        )
                        updateProvinceCategoryEmission(
                            fishEmission,
                            year,
                            month,
                            region,
                            province,
                            "Fish",
                            "Food"
                        )
                        updateProvinceCategoryEmission(
                            crustaceansEmission,
                            year,
                            month,
                            region,
                            province,
                            "Crustaceans",
                            "Food"
                        )
                        updateProvinceCategoryEmission(
                            mollusksEmission,
                            year,
                            month,
                            region,
                            province,
                            "Mollusks",
                            "Food"
                        )
                        updateMunicipalityCategoryEmission(
                            meatEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Meat",
                            "Food"
                        )
                        updateMunicipalityCategoryEmission(
                            fishEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Fish",
                            "Food"
                        )
                        updateMunicipalityCategoryEmission(
                            crustaceansEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Crustaceans",
                            "Food"
                        )
                        updateMunicipalityCategoryEmission(
                            mollusksEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Mollusks",
                            "Food"
                        )

                        //Update Food Emission on MapEmission table
                        updateMapCategoryEmission(
                            meatEmission,
                            region,
                            province,
                            municipality,
                            "Meat",
                            "Food"
                        )
                        updateMapCategoryEmission(
                            fishEmission,
                            region,
                            province,
                            municipality,
                            "Fish",
                            "Food"
                        )
                        updateMapCategoryEmission(
                            crustaceansEmission,
                            region,
                            province,
                            municipality,
                            "Crustaceans",
                            "Food"
                        )
                        updateMapCategoryEmission(
                            mollusksEmission,
                            region,
                            province,
                            municipality,
                            "Mollusks",
                            "Food"
                        )
                        /*
                        getTotalMonthlyFoodEmissionForUser(
                            user,
                            year,
                            month,
                            region,
                            province,
                            municipality
                        )*/

                        //Update Region, Province, and Municipality Vehicle sub-category Emission for current year and month
                        updateRegionCategoryEmission(
                            premiumEmission,
                            year,
                            month,
                            region,
                            "Premium",
                            "Vehicle"
                        )
                        updateRegionCategoryEmission(
                            unleadedEmission,
                            year,
                            month,
                            region,
                            "Unleaded",
                            "Vehicle"
                        )
                        updateRegionCategoryEmission(
                            dieselEmission,
                            year,
                            month,
                            region,
                            "Diesel",
                            "Vehicle"
                        )
                        updateProvinceCategoryEmission(
                            premiumEmission,
                            year,
                            month,
                            region,
                            province,
                            "Premium",
                            "Vehicle"
                        )
                        updateProvinceCategoryEmission(
                            unleadedEmission,
                            year,
                            month,
                            region,
                            province,
                            "Unleaded",
                            "Vehicle"
                        )
                        updateProvinceCategoryEmission(
                            dieselEmission,
                            year,
                            month,
                            region,
                            province,
                            "Diesel",
                            "Vehicle"
                        )
                        updateMunicipalityCategoryEmission(
                            premiumEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Premium",
                            "Vehicle"
                        )
                        updateMunicipalityCategoryEmission(
                            unleadedEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Unleaded",
                            "Vehicle"
                        )
                        updateMunicipalityCategoryEmission(
                            dieselEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Diesel",
                            "Vehicle"
                        )

                        //Update Vehicle Emission on MapEmission table
                        updateMapCategoryEmission(
                            premiumEmission,
                            region,
                            province,
                            municipality,
                            "Premium",
                            "Vehicle"
                        )
                        updateMapCategoryEmission(
                            unleadedEmission,
                            region,
                            province,
                            municipality,
                            "Unleaded",
                            "Vehicle"
                        )
                        updateMapCategoryEmission(
                            dieselEmission,
                            region,
                            province,
                            municipality,
                            "Diesel",
                            "Vehicle"
                        )
                        /*
                        getTotalMonthlyVehicleEmissionForUser(
                            user,
                            year,
                            month,
                            region,
                            province,
                            municipality
                        )*/


                        //Update Region, Province, and Municipality Travel sub-category Emission for current year and month
                        updateRegionCategoryEmission(
                            tricycleEmission,
                            year,
                            month,
                            region,
                            "Tricycle",
                            "Travel"
                        )
                        updateRegionCategoryEmission(
                            pUJEmission,
                            year,
                            month,
                            region,
                            "Jeep",
                            "Travel"
                        )
                        updateRegionCategoryEmission(
                            ePUJEmission,
                            year,
                            month,
                            region,
                            "Electric Jeep",
                            "Travel"
                        )
                        updateRegionCategoryEmission(
                            ePUJAEmission,
                            year,
                            month,
                            region,
                            "Airconditioned Electric Jeep",
                            "Travel"
                        )
                        updateRegionCategoryEmission(
                            pUBEmission,
                            year,
                            month,
                            region,
                            "Bus",
                            "Travel"
                        )
                        updateRegionCategoryEmission(
                            pUBEEmission,
                            year,
                            month,
                            region,
                            "Economy Bus",
                            "Travel"
                        )
                        updateRegionCategoryEmission(
                            pUBAEmission,
                            year,
                            month,
                            region,
                            "Airconditioned Bus",
                            "Travel"
                        )
                        updateRegionCategoryEmission(
                            uVExpressEmission,
                            year,
                            month,
                            region,
                            "UV Express Van",
                            "Travel"
                        )
                        updateRegionCategoryEmission(
                            taxiEmission,
                            year,
                            month,
                            region,
                            "UV Express Taxi",
                            "Travel"
                        )

                        updateProvinceCategoryEmission(
                            tricycleEmission,
                            year,
                            month,
                            region,
                            province,
                            "Tricycle",
                            "Travel"
                        )
                        updateProvinceCategoryEmission(
                            pUJEmission,
                            year,
                            month,
                            region,
                            province,
                            "Jeep",
                            "Travel"
                        )
                        updateProvinceCategoryEmission(
                            ePUJEmission,
                            year,
                            month,
                            region,
                            province,
                            "Electric Jeep",
                            "Travel"
                        )
                        updateProvinceCategoryEmission(
                            ePUJAEmission,
                            year,
                            month,
                            region,
                            province,
                            "Airconditioned Electric Jeep",
                            "Travel"
                        )
                        updateProvinceCategoryEmission(
                            pUBEmission,
                            year,
                            month,
                            region,
                            province,
                            "Bus",
                            "Travel"
                        )
                        updateProvinceCategoryEmission(
                            pUBEEmission,
                            year,
                            month,
                            region,
                            province,
                            "Economy Bus",
                            "Travel"
                        )
                        updateProvinceCategoryEmission(
                            pUBAEmission,
                            year,
                            month,
                            region,
                            province,
                            "Airconditioned Bus",
                            "Travel"
                        )
                        updateProvinceCategoryEmission(
                            uVExpressEmission,
                            year,
                            month,
                            region,
                            province,
                            "UV Express Van",
                            "Travel"
                        )
                        updateProvinceCategoryEmission(
                            taxiEmission,
                            year,
                            month,
                            region,
                            province,
                            "UV Express Taxi",
                            "Travel"
                        )

                        updateMunicipalityCategoryEmission(
                            tricycleEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Tricycle",
                            "Travel"
                        )
                        updateMunicipalityCategoryEmission(
                            pUJEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Jeep",
                            "Travel"
                        )
                        updateMunicipalityCategoryEmission(
                            ePUJEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Electric Jeep",
                            "Travel"
                        )
                        updateMunicipalityCategoryEmission(
                            ePUJAEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Airconditioned Electric Jeep",
                            "Travel"
                        )
                        updateMunicipalityCategoryEmission(
                            pUBEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Bus",
                            "Travel"
                        )
                        updateMunicipalityCategoryEmission(
                            pUBEEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Economy Bus",
                            "Travel"
                        )
                        updateMunicipalityCategoryEmission(
                            pUBAEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "Airconditioned Bus",
                            "Travel"
                        )
                        updateMunicipalityCategoryEmission(
                            uVExpressEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "UV Express Van",
                            "Travel"
                        )
                        updateMunicipalityCategoryEmission(
                            taxiEmission,
                            year,
                            month,
                            region,
                            province,
                            municipality,
                            "UV Express Taxi",
                            "Travel"
                        )

                        //Update Travel Emission on MapEmission table
                        updateMapCategoryEmission(
                            tricycleEmission,
                            region,
                            province,
                            municipality,
                            "Tricycle",
                            "Travel"
                        )
                        updateMapCategoryEmission(
                            pUJEmission,
                            region,
                            province,
                            municipality,
                            "Jeep",
                            "Travel"
                        )
                        updateMapCategoryEmission(
                            ePUJEmission,
                            region,
                            province,
                            municipality,
                            "Electric Jeep",
                            "Travel"
                        )
                        updateMapCategoryEmission(
                            ePUJAEmission,
                            region,
                            province,
                            municipality,
                            "Airconditioned Electric Jeep",
                            "Travel"
                        )
                        updateMapCategoryEmission(
                            pUBEmission,
                            region,
                            province,
                            municipality,
                            "Bus",
                            "Travel"
                        )
                        updateMapCategoryEmission(
                            pUBEEmission,
                            region,
                            province,
                            municipality,
                            "Economy Bus",
                            "Travel"
                        )
                        updateMapCategoryEmission(
                            pUBAEmission,
                            region,
                            province,
                            municipality,
                            "Airconditioned Bus",
                            "Travel"
                        )
                        updateMapCategoryEmission(
                            uVExpressEmission,
                            region,
                            province,
                            municipality,
                            "UV Express Van",
                            "Travel"
                        )
                        updateMapCategoryEmission(
                            taxiEmission,
                            region,
                            province,
                            municipality,
                            "UV Express Taxi",
                            "Travel"
                        )
                        /*
                        getTotalMonthlyTravelEmissionForUser(
                            user,
                            year,
                            month,
                            region,
                            province,
                            municipality
                        )*/

                        val monthlyEmission =
                            houseEmission + foodEmission + vehicleEmission + travelEmission
                        updateMapTotalEmission(
                            monthlyEmission,
                            region,
                            province,
                            municipality
                        )
                        updateMapTypeEmission(
                            houseEmission,
                            region,
                            province,
                            municipality,
                            "House"
                        )
                        updateMapTypeEmission(
                            foodEmission,
                            region,
                            province,
                            municipality,
                            "Food"
                        )
                        updateMapTypeEmission(
                            vehicleEmission,
                            region,
                            province,
                            municipality,
                            "Vehicle"
                        )
                        updateMapTypeEmission(
                            travelEmission,
                            region,
                            province,
                            municipality,
                            "Travel"
                        )
                    }
                }
            }
            binding.layoutLoading.visibility = View.VISIBLE
            binding.layoutTransportation.visibility = View.GONE
            startRandomTimer()
        }
    }

    private fun updateMapTypeEmission(
        emissionTotal: Double,
        region: String,
        province: String,
        municipality: String,
        type: String
    ) {
        FirebaseQuery().readMapTypeEmission(region, province, municipality, type) { emission ->
            //Write emission data to Realtime database
            val database = Firebase.database
            val userLocationEmissionTable =
                database.getReference("MapEmission")

            //Create a Map Total Emission entry using Category
            val newMapEmissionRef = userLocationEmissionTable.child(region)

            //Set Total Emission data
            newMapEmissionRef.child("$type/TotalEmission")
                .setValue((emissionTotal + emission.regionTypeEmission))
            newMapEmissionRef.child("$province/$type/TotalEmission")
                .setValue((emissionTotal + emission.provinceTypeEmission))
            newMapEmissionRef.child("$province/$municipality/$type/TotalEmission")
                .setValue((emissionTotal + emission.municipalityTypeEmission))
        }
    }


    private fun updateDailyTravelEmission(
        user: FirebaseUser,
        tricycle: Double,
        jeep: Double,
        ejeep: Double,
        ajeep: Double,
        bus: Double,
        ebus: Double,
        abus: Double,
        van: Double,
        taxi: Double
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userDailyEmissionTable = database.getReference("DailyTravelEmission/$year/$month/$day")

        //Create a new DailyTransportationEmission entry using unique id
        val newUserDailyEmissionRef = userDailyEmissionTable.child(userId)

        FirebaseQuery().readUserDailyTravelEmission(user, year, month, day) {
            //Set Transportation Emission data
            newUserDailyEmissionRef.child("Tricycle").setValue(tricycle + it.tricycleEmission)
            newUserDailyEmissionRef.child("Jeep").setValue(jeep + it.jeepEmission)
            newUserDailyEmissionRef.child("Electric Jeep").setValue(ejeep + it.electricJeepEmission)
            newUserDailyEmissionRef.child("Airconditioned Electric Jeep")
                .setValue(ajeep + it.airconditionedElectricJeepEmission)
            newUserDailyEmissionRef.child("Bus").setValue(bus + it.busEmission)
            newUserDailyEmissionRef.child("Economy Bus").setValue(ebus + it.economyBusEmission)
            newUserDailyEmissionRef.child("Airconditioned Bus")
                .setValue(abus + it.airconditionedBusEmission)
            newUserDailyEmissionRef.child("UV Express Van").setValue(van + it.uvVanEmission)
            newUserDailyEmissionRef.child("UV Express Taxi").setValue(taxi + it.uvTaxiEmission)
        }
    }

    private fun updateVehicleEmission(
        user: FirebaseUser,
        premiumEmission: Double,
        dieselEmission: Double,
        unleadedEmission: Double
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userDailyEmissionTable = database.getReference("DailyVehicleEmission/$year/$month/$day")

        //Create a new DailyVehicleEmission entry using unique id
        val newUserDailyEmissionRef = userDailyEmissionTable.child(userId)

        //Retrieve current vehicle sub-category emission
        FirebaseQuery().readUserDailyVehicleEmission(user, year, month, day) {
            //Set new vehicle sub-category total Emission data
            newUserDailyEmissionRef.child("PremiumEmission")
                .setValue(premiumEmission + it.premiumEmission)
            newUserDailyEmissionRef.child("DieselEmission")
                .setValue(dieselEmission + it.dieselEmission)
            newUserDailyEmissionRef.child("UnleadedEmission")
                .setValue(unleadedEmission + it.unleadedEmission)
        }
    }

    private fun updateUserHouseMonthlyEmission(
        user: FirebaseUser,
        electricityEmission: Double,
        charcoalEmission: Double,
        firewoodEmission: Double,
        lpgEmission: Double,
        year: Int,
        month: Int,
        category: String
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userMonthlyEmissionTable =
            database.getReference("UserMonthlyEmission/$year/$month/$userId")

        //Create a new UserMonthlyEmission entry using unique id
        val newUserMonthlyEmissionRef = userMonthlyEmissionTable.child(category)

        //Set User Monthly Emission data
        newUserMonthlyEmissionRef.child("electricityEmission").setValue(electricityEmission)
        newUserMonthlyEmissionRef.child("charcoalEmission").setValue(charcoalEmission)
        newUserMonthlyEmissionRef.child("firewoodEmission").setValue(firewoodEmission)
        newUserMonthlyEmissionRef.child("lpgEmission").setValue(lpgEmission)
    }

    private fun updateUserFoodMonthlyEmission(
        user: FirebaseUser,
        meatEmission: Double,
        fishEmission: Double,
        crustaceansEmission: Double,
        mollusksEmission: Double,
        year: Int,
        month: Int,
        category: String
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userMonthlyEmissionTable =
            database.getReference("UserMonthlyEmission/$year/$month/$userId")

        //Create a new UserMonthlyEmission entry using unique id
        val newUserMonthlyEmissionRef = userMonthlyEmissionTable.child(category)

        //Set User Monthly Emission data
        newUserMonthlyEmissionRef.child("meatEmission").setValue(meatEmission)
        newUserMonthlyEmissionRef.child("fishEmission").setValue(fishEmission)
        newUserMonthlyEmissionRef.child("crustaceansEmission").setValue(crustaceansEmission)
        newUserMonthlyEmissionRef.child("mollusksEmission").setValue(mollusksEmission)
    }

    private fun updateUserVehicleMonthlyEmission(
        user: FirebaseUser,
        premiumEmission: Double,
        dieselEmission: Double,
        unleadedEmission: Double,
        year: Int,
        month: Int,
        category: String
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userMonthlyEmissionTable =
            database.getReference("UserMonthlyEmission/$year/$month/$userId")

        //Create a new UserMonthlyEmission entry using unique id
        val newUserMonthlyEmissionRef = userMonthlyEmissionTable.child(category)

        //Set User Monthly Emission data
        newUserMonthlyEmissionRef.child("premiumEmission").setValue(premiumEmission)
        newUserMonthlyEmissionRef.child("dieselEmission").setValue(dieselEmission)
        newUserMonthlyEmissionRef.child("unleadedEmission").setValue(unleadedEmission)
    }

    private fun updateUserTravelMonthlyEmission(
        user: FirebaseUser,
        tricycle: Double,
        jeep: Double,
        ejeep: Double,
        aejeep: Double,
        bus: Double,
        ebus: Double,
        abus: Double,
        van: Double,
        taxi: Double,
        year: Int,
        month: Int,
        category: String
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userMonthlyEmissionTable =
            database.getReference("UserMonthlyEmission/$year/$month/$userId")

        //Create a new UserMonthlyEmission entry using unique id
        val newUserMonthlyEmissionRef = userMonthlyEmissionTable.child(category)

        //Set User Monthly Emission data
        newUserMonthlyEmissionRef.child("Tricycle").setValue(tricycle)
        newUserMonthlyEmissionRef.child("Jeep").setValue(jeep)
        newUserMonthlyEmissionRef.child("Electric Jeep").setValue(ejeep)
        newUserMonthlyEmissionRef.child("Airconditioned Electric Jeep").setValue(aejeep)
        newUserMonthlyEmissionRef.child("Bus").setValue(bus)
        newUserMonthlyEmissionRef.child("Economy Bus").setValue(ebus)
        newUserMonthlyEmissionRef.child("Airconditioned Bus").setValue(abus)
        newUserMonthlyEmissionRef.child("UV Express Van").setValue(van)
        newUserMonthlyEmissionRef.child("UV Express Taxi").setValue(taxi)
    }

    fun getHomeTotalMonthlyEmissionForUser(user: FirebaseUser, year: Int, month: Int) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("DailyHomeEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var electricityEmission = 0.0
                var charcoalEmission = 0.0
                var firewoodEmission = 0.0
                var lpgEmission = 0.0

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef =
                        daySnapshot.child(userId) // Get the user's emission for that day
                    val electricity =
                        userEmissionRef.child("electricityEmission").getValue(Double::class.java)
                            ?: 0.0
                    val charcoal =
                        userEmissionRef.child("charcoalEmission").getValue(Double::class.java)
                            ?: 0.0
                    val firewood =
                        userEmissionRef.child("firewoodEmission").getValue(Double::class.java)
                            ?: 0.0
                    val lpg =
                        userEmissionRef.child("lpgEmission").getValue(Double::class.java) ?: 0.0

                    electricityEmission += electricity
                    charcoalEmission += charcoal
                    firewoodEmission += firewood
                    lpgEmission += lpg
                }

                //Use totalEmission here to pass the result to UserMonthlyEmission
                updateUserHouseMonthlyEmission(
                    user,
                    electricityEmission,
                    charcoalEmission,
                    firewoodEmission,
                    lpgEmission,
                    year,
                    month,
                    "House"
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }

    fun getFoodTotalMonthlyEmissionForUser(user: FirebaseUser, year: Int, month: Int) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("DailyFoodEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var meatEmission = 0.0
                var fishEmission = 0.0
                var crustaceansEmission = 0.0
                var mollusksEmission = 0.0

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef =
                        daySnapshot.child(userId) // Get the user's emission for that day
                    val meat =
                        userEmissionRef.child("meatEmission").getValue(Double::class.java)
                            ?: 0.0
                    val fish =
                        userEmissionRef.child("fishEmission").getValue(Double::class.java)
                            ?: 0.0
                    val crustaceans =
                        userEmissionRef.child("crustaceansEmission").getValue(Double::class.java)
                            ?: 0.0
                    val mollusks =
                        userEmissionRef.child("mollusksEmission").getValue(Double::class.java)
                            ?: 0.0

                    meatEmission += meat
                    fishEmission += fish
                    crustaceansEmission += crustaceans
                    mollusksEmission += mollusks
                }

                //Use totalEmission here to pass the result to UserMonthlyEmission
                updateUserFoodMonthlyEmission(
                    user,
                    meatEmission,
                    fishEmission,
                    crustaceansEmission,
                    mollusksEmission,
                    year,
                    month,
                    "Food"
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }

    fun getVehicleTotalMonthlyEmissionForUser(user: FirebaseUser, year: Int, month: Int) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("DailyVehicleEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var premiumEmission = 0.0
                var dieselEmission = 0.0
                var unleadedEmission = 0.0

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef =
                        daySnapshot.child(userId) // Get the user's emission for that day
                    val premium =
                        userEmissionRef.child("PremiumEmission").getValue(Double::class.java)
                            ?: 0.0
                    val diesel =
                        userEmissionRef.child("DieselEmission").getValue(Double::class.java)
                            ?: 0.0
                    val unleaded =
                        userEmissionRef.child("UnleadedEmission").getValue(Double::class.java)
                            ?: 0.0

                    premiumEmission += premium
                    dieselEmission += diesel
                    unleadedEmission += unleaded
                }

                //Use totalEmission here to pass the result to UserMonthlyEmission
                updateUserVehicleMonthlyEmission(
                    user,
                    premiumEmission,
                    dieselEmission,
                    unleadedEmission,
                    year,
                    month,
                    "Vehicle"
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }

    fun getTravelTotalMonthlyEmissionForUser(user: FirebaseUser, year: Int, month: Int) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("DailyTravelEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var tricycleEmission = 0.0
                var pujEmission = 0.0
                var epujEmission = 0.0
                var epujaEmission = 0.0
                var pubEmission = 0.0
                var epubEmission = 0.0
                var pubaEmission = 0.0
                var vanEmission = 0.0
                var taxiEmission = 0.0

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef =
                        daySnapshot.child(userId) // Get the user's emission for that day
                    val tricycle =
                        userEmissionRef.child("Tricycle").getValue(Double::class.java) ?: 0.0
                    val jeep = userEmissionRef.child("Jeep").getValue(Double::class.java) ?: 0.0
                    val ejeep =
                        userEmissionRef.child("Electric Jeep").getValue(Double::class.java) ?: 0.0
                    val aejeep = userEmissionRef.child("Airconditioned Electric Jeep")
                        .getValue(Double::class.java) ?: 0.0
                    val bus = userEmissionRef.child("Bus").getValue(Double::class.java) ?: 0.0
                    val ebus =
                        userEmissionRef.child("Economy Bus").getValue(Double::class.java) ?: 0.0
                    val abus =
                        userEmissionRef.child("Airconditioned Bus").getValue(Double::class.java)
                            ?: 0.0
                    val van =
                        userEmissionRef.child("UV Express Van").getValue(Double::class.java) ?: 0.0
                    val taxi =
                        userEmissionRef.child("UV Express Taxi").getValue(Double::class.java) ?: 0.0

                    tricycleEmission += tricycle
                    pujEmission += jeep
                    epujEmission += ejeep
                    epujaEmission += aejeep
                    pubEmission += bus
                    epubEmission += ebus
                    pubaEmission += abus
                    vanEmission += van
                    taxiEmission += taxi
                }
                updateUserTravelMonthlyEmission(
                    user,
                    tricycleEmission,
                    pujEmission,
                    epujEmission,
                    epujaEmission,
                    pubEmission,
                    epubEmission,
                    pubaEmission,
                    vanEmission,
                    taxiEmission,
                    year,
                    month,
                    "Travel"
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun getTotalMonthlyEmissionForUser(user: FirebaseUser, year: Int, month: Int) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("UserDailyEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var houseEmission = 0.0
                var foodEmission = 0.0
                var travelEmission = 0.0
                var vehicleEmission = 0.0

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef =
                        daySnapshot.child(userId) // Get the user's emission for that day
                    val house =
                        userEmissionRef.child("houseEmission").getValue(Double::class.java) ?: 0.0
                    val food =
                        userEmissionRef.child("foodEmission").getValue(Double::class.java) ?: 0.0
                    val vehicle =
                        userEmissionRef.child("vehicleEmission").getValue(Double::class.java) ?: 0.0
                    val travel =
                        userEmissionRef.child("travelEmission").getValue(Double::class.java) ?: 0.0

                    houseEmission += house
                    foodEmission += food
                    vehicleEmission += vehicle
                    travelEmission += travel
                }

                //Use totalEmission here to pass the result to UserMonthlyEmission
                updateUserMonthlyEmission(
                    user,
                    houseEmission,
                    foodEmission,
                    vehicleEmission,
                    travelEmission,
                    year,
                    month
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }


    fun getTotalMonthlyHouseEmissionForUser(
        user: FirebaseUser,
        year: Int,
        month: Int,
        region: String,
        province: String,
        municipality: String
    ) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("DailyHomeEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var electricityEmission = 0.0
                var charcoalEmission = 0.0
                var firewoodEmission = 0.0
                var lpgEmission = 0.0

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef =
                        daySnapshot.child(userId) // Get the user's emission for that day
                    val electricity =
                        userEmissionRef.child("electricityEmission").getValue(Double::class.java)
                            ?: 0.0
                    val charcoal =
                        userEmissionRef.child("charcoalEmission").getValue(Double::class.java)
                            ?: 0.0
                    val firewood =
                        userEmissionRef.child("firewoodEmission").getValue(Double::class.java)
                            ?: 0.0
                    val lpg =
                        userEmissionRef.child("lpgEmission").getValue(Double::class.java) ?: 0.0

                    electricityEmission += electricity
                    charcoalEmission += charcoal
                    firewoodEmission += firewood
                    lpgEmission += lpg
                }
                // Use the monthlyEmission as needed
                updateRegionCategoryEmission(
                    electricityEmission,
                    year,
                    month,
                    region,
                    "Electricity",
                    "House"
                )
                updateRegionCategoryEmission(
                    charcoalEmission,
                    year,
                    month,
                    region,
                    "Charcoal",
                    "House"
                )
                updateRegionCategoryEmission(
                    firewoodEmission,
                    year,
                    month,
                    region,
                    "Firewood",
                    "House"
                )
                updateRegionCategoryEmission(lpgEmission, year, month, region, "LPG", "House")
                updateProvinceCategoryEmission(
                    electricityEmission,
                    year,
                    month,
                    region,
                    province,
                    "Electricity",
                    "House"
                )
                updateProvinceCategoryEmission(
                    charcoalEmission,
                    year,
                    month,
                    region,
                    province,
                    "Charcoal",
                    "House"
                )
                updateProvinceCategoryEmission(
                    firewoodEmission,
                    year,
                    month,
                    region,
                    province,
                    "Firewood",
                    "House"
                )
                updateProvinceCategoryEmission(
                    lpgEmission,
                    year,
                    month,
                    region,
                    province,
                    "LPG",
                    "House"
                )
                updateMunicipalityCategoryEmission(
                    electricityEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Electricity",
                    "House"
                )
                updateMunicipalityCategoryEmission(
                    charcoalEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Charcoal",
                    "House"
                )
                updateMunicipalityCategoryEmission(
                    firewoodEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Firewood",
                    "House"
                )
                updateMunicipalityCategoryEmission(
                    lpgEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "LPG",
                    "House"
                )
                updateMapCategoryEmission(
                    electricityEmission,
                    region,
                    province,
                    municipality,
                    "Electricity",
                    "House"
                )
                updateMapCategoryEmission(
                    charcoalEmission,
                    region,
                    province,
                    municipality,
                    "Charcoal",
                    "House"
                )
                updateMapCategoryEmission(
                    firewoodEmission,
                    region,
                    province,
                    municipality,
                    "Firewood",
                    "House"
                )
                updateMapCategoryEmission(
                    lpgEmission,
                    region,
                    province,
                    municipality,
                    "LPG",
                    "House"
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateMapCategoryEmission(
        monthlyEmission: Double,
        region: String,
        province: String,
        municipality: String,
        category: String,
        type: String
    ) {
        FirebaseQuery().readMapCategoryEmission(
            region,
            province,
            municipality,
            type,
            category
        ) { emission ->
            //Write emission data to Realtime database
            val database = Firebase.database
            val userLocationEmissionTable =
                database.getReference("MapEmission")

            //Create a Map Total Emission entry using Category
            val newMapEmissionRef = userLocationEmissionTable.child(region)

            //Set Total Emission data
            newMapEmissionRef.child("$type/$category/TotalEmission")
                .setValue((monthlyEmission + emission.regionCategoryEmission))
            newMapEmissionRef.child("$province/$type/$category/TotalEmission")
                .setValue((monthlyEmission + emission.provinceCategoryEmission))
            newMapEmissionRef.child("$province/$municipality/$type/$category/TotalEmission")
                .setValue((monthlyEmission + emission.municipalityCategoryEmission))
        }
    }


    fun getTotalMonthlyTravelEmissionForUser(
        user: FirebaseUser,
        year: Int,
        month: Int,
        region: String,
        province: String,
        municipality: String
    ) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("DailyTravelEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var tricycleEmission = 0.0
                var pujEmission = 0.0
                var epujEmission = 0.0
                var epujaEmission = 0.0
                var pubEmission = 0.0
                var epubEmission = 0.0
                var pubaEmission = 0.0
                var vanEmission = 0.0
                var taxiEmission = 0.0

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef =
                        daySnapshot.child(userId) // Get the user's emission for that day
                    val tricycle =
                        userEmissionRef.child("Tricycle").getValue(Double::class.java) ?: 0.0
                    val jeep = userEmissionRef.child("Jeep").getValue(Double::class.java) ?: 0.0
                    val ejeep =
                        userEmissionRef.child("Electric Jeep").getValue(Double::class.java) ?: 0.0
                    val aejeep = userEmissionRef.child("Airconditioned Electric Jeep")
                        .getValue(Double::class.java) ?: 0.0
                    val bus = userEmissionRef.child("Bus").getValue(Double::class.java) ?: 0.0
                    val ebus =
                        userEmissionRef.child("Economy Bus").getValue(Double::class.java) ?: 0.0
                    val abus =
                        userEmissionRef.child("Airconditioned Bus").getValue(Double::class.java)
                            ?: 0.0
                    val van =
                        userEmissionRef.child("UV Express Van").getValue(Double::class.java) ?: 0.0
                    val taxi =
                        userEmissionRef.child("UV Express Taxi").getValue(Double::class.java) ?: 0.0

                    tricycleEmission += tricycle
                    pujEmission += jeep
                    epujEmission += ejeep
                    epujaEmission += aejeep
                    pubEmission += bus
                    epubEmission += ebus
                    pubaEmission += abus
                    vanEmission += van
                    taxiEmission += taxi
                }
                // Use the monthlyEmission as needed
                updateRegionCategoryEmission(
                    tricycleEmission,
                    year,
                    month,
                    region,
                    "Tricycle",
                    "Travel"
                )
                updateRegionCategoryEmission(pujEmission, year, month, region, "Jeep", "Travel")
                updateRegionCategoryEmission(
                    epujEmission,
                    year,
                    month,
                    region,
                    "Electric Jeep",
                    "Travel"
                )
                updateRegionCategoryEmission(
                    epujaEmission,
                    year,
                    month,
                    region,
                    "Airconditioned Electric Jeep",
                    "Travel"
                )
                updateRegionCategoryEmission(pubEmission, year, month, region, "Bus", "Travel")
                updateRegionCategoryEmission(epubEmission, year, month, region, "Economy", "Travel")
                updateRegionCategoryEmission(
                    pubaEmission,
                    year,
                    month,
                    region,
                    "Airconditioned Bus",
                    "Travel"
                )
                updateRegionCategoryEmission(
                    vanEmission,
                    year,
                    month,
                    region,
                    "UV Express Van",
                    "Travel"
                )
                updateRegionCategoryEmission(
                    taxiEmission,
                    year,
                    month,
                    region,
                    "UV Express Taxi",
                    "Travel"
                )

                updateProvinceCategoryEmission(
                    tricycleEmission,
                    year,
                    month,
                    region,
                    province,
                    "Tricycle",
                    "Travel"
                )
                updateProvinceCategoryEmission(
                    pujEmission,
                    year,
                    month,
                    region,
                    province,
                    "Jeep",
                    "Travel"
                )
                updateProvinceCategoryEmission(
                    epujEmission,
                    year,
                    month,
                    region,
                    province,
                    "Electric Jeep",
                    "Travel"
                )
                updateProvinceCategoryEmission(
                    epujaEmission,
                    year,
                    month,
                    region,
                    province,
                    "Airconditioned Electric Jeep",
                    "Travel"
                )
                updateProvinceCategoryEmission(
                    pubEmission,
                    year,
                    month,
                    region,
                    province,
                    "Bus",
                    "Travel"
                )
                updateProvinceCategoryEmission(
                    pubaEmission,
                    year,
                    month,
                    region,
                    province,
                    "Airconditioned Bus",
                    "Travel"
                )
                updateProvinceCategoryEmission(
                    vanEmission,
                    year,
                    month,
                    region,
                    province,
                    "UV Express Van",
                    "Travel"
                )
                updateProvinceCategoryEmission(
                    taxiEmission,
                    year,
                    month,
                    region,
                    province,
                    "UV Express Taxi",
                    "Travel"
                )

                updateMunicipalityCategoryEmission(
                    tricycleEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Tricycle",
                    "Travel"
                )
                updateMunicipalityCategoryEmission(
                    pujEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Jeep",
                    "Travel"
                )
                updateMunicipalityCategoryEmission(
                    epujEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Electric Jeep",
                    "Travel"
                )
                updateMunicipalityCategoryEmission(
                    epujaEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Airconditioned Electric Jeep",
                    "Travel"
                )
                updateMunicipalityCategoryEmission(
                    pubEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Bus",
                    "Travel"
                )
                updateMunicipalityCategoryEmission(
                    pubaEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Airconditioned Bus",
                    "Travel"
                )
                updateMunicipalityCategoryEmission(
                    vanEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "UV Express Van",
                    "Travel"
                )
                updateMunicipalityCategoryEmission(
                    taxiEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "UV Express Taxi",
                    "Travel"
                )

                updateMapCategoryEmission(
                    tricycleEmission,
                    region,
                    province,
                    municipality,
                    "Tricycle",
                    "Travel"
                )
                updateMapCategoryEmission(
                    pujEmission,
                    region,
                    province,
                    municipality,
                    "Jeep",
                    "Travel"
                )
                updateMapCategoryEmission(
                    epujEmission,
                    region,
                    province,
                    municipality,
                    "Electric Jeep",
                    "Travel"
                )
                updateMapCategoryEmission(
                    epujaEmission,
                    region,
                    province,
                    municipality,
                    "Airconditioned Electric Jeep",
                    "Travel"
                )
                updateMapCategoryEmission(
                    pubEmission,
                    region,
                    province,
                    municipality,
                    "Bus",
                    "Travel"
                )
                updateMapCategoryEmission(
                    pubaEmission,
                    region,
                    province,
                    municipality,
                    "Airconditioned Bus",
                    "Travel"
                )
                updateMapCategoryEmission(
                    vanEmission,
                    region,
                    province,
                    municipality,
                    "UV Express Van",
                    "Travel"
                )
                updateMapCategoryEmission(
                    taxiEmission,
                    region,
                    province,
                    municipality,
                    "UV Express Taxi",
                    "Travel"
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun getTotalMonthlyFoodEmissionForUser(
        user: FirebaseUser,
        year: Int,
        month: Int,
        region: String,
        province: String,
        municipality: String
    ) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("DailyFoodEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var meatEmission = 0.0
                var fishEmission = 0.0
                var crustaceansEmission = 0.0
                var mollusksEmission = 0.0

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef =
                        daySnapshot.child(userId) // Get the user's emission for that day
                    val meat =
                        userEmissionRef.child("meatEmission").getValue(Double::class.java) ?: 0.0
                    val fish =
                        userEmissionRef.child("fishEmission").getValue(Double::class.java) ?: 0.0
                    val crustaceans =
                        userEmissionRef.child("crustaceansEmission").getValue(Double::class.java)
                            ?: 0.0
                    val mollusks =
                        userEmissionRef.child("mollusksEmission").getValue(Double::class.java)
                            ?: 0.0

                    meatEmission += meat
                    fishEmission += fish
                    crustaceansEmission += crustaceans
                    mollusksEmission += mollusks
                }
                // Use the monthlyEmission as needed
                updateRegionCategoryEmission(meatEmission, year, month, region, "Meat", "Food")
                updateRegionCategoryEmission(fishEmission, year, month, region, "Fish", "Food")
                updateRegionCategoryEmission(
                    crustaceansEmission,
                    year,
                    month,
                    region,
                    "Crustaceans",
                    "Food"
                )
                updateRegionCategoryEmission(
                    mollusksEmission,
                    year,
                    month,
                    region,
                    "Mollusk",
                    "Food"
                )
                updateProvinceCategoryEmission(
                    meatEmission,
                    year,
                    month,
                    region,
                    province,
                    "Meat",
                    "Food"
                )
                updateProvinceCategoryEmission(
                    fishEmission,
                    year,
                    month,
                    region,
                    province,
                    "Fish",
                    "Food"
                )
                updateProvinceCategoryEmission(
                    crustaceansEmission,
                    year,
                    month,
                    region,
                    province,
                    "Crustaceans",
                    "Food"
                )
                updateProvinceCategoryEmission(
                    mollusksEmission,
                    year,
                    month,
                    region,
                    province,
                    "Mollusks",
                    "Food"
                )
                updateMunicipalityCategoryEmission(
                    meatEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Meat",
                    "Food"
                )
                updateMunicipalityCategoryEmission(
                    fishEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Fish",
                    "Food"
                )
                updateMunicipalityCategoryEmission(
                    crustaceansEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Crustaceans",
                    "Food"
                )
                updateMunicipalityCategoryEmission(
                    mollusksEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Mollusks",
                    "Food"
                )
                updateMapCategoryEmission(
                    meatEmission,
                    region,
                    province,
                    municipality,
                    "Meat",
                    "Food"
                )
                updateMapCategoryEmission(
                    fishEmission,
                    region,
                    province,
                    municipality,
                    "Fish",
                    "Food"
                )
                updateMapCategoryEmission(
                    crustaceansEmission,
                    region,
                    province,
                    municipality,
                    "Crustaceans",
                    "Food"
                )
                updateMapCategoryEmission(
                    mollusksEmission,
                    region,
                    province,
                    municipality,
                    "Mollusks",
                    "Food"
                )
                updateRegionCategoryEmission(
                    crustaceansEmission,
                    year,
                    month,
                    region,
                    "Crustaceans",
                    "Food"
                )
                updateRegionCategoryEmission(
                    mollusksEmission,
                    year,
                    month,
                    region,
                    "Mollusks",
                    "Food"
                )
                updateProvinceCategoryEmission(
                    meatEmission,
                    year,
                    month,
                    region,
                    province,
                    "Meat",
                    "Food"
                )
                updateProvinceCategoryEmission(
                    fishEmission,
                    year,
                    month,
                    region,
                    province,
                    "Fish",
                    "Food"
                )
                updateProvinceCategoryEmission(
                    crustaceansEmission,
                    year,
                    month,
                    region,
                    province,
                    "Crustaceans",
                    "Food"
                )
                updateProvinceCategoryEmission(
                    mollusksEmission,
                    year,
                    month,
                    region,
                    province,
                    "Mollusks",
                    "Food"
                )
                updateMunicipalityCategoryEmission(
                    meatEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Meat",
                    "Food"
                )
                updateMunicipalityCategoryEmission(
                    fishEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Fish",
                    "Food"
                )
                updateMunicipalityCategoryEmission(
                    crustaceansEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Crustaceans",
                    "Food"
                )
                updateMunicipalityCategoryEmission(
                    mollusksEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Mollusks",
                    "Food"
                )
                updateMapCategoryEmission(
                    meatEmission,
                    region,
                    province,
                    municipality,
                    "Meat",
                    "Food"
                )
                updateMapCategoryEmission(
                    fishEmission,
                    region,
                    province,
                    municipality,
                    "Fish",
                    "Food"
                )
                updateMapCategoryEmission(
                    crustaceansEmission,
                    region,
                    province,
                    municipality,
                    "Crustaceans",
                    "Food"
                )
                updateMapCategoryEmission(
                    mollusksEmission,
                    region,
                    province,
                    municipality,
                    "Mollusks",
                    "Food"
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun getTotalMonthlyVehicleEmissionForUser(
        user: FirebaseUser,
        year: Int,
        month: Int,
        region: String,
        province: String,
        municipality: String
    ) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("DailyVehicleEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var premiumEmission = 0.0
                var dieselEmission = 0.0
                var unleadedEmission = 0.0

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef =
                        daySnapshot.child(userId) // Get the user's emission for that day
                    val premium =
                        userEmissionRef.child("PremiumEmission").getValue(Double::class.java) ?: 0.0
                    val diesel =
                        userEmissionRef.child("DieselEmission").getValue(Double::class.java) ?: 0.0
                    val unleaded =
                        userEmissionRef.child("UnleadedEmission").getValue(Double::class.java)
                            ?: 0.0

                    premiumEmission += premium
                    dieselEmission += diesel
                    unleadedEmission += unleaded
                }
                // Use the monthlyEmission as needed
                updateRegionCategoryEmission(
                    premiumEmission,
                    year,
                    month,
                    region,
                    "Premium",
                    "Vehicle"
                )
                updateRegionCategoryEmission(
                    unleadedEmission,
                    year,
                    month,
                    region,
                    "Unleaded",
                    "Vehicle"
                )
                updateRegionCategoryEmission(
                    dieselEmission,
                    year,
                    month,
                    region,
                    "Diesel",
                    "Vehicle"
                )
                updateProvinceCategoryEmission(
                    premiumEmission,
                    year,
                    month,
                    region,
                    province,
                    "Premium",
                    "Vehicle"
                )
                updateProvinceCategoryEmission(
                    unleadedEmission,
                    year,
                    month,
                    region,
                    province,
                    "Unleaded",
                    "Vehicle"
                )
                updateProvinceCategoryEmission(
                    dieselEmission,
                    year,
                    month,
                    region,
                    province,
                    "Diesel",
                    "Vehicle"
                )
                updateMunicipalityCategoryEmission(
                    premiumEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Premium",
                    "Vehicle"
                )
                updateMunicipalityCategoryEmission(
                    unleadedEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Unleaded",
                    "Vehicle"
                )
                updateMunicipalityCategoryEmission(
                    dieselEmission,
                    year,
                    month,
                    region,
                    province,
                    municipality,
                    "Diesel",
                    "Vehicle"
                )
                updateMapCategoryEmission(
                    premiumEmission,
                    region,
                    province,
                    municipality,
                    "Premium",
                    "Vehicle"
                )
                updateMapCategoryEmission(
                    unleadedEmission,
                    region,
                    province,
                    municipality,
                    "Unleaded",
                    "Vehicle"
                )
                updateMapCategoryEmission(
                    dieselEmission,
                    region,
                    province,
                    municipality,
                    "Diesel",
                    "Vehicle"
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    private fun updateRegionCategoryEmission(
        totalEmission: Double,
        year: Int,
        month: Int,
        region: String,
        category: String,
        type: String
    ) {
        FirebaseQuery().readRegionCategoryEmission(year, month, region, type, category) {
            //Write emission data to Realtime database
            val database = Firebase.database
            val userLocationEmissionTable =
                database.getReference("LocationEmission/$year/$month/$region/$type")

            //Create a Municipality Total Emission entry using Category
            val newMunicipalityEmissionRef = userLocationEmissionTable.child(category)

            //Set Total Emission data
            newMunicipalityEmissionRef.child("TotalEmission")
                .setValue((totalEmission + it.currentEmission))
        }
    }

    private fun updateProvinceCategoryEmission(
        totalEmission: Double,
        year: Int,
        month: Int,
        region: String,
        province: String,
        category: String,
        type: String
    ) {
        FirebaseQuery().readProvinceCategoryEmission(
            year,
            month,
            region,
            province,
            type,
            category
        ) {
            //Write emission data to Realtime database
            val database = Firebase.database
            val userLocationEmissionTable =
                database.getReference("LocationEmission/$year/$month/$region/$province/$type")

            //Create a Municipality Total Emission entry using Category
            val newMunicipalityEmissionRef = userLocationEmissionTable.child(category)

            //Set Total Emission data
            newMunicipalityEmissionRef.child("TotalEmission")
                .setValue((totalEmission + it.currentEmission))
        }
    }

    private fun updateMunicipalityCategoryEmission(
        totalEmission: Double,
        year: Int,
        month: Int,
        region: String,
        province: String,
        municipality: String,
        category: String,
        type: String
    ) {
        FirebaseQuery().readMunicipalityCategoryEmission(
            year,
            month,
            region,
            province,
            municipality,
            type,
            category
        ) {
            //Write emission data to Realtime database
            val database = Firebase.database
            val userLocationEmissionTable =
                database.getReference("LocationEmission/$year/$month/$region/$province/$municipality/$type")

            //Create a Municipality Total Emission entry using Category
            val newMunicipalityEmissionRef = userLocationEmissionTable.child(category)

            //Set Total Emission data
            newMunicipalityEmissionRef.child("TotalEmission")
                .setValue((totalEmission + it.currentEmission))
        }
    }

    private fun updateMunicipalityEmission(
        totalEmission: Double,
        year: Int,
        month: Int,
        region: String,
        province: String,
        municipality: String,
        houseEmission: Double,
        foodEmission: Double,
        vehicleEmission: Double,
        travelEmission: Double
    ) {
        //Write emission data to Realtime database
        val database = Firebase.database
        val userLocationEmissionTable =
            database.getReference("LocationEmission/$year/$month/$region/$province")

        //Create a Municipality Total Emission entry using unique id
        val newMunicipalityEmissionRef = userLocationEmissionTable.child(municipality)

        //Set Total Emission data
        newMunicipalityEmissionRef.child("TotalEmission")
            .setValue(totalEmission + houseEmission + foodEmission + travelEmission + vehicleEmission)
    }

    private fun updateProvinceEmission(
        totalEmission: Double,
        year: Int,
        month: Int,
        region: String,
        province: String,
        houseEmission: Double,
        foodEmission: Double,
        vehicleEmission: Double,
        travelEmission: Double
    ) {
        //Write emission data to Realtime database
        val database = Firebase.database
        val userLocationEmissionTable =
            database.getReference("LocationEmission/$year/$month/$region")

        //Create a Province Total Emission entry
        val newProvinceEmissionRef = userLocationEmissionTable.child(province)

        //Set Total Emission data
        newProvinceEmissionRef.child("TotalEmission")
            .setValue((totalEmission + houseEmission + foodEmission + travelEmission + vehicleEmission))
    }

    private fun updateRegionEmission(
        totalEmission: Double,
        year: Int,
        month: Int,
        region: String,
        houseEmission: Double,
        foodEmission: Double,
        vehicleEmission: Double,
        travelEmission: Double
    ) {
        //Write emission data to Realtime database
        val database = Firebase.database
        val userLocationEmissionTable = database.getReference("LocationEmission/$year/$month")

        //Create a Region Total Emission entry
        val newRegionEmissionRef = userLocationEmissionTable.child(region)

        //Set Total Emission data
        newRegionEmissionRef.child("TotalEmission")
            .setValue(totalEmission + houseEmission + foodEmission + travelEmission + vehicleEmission)
    }


    private fun updateMapTotalEmission(
        totalEmission: Double,
        region: String,
        province: String,
        municipality: String,
    ) {
        FirebaseQuery().readMapTotalEmission(region, province, municipality) {
            val database = Firebase.database
            val userLocationEmissionTable =
                database.getReference("MapEmission")

            //Create a Municipality Total Emission entry using unique id
            val newTotalEmissionRef = userLocationEmissionTable.child(region)

            //Set Total Emission data
            newTotalEmissionRef.child("TotalEmission").setValue(totalEmission + it.regionEmission)
            newTotalEmissionRef.child("$province/TotalEmission")
                .setValue(totalEmission + it.provinceEmission)
            newTotalEmissionRef.child("$province/$municipality/TotalEmission")
                .setValue(totalEmission + it.municipalityEmission)
        }
    }

    private fun updateUserMonthlyEmission(
        user: FirebaseUser,
        houseEmission: Double,
        foodEmission: Double,
        vehicleEmission: Double,
        travelEmission: Double,
        year: Int,
        month: Int
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userMonthlyEmissionTable = database.getReference("UserMonthlyEmission/$year/$month")

        //Create a new UserMonthlyEmission entry using unique id
        val newUserMonthlyEmissionRef = userMonthlyEmissionTable.child(userId)

        //Set User Monthly Emission data
        newUserMonthlyEmissionRef.child("houseEmission").setValue(houseEmission)
        newUserMonthlyEmissionRef.child("foodEmission").setValue(foodEmission)
        newUserMonthlyEmissionRef.child("vehicleEmission").setValue(vehicleEmission)
        newUserMonthlyEmissionRef.child("travelEmission").setValue(travelEmission)
    }


    private fun updateUserFoodEmission(
        user: FirebaseUser,
        meatEmission: Double,
        fishEmission: Double,
        crustaceansEmission: Double,
        mollusksEmission: Double
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userDailyEmissionTable = database.getReference("DailyFoodEmission/$year/$month/$day")

        //Create a new UserInitialEmission entry using unique id
        val newUserDailyEmissionRef = userDailyEmissionTable.child(userId)

        //Retrieve Food sub-category current daily emission
        FirebaseQuery().readUserDailyFoodEmission(user, year, month, day) {
            //Set New total Emission data
            newUserDailyEmissionRef.child("meatEmission").setValue(meatEmission + it.meatEmission)
            newUserDailyEmissionRef.child("fishEmission").setValue(fishEmission + it.fishEmission)
            newUserDailyEmissionRef.child("crustaceansEmission")
                .setValue(crustaceansEmission + it.crustaceansEmission)
            newUserDailyEmissionRef.child("mollusksEmission")
                .setValue(mollusksEmission + it.mollusksEmission)
        }
    }

    private fun updateUserDailyHome(
        user: FirebaseUser,
        electricity: Double,
        charcoal: Double,
        firewood: Double,
        lpg: Double
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userDailyEmissionTable = database.getReference("DailyHomeEmission/$year/$month/$day")

        //Create a new UserInitialEmission entry using unique id
        val newUserDailyEmissionRef = userDailyEmissionTable.child(userId)
        //Retrieve current user house sub-category emission
        FirebaseQuery().readUserDailyHouseEmission(user, year, month, day) {
            //Set Home sub category Emission data
            newUserDailyEmissionRef.child("electricityEmission")
                .setValue(electricity + it.electricityEmission)
            newUserDailyEmissionRef.child("charcoalEmission")
                .setValue(charcoal + it.charcoalEmission)
            newUserDailyEmissionRef.child("firewoodEmission")
                .setValue(firewood + it.firewoodEmission)
            newUserDailyEmissionRef.child("lpgEmission").setValue(lpg + it.lpgEmission)
        }
    }


    private fun updateUserVehicleOdometer(user: FirebaseUser, newOdometer: String, key: String) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = FirebaseDatabase.getInstance().getReference("UserVehicle/$userId/$key")
        database.child("vehicleOdometer").setValue(newOdometer)
        Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
    }


    private fun updateUserDailyHomeEmissionDatabase(user: FirebaseUser, houseEmission: Double) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userDailyEmissionTable = database.getReference("UserDailyEmission/$year/$month/$day")

        //Create a new UserInitialEmission entry using unique id
        val newUserDailyEmissionRef = userDailyEmissionTable.child(userId)
        //Retrieve current user daily house emission
        FirebaseQuery().readUserDailyEmission(user, year, month, day) {
            //Set new total of house emission data
            newUserDailyEmissionRef.child("houseEmission")
                .setValue(houseEmission + it.houseEmission)
        }
    }

    private fun updateUserDailyFoodEmission(user: FirebaseUser, foodEmission: Double) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userDailyEmissionTable = database.getReference("UserDailyEmission/$year/$month/$day")

        //Create a new UserDailyEmission entry using unique id
        val newUserDailyEmissionRef = userDailyEmissionTable.child(userId)
        //Retrieve current category emission
        FirebaseQuery().readUserDailyEmission(user, year, month, day) {
            //Set UserFood data
            newUserDailyEmissionRef.child("foodEmission").setValue(foodEmission + it.foodEmission)
        }
    }

    private fun updateUserDailyTravelEmissionDatabase(user: FirebaseUser, travelEmission: Double) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userDailyEmissionTable = database.getReference("UserDailyEmission/$year/$month/$day")

        //Create a new UserDailyEmission entry using unique id
        val newUserDailyEmissionRef = userDailyEmissionTable.child(userId)

        //Retrieve current travel emission
        FirebaseQuery().readUserDailyEmission(user, year, month, day) {
            //Set new travel emission data
            newUserDailyEmissionRef.child("travelEmission")
                .setValue(travelEmission + it.travelEmission)
        }
    }

    private fun updateUserVehicleEmissionDatabase(
        user: FirebaseUser,
        vehicleFuelEmission: Double
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userDailyEmissionTable = database.getReference("UserDailyEmission/$year/$month/$day")

        //Create a new UserInitialEmission entry using unique id
        val newUserDailyEmissionRef = userDailyEmissionTable.child(userId)
        //Retrieve current vehicle emission
        FirebaseQuery().readUserDailyEmission(user, year, month, day) {
            //Set new vehicle emission total
            newUserDailyEmissionRef.child("vehicleEmission")
                .setValue(vehicleFuelEmission + it.vehicleEmission)
        }
    }

    private fun computeTotalTransportationEmission(emissionList: ArrayList<Double>): Double {
        var total = 0.0
        emissionList.forEach {
            total += it
        }
        return total
    }

    private fun computeTotalFoodEmission(foodList: ArrayList<Double>): Double {
        var total = 0.0
        foodList.forEach {
            total += it
        }
        return total
    }

    override fun onItemClick(position: Int) {
        if (binding.layoutFood.visibility == View.VISIBLE) {
            binding.txtFoodNumber.setText((position + 1).toString())
        } else {
            binding.txtItemNumber.setText((position + 1).toString())
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

    private val handler = Handler()
    private val random = Random
    private fun startRandomTimer() {
        val delay = getRandomDelay()

        handler.postDelayed({
            val intent = Intent(this, DailyCheckUpResult::class.java)
            startActivity(intent)
            finish()
        }, delay.toLong())
    }

    private fun getRandomDelay(): Int {
        return random.nextInt(5000, 10001)
    }

    private fun validateCarFields(
        newOdometer: String,
        oldOdometer: String
    ): Boolean {
        return when {
            TextUtils.isEmpty(oldOdometer) -> {
                binding.txtOldOdometer.error = "Complete this Field"
                false
            }

            TextUtils.isEmpty(newOdometer) -> {
                binding.txtNewOdometer.error = "Complete this Field"
                false
            }

            newOdometer.toInt() < oldOdometer.toInt() -> {
                binding.txtNewOdometer.error = "New Reading must be higher than the Old Reading"
                false
            }

            newOdometer.toInt() == oldOdometer.toInt() -> {
                binding.txtNewOdometer.error = "New Reading must not equal to Old Reading"
                false
            }

            else -> {
                binding.txtNewOdometer.error = null
                binding.txtOldOdometer.error = null
                true
            }
        }
    }
}




