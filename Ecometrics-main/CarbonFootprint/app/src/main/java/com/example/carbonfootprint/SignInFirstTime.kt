package com.example.carbonfootprint

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carbonfootprint.CalculationFun.ButtonChangingColor
import com.example.carbonfootprint.CalculationFun.EmissionCalculation
import com.example.carbonfootprint.adapter.DataModel
import com.example.carbonfootprint.adapter.TableAdapter
import com.example.carbonfootprint.database.FirebaseQuery
import com.example.carbonfootprint.database.FoodData
import com.example.carbonfootprint.database.VehicleData
import com.example.carbonfootprint.databinding.FirstTimeSignInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import java.util.Calendar
import kotlin.random.Random

data class vehicleInfo(
    val name: String,
    val type: String,
    val transmission: String,
    val size: String,
    val gas: String,
    val odometer: String,
    val efficiency: String
)

class SignInFirstTime : AppCompatActivity(), TableAdapter.OnItemClickListener {
    lateinit var binding: FirstTimeSignInBinding
    private lateinit var auth: FirebaseAuth
    var carCount = 0
    var carIndex = 0
    private lateinit var databaseRef: DatabaseReference

    var carArray:Array<vehicleInfo> = arrayOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FirstTimeSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val user = auth.currentUser
        val userId = user?.uid.toString()

        val spinnerModel = binding.spinnerCarType
        val spinnerTransmission = binding.spinnerTransmissionType
        val spinnerSize = binding.spinnerSizeType
        val spinnerMonth = binding.spinnerMonth
        val spinnerYear = binding.spinnerYear

        FirebaseQuery().readUserProfile(userId) {
            binding.userRegion.text = it.region
            binding.userProvince.text = it.province
            binding.userMunicipality.text = it.municipality
            binding.familyNumber.text = it.houseMember.toString()
            binding.vehicleNumber.text = it.carNumber.toString()
            binding.userStatus.text = it.userType
        }

        var region = binding.userRegion.text.toString()
        var municipality = binding.userMunicipality.text.toString()

        var model = ""
        var transmission = ""
        var size = ""
        var age = 0
        var capacity = 0
        var customEfficiency = false
        var efficiency = ""
        var discount = false
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
                val enteredText = editable.toString()
                var houseMember = binding.familyNumber.text.toString()
                var province = binding.userProvince.text.toString()
                if (enteredText.isEmpty() || enteredText == "0") {
                    binding.txtElectricityBill.error = "Complete this field first"
                    binding.lblElectricityEmission.text = ""
                    return
                }
                binding.txtElectricityBill.error = null
                EmissionCalculation().calculateElectricityBillEmissions(
                    province = province,
                    amount = enteredText.toDouble()
                ) { emission ->
                    var result = emission / houseMember.toDouble()
                    binding.lblElectricityEmission.text = "${result}"
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
                val enteredText = editable.toString()
                var houseMember = binding.familyNumber.text.toString()
                var province = binding.userProvince.text.toString()
                if (enteredText.isEmpty() || enteredText == "0") {
                    binding.txtElectricityAmount.error = "Complete this field first"
                    binding.lblElectricityEmission.text = ""
                    return
                }
                binding.txtElectricityAmount.error = null
                EmissionCalculation().calculateElectricityAmountEmissions(
                    province = province,
                    amount = enteredText.toDouble(),
                ) { emission ->
                    var result = emission / houseMember.toDouble()
                    binding.lblElectricityEmission.text = "${result}"
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
                val enteredText = editable.toString()
                var houseMember = binding.familyNumber.text.toString()
                var province = binding.userProvince.text.toString()
                if (enteredText.isEmpty()) {
                    binding.txtLpgBill.error = "Complete this field first"
                    binding.lblLPGEmission.text = ""
                    return
                }
                binding.lblLPGEmission.error = null
                EmissionCalculation().calculateLPGEmissions(
                    province = province,
                    amount = enteredText.toDouble()
                ) { emission ->
                    var result = emission / houseMember.toDouble()
                    binding.lblLPGEmission.text = "${result}"
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
                val enteredText = editable.toString()
                var houseMember = binding.familyNumber.text.toString()
                var province = binding.userProvince.text.toString()
                if (enteredText.isEmpty()) {
                    binding.txtCharcoalBill.error = "Complete this field first"
                    binding.lblCharcoalEmission.text = ""
                    return
                }
                binding.txtCharcoalBill.error = null
                EmissionCalculation().calculateCharcoalEmissions(
                    province = province,
                    amount = enteredText.toDouble()
                ) { emission ->
                    var result = emission / houseMember.toDouble()
                    binding.lblCharcoalEmission.text = "${result}"
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
                val enteredText = editable.toString()
                var houseMember = binding.familyNumber.text.toString()
                var province = binding.userProvince.text.toString()
                if (enteredText.isEmpty()) {
                    binding.txtFirewoodBill.error = "Complete this field first"
                    binding.lblFirewoodEmission.text = ""
                    return
                }
                binding.txtFirewoodBill.error = null
                EmissionCalculation().calculateFirewoodEmissions(
                    province = province,
                    kg = enteredText.toDouble()
                ) { emission ->
                    var result = emission / houseMember.toDouble()
                    binding.lblFirewoodEmission.text = "${result}"
                }
            }
        })



        binding.btnSubmitHouse.setOnClickListener {
            val electricityBillText = binding.txtElectricityBill.text.toString()
            val electricityAmountText = binding.txtElectricityAmount.text.toString()

            if (electricityBillText.isNotEmpty() || electricityAmountText.isNotEmpty()) {
                if( binding.lblHouseTotalEmission.text.isNotEmpty()){
                    binding.lblHouseTotalEmission.text = "${
                        binding.lblHouseTotalEmission.text.toString().toDouble() + binding.lblElectricityEmission.text.toString().toDouble()
                    }"
                }else{
                    binding.lblHouseTotalEmission.text = binding.lblElectricityEmission.text.toString()
                }
                electricityEmission = binding.lblElectricityEmission.text.toString().toDouble()
            } else {
                Toast.makeText(
                    this,
                    "Please answer the question for Electricity.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener // Exit the function if electricity fields are empty
            }

            if (binding.lblLPGEmission.text.isNotEmpty()) {
                if( binding.lblHouseTotalEmission.text.isNotEmpty()){
                    binding.lblHouseTotalEmission.text = "${
                        binding.lblHouseTotalEmission.text.toString().toDouble() + binding.lblLPGEmission.text.toString().toDouble()
                    }"
                }else{
                    binding.lblHouseTotalEmission.text = binding.lblLPGEmission.text.toString()
                }
                lpgEmission = binding.lblLPGEmission.text.toString().toDouble()
            }

            if (binding.lblCharcoalEmission.text.isNotEmpty()) {
                if( binding.lblHouseTotalEmission.text.isNotEmpty()){
                    binding.lblHouseTotalEmission.text = "${
                        binding.lblHouseTotalEmission.text.toString().toDouble() + binding.lblCharcoalEmission.text.toString().toDouble()
                    }"
                }else{
                    binding.lblHouseTotalEmission.text = binding.lblCharcoalEmission.text.toString()
                }
                charcoalEmission = binding.lblCharcoalEmission.text.toString().toDouble()
            }

            if (binding.lblFirewoodEmission.text.isNotEmpty()) {
                if( binding.lblHouseTotalEmission.text.isNotEmpty()){
                    binding.lblHouseTotalEmission.text = "${
                        binding.lblHouseTotalEmission.text.toString().toDouble() + binding.lblFirewoodEmission.text.toString().toDouble()
                    }"
                }else{
                    binding.lblHouseTotalEmission.text = binding.lblFirewoodEmission.text.toString()
                }
                firewoodEmission = binding.lblFirewoodEmission.text.toString().toDouble()
            }
            binding.layoutHouse.visibility = View.GONE
            binding.layoutFood.visibility = View.VISIBLE
            binding.appIconUserProfile.setImageResource(getResources().getIdentifier(
                "ecometrics_first_time_food",
                "drawable",
                getPackageName()
            ))
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

                        when(food){
                            "Meat" -> icon = getResources().getIdentifier("new_icon_meat", "mipmap", getPackageName())
                            "Fish" -> icon = getResources().getIdentifier("new_icon_fish", "mipmap", getPackageName())
                            "Crustaceans" -> icon = getResources().getIdentifier("new_icon_crustacean", "mipmap", getPackageName())
                            "Mollusks" -> icon = getResources().getIdentifier("new_icon_mollusk", "mipmap", getPackageName())
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
                            this@SignInFirstTime,
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

                        val adapter = TableAdapter(foodList, this@SignInFirstTime)
                        recyclerViewFood.adapter = adapter
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case where nothing is selected
                }
            }
        
        binding.spinnerFoodName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val food = parent?.getItemAtPosition(position).toString()
                binding.txtFoodBill.text.clear()
                if(food == "Choose Food"){
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

                if(input == ""){
                    binding.txtFoodPrice.error = "Complete this field first"
                    binding.lblFoodEmission.text = ""
                    binding.txtFoodBill.text.clear()
                    binding.tblRowQuestionAmountFood.visibility = View.GONE
                    binding.tblRowFoodAmount.visibility = View.GONE
                    return
                }

                binding.txtFoodPrice.error = null

                if(input.toInt() < 0){
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
                val enteredText = editable.toString()
                var province = binding.userProvince.text.toString()
                if (enteredText.isEmpty()) {
                    binding.txtFoodBill.error = "Complete this field first"
                    binding.lblFoodEmission.text = ""
                } else {
                    var category = binding.spinnerFoodCategory.selectedItem.toString()
                    var food = binding.spinnerFoodName.selectedItem.toString()

                    if(food == "Choose Food"){
                        Toast.makeText(this@SignInFirstTime, "Please Choose a Food.", Toast.LENGTH_SHORT)
                            .show()
                        binding.lblFoodEmission.text = ""
                        binding.txtFoodBill.text.clear()
                        return
                    }

                    food = FoodData().getIDData(categoryName = category, foodName = food)!!

                    if(category == "Meat"){
                        category = food
                    }

                    val enteredText = editable.toString()
                    if (enteredText.isEmpty()) {
                        binding.txtFoodBill.error = "Complete this field first"
                        binding.lblFoodEmission.text = ""
                        binding.btnInsertFood.visibility = View.GONE
                    }else{
                        val amount = enteredText.toDouble()
                        binding.txtFoodBill.error = null

                        val price = if(binding.txtFoodPrice.text.isEmpty()){
                            0
                        }else{
                            binding.txtFoodPrice.text.toString().toInt()
                        }

                        EmissionCalculation().calculateFoodEmissions(
                            province = province,
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
            if(binding.spinnerFoodName.selectedItem.toString() == "Choose Food"){
                Toast.makeText(this@SignInFirstTime, "Please Choose a Food.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (binding.spinnerFoodName.selectedItem.toString() == "Choose Food") {
                Toast.makeText(this, "Please select a food category.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(binding.txtFoodBill.text.isEmpty()){
                binding.txtFoodBill.error = "Complete this Field"
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

            when (category){
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
                    Toast.makeText(this@SignInFirstTime, "The List is Empty.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                if (binding.txtFoodNumber.text.toString() != "" && int.toInt() > foodList.size) {
                    Toast.makeText(
                        this@SignInFirstTime,
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
                Toast.makeText(this@SignInFirstTime, "The List is Empty.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (binding.txtFoodNumber.text.toString() == "") {
                Toast.makeText(
                    this@SignInFirstTime,
                    "Select an Item in the List.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val index = binding.txtFoodNumber.text.toString().toInt() - 1

            foodList.removeAt(index)
            val category = foodCategoryList.elementAt(index)
            foodCategoryList.removeAt(index)
            val emission = foodEmissionList.elementAt(index)
            foodEmissionList.removeAt(index)

            when (category){
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
            var carCount = binding.vehicleNumber.text.toString().toInt()
            if (carCount == 0) {
                binding.layoutFood.visibility = View.GONE
                binding.layoutTransportation.visibility = View.VISIBLE
                binding.appIconUserProfile.setImageResource(getResources().getIdentifier(
                    "ecometrics_first_time_transpo",
                    "drawable",
                    getPackageName()
                ))
            } else {
                binding.layoutFood.visibility = View.GONE
                binding.layoutVehicle.visibility = View.VISIBLE
                binding.appIconUserProfile.setImageResource(getResources().getIdentifier(
                    "ecometrics_first_time_vehicle",
                    "drawable",
                    getPackageName()
                ))

                val models = mutableListOf("Choose Model")
                models.addAll(VehicleData().getModelData())
                var adapter = ArrayAdapter(this, R.layout.simple_spinner_item, models)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerModel.adapter = adapter
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val years = mutableListOf("Year")
                years.addAll((1990..currentYear).sortedBy { it.toString() }.map { it.toString() })
                adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerYear.adapter = adapter
            }
        }

        spinnerModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected item change here
                binding.txtOdometer.text.clear()
                binding.lblVehicleEmission.text = ""
                model = parent?.getItemAtPosition(position).toString()

                // Assuming getProvinceData returns a list of province names based on the selected model
                val transmissions = mutableListOf("Choose Transmission")
                transmissions.addAll(VehicleData().getTransmissionData(model))

                val adapter = ArrayAdapter(
                    this@SignInFirstTime,
                    android.R.layout.simple_spinner_item,
                    transmissions
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTransmission.adapter = adapter

                // Remove the default item after it's selected
                // Remove the "Choose Province" item from the dropdown list after it's selected
                if (position == 0) {
                    transmissions.removeAt(0)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        spinnerTransmission.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.txtOdometer.text.clear()
                binding.lblVehicleEmission.text = ""
                // Handle the selected item change here
                transmission = parent?.getItemAtPosition(position).toString()

                // Assuming getTransmissionData returns a list of transmission names based on the selected model
                val sizes = mutableListOf("Choose Size")
                sizes.addAll(VehicleData().getSizeData(model, transmission))

                val adapter = ArrayAdapter(
                    this@SignInFirstTime,
                    android.R.layout.simple_spinner_item,
                    sizes
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerSize.adapter = adapter

                // Remove the default item after it's selected
                // Remove the "Choose Transmission" item from the dropdown list after it's selected
                if (position == 0) {
                    sizes.removeAt(0)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        spinnerSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.txtOdometer.text.clear()
                binding.lblVehicleEmission.text = ""
                // Handle the selected item change here
                size = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.txtOdometer.text.clear()
                binding.lblVehicleEmission.text = ""
                // Handle the selected item change here
                val month = parent?.getItemAtPosition(position).toString()
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val year = spinnerYear.selectedItem.toString()

                if (year == "Year") {
                    return
                }

                if (month != "Month") {
                    age = spinnerMonth.selectedItemId.toInt() + (currentYear - year.toInt()) * 12
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        binding.btnEfficiencyYes.setOnClickListener {
            fadeInAnimation(binding.rowEfficiency);
            customEfficiency = true
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnEfficiencyNo,
                buttonYes = binding.btnEfficiencyYes,
                this
            )
        }

        binding.btnEfficiencyNo.setOnClickListener {
            fadeOutAnimation(binding.rowEfficiency)
            customEfficiency = false
            binding.txtOdometer.text.clear()
            binding.lblVehicleEmission.text = ""
            binding.txtEfficiency.text.clear()
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnEfficiencyNo,
                buttonYes = binding.btnEfficiencyYes,
                this
            )
        }

        binding.txtEfficiency.addTextChangedListener(object : TextWatcher {
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
                if (editable.toString() == "") {
                    Toast.makeText(
                        this@SignInFirstTime,
                        "Please Input the Efficiency",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.txtOdometer.text.clear()
                    binding.lblVehicleEmission.text = ""
                }else{
                    efficiency = editable.toString()
                }
            }
        })

        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.txtOdometer.text.clear()
                binding.lblVehicleEmission.text = ""
                // Handle the selected item change here
                val year = parent?.getItemAtPosition(position).toString()
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)

                if (year != "Year") {
                    age = spinnerMonth.selectedItemId.toInt() + (currentYear - year.toInt()) * 12
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        binding.txtOdometer.addTextChangedListener(object : TextWatcher {
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

                if (validateUserFields(
                        vehicleType = binding.spinnerCarType.selectedItem.toString(),
                        vehicleTransmission = binding.spinnerTransmissionType.toString(),
                        vehicleSize = binding.spinnerSizeType.toString(),
                        vehicleGasType = binding.spinnerGasType.selectedItem.toString(),
                        vehicleCustomEfficiency = customEfficiency,
                        vehicleEfficiency = efficiency,
                        vehicleAgeMonth = binding.spinnerMonth.selectedItem.toString(),
                        vehicleAgeYear = binding.spinnerYear.selectedItem.toString(),
                        vehicleOdometer = editable.toString()
                    )
                ) {
                    if (customEfficiency) {
                        efficiency = "${1/binding.txtEfficiency.text.toString().toDouble()}"
                    } else {
                        efficiency =
                            VehicleData().getEfficiencyData(model, transmission, size)!!.toString()
                    }
                    var vehicleGasType = binding.spinnerGasType.selectedItem.toString()
                    var province = binding.userProvince.text.toString()
                    println(province)
                    capacity = VehicleData().getCapacityData(model, transmission, size)!!
                    val odometer = editable.toString().toDouble() / age
                    when {
                        vehicleGasType == "Diesel" -> {
                            EmissionCalculation().calculateDieselGasEmissions(
                                province = province,
                                km = odometer,
                                efficiency = efficiency.toDouble()
                            ) { result ->
                                var emission = result / capacity
                                binding.lblVehicleEmission.text = "$emission"
                            }
                        }

                        vehicleGasType == "Unleaded" -> {
                            EmissionCalculation().calculateUnleadedGasEmissions(
                                province = province,
                                km = odometer,
                                efficiency = efficiency.toDouble()
                            ) { result ->
                                var emission = result / capacity
                                binding.lblVehicleEmission.text = "$emission"
                            }
                        }

                        vehicleGasType == "Premium" -> {
                            EmissionCalculation().calculatePremiumGasEmissions(
                                province = province,
                                km = odometer,
                                efficiency = efficiency.toDouble()
                            ) { result ->
                                var emission = result / capacity
                                binding.lblVehicleEmission.text = "$emission"
                            }
                        }
                    }
                }else{
                    binding.txtOdometer.text.clear()
                }
            }
        })


        binding.btnSubmitVehicle.setOnClickListener {
            var carNumber = binding.vehicleNumber.text.toString()
            carCount = carNumber.toInt()
            if (carCount > carIndex) {
                if (validateUserFields(
                        vehicleOdometer = binding.txtOdometer.text.toString()
                    )
                ) {
                    val entry = vehicleInfo(
                        binding.txtVehicleName.text.toString(),
                        binding.spinnerCarType.selectedItem.toString(),
                        binding.spinnerTransmissionType.selectedItem.toString(),
                        binding.spinnerSizeType.selectedItem.toString(),
                        binding.spinnerGasType.selectedItem.toString(),
                        binding.txtOdometer.text.toString(),
                        efficiency
                    )

                    val emission = binding.lblVehicleEmission.text.toString()


                    carArray = carArray.plus(entry)

                    if (binding.lblVehicleTotalEmission.text.isEmpty()) {
                        binding.lblVehicleTotalEmission.text = emission
                    } else {
                        binding.lblVehicleTotalEmission.text = "${
                            binding.lblVehicleTotalEmission.text.toString().toDouble() + emission.toDouble()
                        }"
                    }

                    val fuel = entry.gas

                    when (fuel){
                        "Diesel" -> dieselEmission += emission.toDouble()
                        "Unleaded" -> unleadedEmission += emission.toDouble()
                        "Premium" -> premiumEmission += emission.toDouble()
                    }

                    binding.txtVehicleName.text.clear()
                    val adapter =
                        ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOf())
                    binding.spinnerSizeType.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.spinnerTransmissionType.setSelection(0)
                    binding.spinnerCarType.setSelection(0)
                    binding.spinnerGasType.setSelection(0)
                    fadeOutAnimation(binding.rowEfficiency);
                    customEfficiency = false
                    binding.txtEfficiency.text.clear()
                    ButtonChangingColor().setButtonBackgroundColorSelectDefault(
                        buttonNo = binding.btnEfficiencyNo,
                        buttonYes = binding.btnEfficiencyYes,
                        this
                    )
                    binding.spinnerYear.setSelection(0)
                    binding.spinnerMonth.setSelection(0)
                    binding.txtOdometer.text.clear()
                    binding.lblVehicleEmission.text = ""


                    carIndex++
                    binding.lblVehicleNumber.text = "Number " + (carIndex + 1)

                    if (carCount == carIndex) {
                        binding.layoutVehicle.visibility = View.GONE
                        binding.layoutTransportation.visibility = View.VISIBLE
                        binding.appIconUserProfile.setImageResource(getResources().getIdentifier(
                            "ecometrics_first_time_transpo",
                            "drawable",
                            getPackageName()
                        ))
                    }
                }
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
                                icon = getResources().getIdentifier("new_icon_tricycle", "mipmap", getPackageName())
                            }

                            "Jeep" -> {
                                icon = getResources().getIdentifier("new_icon_jeep", "mipmap", getPackageName())
                            }

                            "Electric Jeep" -> {
                                icon = getResources().getIdentifier("new_icon_jeep_electric", "mipmap", getPackageName())
                            }

                            "Airconditioned Electric Jeep" -> {
                                icon = getResources().getIdentifier("new_icon_jeep_aircon", "mipmap", getPackageName())
                            }

                            "Bus" -> {
                                icon = getResources().getIdentifier("new_icon_bus", "mipmap", getPackageName())
                            }

                            "Economy Bus" -> {
                                icon = getResources().getIdentifier("new_icon_bus_economy", "mipmap", getPackageName())
                            }

                            "Airconditioned Bus" -> {
                                icon = getResources().getIdentifier("new_icon_bus_aircon", "mipmap", getPackageName())
                            }

                            "UV Express Van" -> {
                                icon = getResources().getIdentifier("new_icon_uvexpress", "mipmap", getPackageName())
                            }

                            "UV Express Taxi" -> {
                                icon = getResources().getIdentifier("new_icon_taxi", "mipmap", getPackageName())
                            }
                        }

                        binding.tblQuestions.visibility = View.VISIBLE
                        binding.tblTransportationList.visibility = View.GONE
                        binding.iconTravelCategory.setImageResource(icon)
                        binding.txtTranportationBill.text.clear()
                        binding.lblTransportationEmission.text = ""
                        val adapter = TableAdapter(transportationList, this@SignInFirstTime)
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
                val enteredText = editable.toString()
                var userType = binding.userStatus.text.toString()
                if (userType == "Student" || userType == "Senior" || userType == "PWD") {
                    discount = true
                }

                if (enteredText.isEmpty()) {
                    binding.txtTranportationBill.error = "Complete this field first"
                    binding.lblTransportationEmission.text = ""
                } else {
                    val amount = enteredText.toDouble()
                    var province = binding.userProvince.text.toString()
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
        })

        binding.btnInsertTransportation.setOnClickListener {
            if(binding.txtTranportationBill.text.isEmpty()){
                binding.txtTranportationBill.error = "Complete this Field"
                return@setOnClickListener
            }
            if(binding.lblTransportationEmission.text.toString().toDouble() == -1.0){
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
                    Toast.makeText(this@SignInFirstTime, "The List is Empty.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                if (binding.txtItemNumber.text.toString() != "" && int.toInt() > transportationList.size) {
                    Toast.makeText(
                        this@SignInFirstTime,
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
                Toast.makeText(this@SignInFirstTime, "The List is Empty.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (binding.txtItemNumber.text.toString() == "") {
                Toast.makeText(
                    this@SignInFirstTime,
                    "Select an Item in the List.",
                    Toast.LENGTH_SHORT
                ).show()
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

            pUBEEmission = computeTotalTransportationEmission(
                travelPUBEEmissionList
            )

            pUBAEmission = computeTotalTransportationEmission(
                travelPUBAEmissionList
            )

            taxiEmission = computeTotalTransportationEmission(
                travelTaxiEmissionList
            )

            uVExpressEmission = computeTotalTransportationEmission(
                travelUVExpressEmissionList
            )

            if(binding.lblHouseTotalEmission.text.isNotEmpty()){
                houseEmission = binding.lblHouseTotalEmission.text.toString().toDouble()
            }
            user?.let {
                updateUserHouseInitialEmissionDatabase(
                    it,
                    houseEmission, electricityEmission, charcoalEmission, lpgEmission, firewoodEmission
                )
            }

            if(binding.lblFoodTotalEmission.text.isNotEmpty()){
                foodEmission = binding.lblFoodTotalEmission.text.toString().toDouble()
            }
            user?.let {
                updateUserFoodInitialEmissionDatabase(
                    it,
                    foodEmission, meatEmission, fishEmission, crustaceansEmission, mollusksEmission
                )
            }

            for (i in 0 until carCount) {
                user?.let {
                    updateUserVehicleDatabase(
                        it,
                        i,
                        carArray[i].name,
                        carArray[i].type,
                        carArray[i].transmission,
                        carArray[i].size,
                        carArray[i].gas,
                        carArray[i].odometer,
                        carArray[i].efficiency
                    )
                }
            }

            if(binding.lblVehicleTotalEmission.text.isNotEmpty()){
                vehicleEmission = binding.lblVehicleTotalEmission.text.toString().toDouble()
            }

            user?.let {
                updateUserVehicleInitialEmissionDatabase(it, vehicleEmission, premiumEmission, dieselEmission, unleadedEmission)
            }

            if(binding.lblTransportationTotalEmission.text.isNotEmpty()){
                travelEmission = binding.lblTransportationTotalEmission.text.toString().toDouble()
            }

           user?.let {
                updateUserInitialTravelEmissionDatabase(it, travelEmission, tricycleEmission, pUJEmission, ePUJEmission, ePUJAEmission, pUBEmission, pUBAEmission, pUBEEmission, taxiEmission, uVExpressEmission)
                FirebaseQuery().readUserProfile(it.uid){user ->
                    updateMapTotalEmission(
                        (houseEmission + foodEmission + vehicleEmission + travelEmission),
                        user.region,
                        user.province,
                        user.municipality
                    )
                    updateMapTypeEmission(
                        houseEmission,
                        user.region,
                        user.province,
                        user.municipality,
                        "House"
                    )
                    updateMapTypeEmission(
                        foodEmission,
                        user.region,
                        user.province,
                        user.municipality,
                        "Food"
                    )
                    updateMapTypeEmission(
                        vehicleEmission,
                        user.region,
                        user.province,
                        user.municipality,
                        "Vehicle"
                    )
                    updateMapTypeEmission(
                        travelEmission,
                        user.region,
                        user.province,
                        user.municipality,
                        "Travel"
                    )
                }
            }


            binding.layoutTransportation.visibility = View.GONE
            binding.layoutLoading.visibility = View.VISIBLE
            startRandomTimer()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
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


    private fun updateUserInitialTravelEmissionDatabase(
        user: FirebaseUser,
        travelEmission: Double,
        tricycleEmission: Double,
        pUJEmission: Double,
        ePUJEmission: Double,
        ePUJAEmission: Double,
        pUBEmission: Double,
        pUBAEmission: Double,
        pUBEEmission: Double,
        taxiEmission: Double,
        uVExpressEmission: Double
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userInitialEmissionTable = database.getReference("UserInitialEmission")

        //Create a new UserInitialEmission entry using unique id
        val newUserInitialEmissionRef = userInitialEmissionTable.child(userId)

        //Set UserVehicle data
        newUserInitialEmissionRef.child("travelEmission").setValue(travelEmission)
        newUserInitialEmissionRef.child("Travel/Tricycle").setValue(tricycleEmission)
        newUserInitialEmissionRef.child("Travel/Jeep").setValue(pUJEmission)
        newUserInitialEmissionRef.child("Travel/Electric Jeep").setValue(ePUJEmission)
        newUserInitialEmissionRef.child("Travel/Airconditioned Electric Jeep").setValue(ePUJAEmission)
        newUserInitialEmissionRef.child("Travel/Bus").setValue(pUBEmission)
        newUserInitialEmissionRef.child("Travel/Airconditioned Bus").setValue(pUBAEmission)
        newUserInitialEmissionRef.child("Travel/Economy Bus").setValue(pUBEEmission)
        newUserInitialEmissionRef.child("Travel/UV Express Taxi").setValue(taxiEmission)
        newUserInitialEmissionRef.child("Travel/UV Express Van").setValue(uVExpressEmission)
        Toast.makeText(this, "Welcome aboard! Enjoy your experience.", Toast.LENGTH_SHORT).show()
    }

    private fun updateUserHouseInitialEmissionDatabase(
        user: FirebaseUser,
        houseEmission: Double,
        electricityEmission: Double,
        charcoalEmission: Double,
        lpgEmission: Double,
        firewoodEmission: Double
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userInitialEmissionTable = database.getReference("UserInitialEmission")

        //Create a new UserInitialEmission entry using unique id
        val newUserInitialEmissionRef = userInitialEmissionTable.child(userId)

        //Set UserFoodEmission data
        newUserInitialEmissionRef.child("houseEmission").setValue(houseEmission)
        newUserInitialEmissionRef.child("House/electricityEmission").setValue(electricityEmission)
        newUserInitialEmissionRef.child("House/charcoalEmission").setValue(charcoalEmission)
        newUserInitialEmissionRef.child("House/lpgEmission").setValue(lpgEmission)
        newUserInitialEmissionRef.child("House/firewoodEmission").setValue(firewoodEmission)
        Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun updateUserFoodInitialEmissionDatabase(user: FirebaseUser, foodEmission: Double, meatEmission: Double, fishEmission: Double, crustaceansEmission: Double, mollusksEmission: Double) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userInitialEmissionTable = database.getReference("UserInitialEmission")

        //Create a new UserInitialEmission entry using unique id
        val newUserInitialEmissionRef = userInitialEmissionTable.child(userId)

        //Set UserFoodEmission data
        newUserInitialEmissionRef.child("foodEmission").setValue(foodEmission)
        newUserInitialEmissionRef.child("Food/meatEmission").setValue(meatEmission)
        newUserInitialEmissionRef.child("Food/fishEmission").setValue(fishEmission)
        newUserInitialEmissionRef.child("Food/crustaceansEmission").setValue(crustaceansEmission)
        newUserInitialEmissionRef.child("Food/mollusksEmission").setValue(mollusksEmission)
        Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show()
    }




    private fun updateUserVehicleInitialEmissionDatabase(
        user: FirebaseUser,
        vehicleFuelEmission: Double,
        premiumEmission: Double,
        dieselEmission: Double,
        unleadedEmission: Double
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userInitialEmissionTable = database.getReference("UserInitialEmission")

        //Create a new UserInitialEmission entry using unique id
        val newUserInitialEmissionRef = userInitialEmissionTable.child(userId)

        //Set UserVehicle data
        newUserInitialEmissionRef.child("vehicleEmission").setValue(vehicleFuelEmission)
        newUserInitialEmissionRef.child("Vehicle/premiumEmission").setValue(premiumEmission)
        newUserInitialEmissionRef.child("Vehicle/dieselEmission").setValue(dieselEmission)
        newUserInitialEmissionRef.child("Vehicle/unleadedEmission").setValue(unleadedEmission)
    }


    private fun updateUserVehicleDatabase(
        user: FirebaseUser,
        carIndex: Int,
        vehicleName: String,
        carType: String,
        transmissionType: String,
        sizeType: String,
        gasType: String,
        odometer: String,
        efficiency: String
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userVehicleTable = database.getReference("UserVehicle/" + userId)

        //Create a new UserVehicle entry using unique id
        val newUserIVehicleRef = userVehicleTable.child("Vehicle" + (carIndex + 1))

        //Set UserVehicle data
        //newUserIVehicleRef.child("userId").setValue(userId)
        newUserIVehicleRef.child("vehicleName").setValue(vehicleName)
        newUserIVehicleRef.child("vehicleType").setValue(carType)
        newUserIVehicleRef.child("vehicleGasType").setValue(gasType)
        newUserIVehicleRef.child("vehicleTransmission").setValue(transmissionType)
        newUserIVehicleRef.child("vehicleSize").setValue(sizeType)
        newUserIVehicleRef.child("vehicleOdometer").setValue(odometer)
        newUserIVehicleRef.child("vehicleEfficiency").setValue(efficiency)
        Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun updateUserInitialEmissionDatabase(
        user: FirebaseUser,
        houseEmission: Double
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userInitialEmissionTable = database.getReference("UserInitialEmission")

        //Create a new UserInitialEmission entry using unique id
        val newUserInitialEmissionRef = userInitialEmissionTable.child(userId)

        //Set UserVehicle data
        newUserInitialEmissionRef.child("userId").setValue(userId)
        newUserInitialEmissionRef.child("houseEmission").setValue(houseEmission)
        Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun updateUserProfile(
        user: FirebaseUser,
        region: String,
        province: String,
        municipality: String,
        carNumber: String,
        houseMember: String,
        userType: String,
        status: String
    ) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userProfileTable = database.getReference("UserProfile")

        //Create a new UserProfile entry using unique id
        val newUserProfileRef = userProfileTable.child(userId)

        //Set UserProfile data
        newUserProfileRef.child("userId").setValue(userId)
        newUserProfileRef.child("userRegion").setValue(region)
        newUserProfileRef.child("userProvince").setValue(province)
        newUserProfileRef.child("userMunicipality").setValue(municipality)
        newUserProfileRef.child("userCarNumber").setValue(carNumber)
        newUserProfileRef.child("userHouseMember").setValue(houseMember)
        newUserProfileRef.child("userType").setValue(userType)
        newUserProfileRef.child("userStatus").setValue(status)
        Toast.makeText(this, "Created successfully", Toast.LENGTH_SHORT).show()
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

    private val handler = Handler()
    private val random = Random
    private fun startRandomTimer() {
        val delay = getRandomDelay()

        handler.postDelayed({
            val intent = Intent(this,SignInFirstTimeResult::class.java)
            startActivity(intent)
            finish()
        }, delay.toLong())
    }

    private fun getRandomDelay(): Int {
        return random.nextInt(5000, 5001)
    }

    private fun validateUserFields(
        vehicleType: String,
        vehicleTransmission: String,
        vehicleSize: String,
        vehicleGasType: String,
        vehicleCustomEfficiency: Boolean,
        vehicleEfficiency: String,
        vehicleAgeMonth: String,
        vehicleAgeYear: String,
        vehicleOdometer: String,
    ): Boolean {
        return when {
            vehicleType == "Choose Model" -> {
                Toast.makeText(this, "Please Choose a Model", Toast.LENGTH_SHORT).show()
                false
            }

            vehicleTransmission == "Choose Transmission" -> {
                Toast.makeText(this, "Please Choose a Transmission", Toast.LENGTH_SHORT).show()
                false
            }

            vehicleSize == "Choose Size" -> {
                Toast.makeText(this, "Please Choose a Size", Toast.LENGTH_SHORT).show()
                false
            }

            vehicleGasType == "Choose a Fuel" -> {
                Toast.makeText(this, "Please Choose a Fuel", Toast.LENGTH_SHORT).show()
                false
            }

            vehicleCustomEfficiency && vehicleEfficiency == "" -> {
                Toast.makeText(this, "Please Input the Efficiency", Toast.LENGTH_SHORT).show()
                false
            }

            vehicleAgeMonth == "Month" -> {
                Toast.makeText(this, "Please Choose a Month", Toast.LENGTH_SHORT).show()
                false
            }

            vehicleAgeYear == "Year" -> {
                Toast.makeText(this, "Please Choose a Year", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(vehicleOdometer) -> {
                Toast.makeText(this, "Please Input a value in the Odometer", Toast.LENGTH_SHORT)
                    .show()
                false
            }

            else -> {
                true
            }
        }
    }

    private fun validateUserFields(
        vehicleOdometer: String
    ): Boolean {
        return when {
            TextUtils.isEmpty(vehicleOdometer) -> {
                Toast.makeText(this, "Please Input a value in the Odometer", Toast.LENGTH_SHORT)
                    .show()
                false
            }

            else -> {
                true
            }
        }
    }
}




