package com.example.carbonfootprint

import android.R
import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.core.view.isNotEmpty
import com.example.carbonfootprint.CalculationFun.EmissionCalculation
import com.example.carbonfootprint.database.LocationData
import com.example.carbonfootprint.databinding.FirstTimeSignInBinding
import com.example.carbonfootprint.databinding.ProfilingLayoutBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlin.random.Random

data class UserProfile(
    val region: String,
    val province: String,
    val municipality: String,
    val houseMember: String,
    val carNumber: String,
    val userType: String,
    val status: String
)

class Profiling : AppCompatActivity(){
    lateinit var binding: ProfilingLayoutBinding
    private lateinit var auth: FirebaseAuth
    var carCount = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfilingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val user = auth.currentUser

        var emissionHighLevel = EmissionCalculation.AverageEmission(
            false, false, false, false
        )

        val spinnerRegion = binding.spinnerRegion
        val spinnerProvince = binding.spinnerProvince
        val spinnerMunicipality = binding.spinnerMunicipality

        val spinnerUserType = binding.spinnerUserType
        val txthouseMember = binding.txtHouseMember
        val txtcarNumber = binding.txtCarNumber

        var region = ""
        var province = ""
        var municipality = ""

        var houseMember = ""
        var carNumber = ""
        var userType = "Choose an Option"

        val regions = mutableListOf("Choose Region", "Region III")
        //regions.addAll(LocationData().getRegionData())

        // Set up the ArrayAdapter and associate it with the Spinner
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, regions)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRegion.adapter = adapter

        // Add an OnItemSelectedListener to the Spinner
        spinnerRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected item change here
                region = parent?.getItemAtPosition(position).toString()

                if (region != "Choose Region") {
                    binding.txtRegionError.error = null
                }

                // Assuming getProvinceData returns a list of province names based on the selected region
                val provinces = mutableListOf("Choose Province")
                provinces.addAll(LocationData().getProvinceData(region))

                val adapter = ArrayAdapter(
                    this@Profiling,
                    android.R.layout.simple_spinner_item,
                    provinces
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerProvince.adapter = adapter

                // Remove the default item after it's selected
                // Remove the "Choose Province" item from the dropdown list after it's selected
                if (position == 0) {
                    provinces.removeAt(0)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        spinnerProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected item change here
                province = parent?.getItemAtPosition(position).toString()

                if (province != "Choose Province") {
                    binding.txtProvinceError.error = null
                }

                // Assuming getProvinceData returns a list of province names based on the selected region
                val municipalitites = mutableListOf("Choose Municipality")
                municipalitites.addAll(LocationData().getMunicipalityData(region, province))

                val adapter = ArrayAdapter(
                    this@Profiling,
                    android.R.layout.simple_spinner_item,
                    municipalitites
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerMunicipality.adapter = adapter

                // Remove the default item after it's selected
                // Remove the "Choose Province" item from the dropdown list after it's selected
                if (position == 0) {
                    municipalitites.removeAt(0)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        spinnerMunicipality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected item change here
                municipality = parent?.getItemAtPosition(position).toString()

                if (municipality != "Choose Municipality") {
                    binding.txtMunicipalityError.error = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        spinnerUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected item change here
                userType = parent?.getItemAtPosition(position).toString()

                if (userType != "Choose an Option") {
                    binding.txtUserTypeError.error = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
            }
        }

        txthouseMember.addTextChangedListener(object : TextWatcher {
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

                if (enteredText == "0") {
                    txthouseMember.error = "Family member including yourself"
                    txthouseMember.text.clear()
                    return
                }
                txthouseMember.error = null
            }
        })

        binding.btnSubmitUserProfile.setOnClickListener {
            region = spinnerRegion.selectedItem.toString()
            if (spinnerProvince.isNotEmpty()) {
                province = spinnerProvince.selectedItem.toString()
            }
            if (spinnerMunicipality.isNotEmpty()) {
                municipality = spinnerMunicipality.selectedItem.toString()
            }
            houseMember = txthouseMember.text.toString()
            carNumber = txtcarNumber.text.toString()
            userType = spinnerUserType.selectedItem.toString()

            val userData = UserProfile(
                region,
                province,
                municipality,
                houseMember,
                carNumber,
                userType,
                "Active"
            )


            if (validateUserFields(userProfile = userData)) {
                carCount = carNumber.toInt()

                user?.let {
                    updateUserProfile(
                        it,
                        userData.region,
                        userData.province,
                        userData.municipality,
                        userData.carNumber,
                        userData.houseMember,
                        userData.userType,
                        userData.status
                    )
                }
                startActivity(Intent(this, SignInFirstTime::class.java))
                finish()
            }
        }
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

    private val handler = Handler()
    private val random = Random

    private fun getRandomDelay(): Int {
        return random.nextInt(5000, 5001)
    }


    private fun validateUserFields(userProfile: UserProfile): Boolean {
        return when {
            userProfile.region == "Choose Region" -> {
                binding.txtRegionError.error = "Select a Region"
                false
            }

            userProfile.province == "Choose Province" -> {
                binding.txtProvinceError.error = "Select a Province"
                false
            }

            userProfile.municipality == "Choose Municipality" -> {
                binding.txtMunicipalityError.error = "Select a Municipality"
                false
            }

            TextUtils.isEmpty(userProfile.houseMember) -> {
                binding.txtHouseMember.error = "Complete this Field"
                false
            }

            TextUtils.isEmpty(userProfile.carNumber) -> {
                binding.txtCarNumber.error = "Complete this Field"
                false
            }

            userProfile.userType == "Choose an Option" -> {
                binding.txtUserTypeError.error = "Select an Option";
                false
            }

            else -> {
                binding.txtRegionError.error = null
                binding.txtProvinceError.error = null
                binding.txtMunicipalityError.error = null
                binding.txtUserTypeError.error = null
                binding.txtCarNumber.error = null
                binding.txtHouseMember.error = null
                true
            }
        }
    }
}




