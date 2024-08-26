package com.example.carbonfootprint

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.carbonfootprint.CalculationFun.ButtonChangingColor
import com.example.carbonfootprint.database.FirebaseQuery
import com.example.carbonfootprint.database.LocationData
import com.example.carbonfootprint.database.VehicleData
import com.example.carbonfootprint.databinding.EditProfileBinding
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


class EditProfile : AppCompatActivity() {
    lateinit var binding: EditProfileBinding
    private lateinit var auth: FirebaseAuth
    var carCount = 0
    var carIndex = 0
    val columns = 4
    private lateinit var database: DatabaseReference

    var carArray = Array(carCount) { Array(columns) { "" } }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val user = auth.currentUser
        val userId = user?.uid.toString()

        val spinnerModel = binding.spinnerCarType
        val spinnerTransmission = binding.spinnerTransmissionType
        val spinnerSize = binding.spinnerSizeType

        val spinnerRegion = binding.spinnerRegion
        val spinnerProvince = binding.spinnerProvince
        val spinnerMunicipality = binding.spinnerMunicipality
        val spinnerUserType = binding.spinnerUserType
        val spinnerVehicle = binding.spinnerCarName
        val txthouseMember = binding.txtHouseMember

        var changeLocation = false
        var changeHouseMember = false
        var changeVehicle = false
        var changeStatus = false

        var model = ""
        var transmission = ""
        var size = ""
        var age = 0
        var capacity = 0
        var customEfficiency = false
        var efficiency = ""

        var region = ""
        var province = ""
        var municipality = ""
        var houseMember = ""
        var carNumber = ""
        var userType = "Choose an Option"

        // Your data list
        val regions = mutableListOf("Choose Region","Region III")
        //regions.addAll(LocationData().getRegionData())

        // Set up the ArrayAdapter and associate it with the Spinner
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, regions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRegion.adapter = adapter



        FirebaseQuery().readUserVehicles(userId) { userVehicles ->
            val vehicles = mutableListOf("Insert a New Vehicle")
            var carArray = FirebaseQuery().populateCarArray(userVehicles)
            println(carArray.indices)
            for (i in carArray.indices) {
            if (carArray[i].size > 3) {
                vehicles.add(carArray[i][0])
            }
        }
            val adapterVehicle = ArrayAdapter(this, R.layout.simple_spinner_item, vehicles)
            spinnerVehicle.adapter = adapterVehicle
        }
        //Add all the name of the cars of the user





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

                if(region != "Choose Region"){
                    binding.txtRegionError.error = null
                }

                // Assuming getProvinceData returns a list of province names based on the selected region
                val provinces = mutableListOf("Choose Province")
                provinces.addAll(LocationData().getProvinceData(region))

                val adapter = ArrayAdapter(
                    this@EditProfile,
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

                if(province != "Choose Province"){
                    binding.txtProvinceError.error = null
                }

                // Assuming getProvinceData returns a list of province names based on the selected region
                val municipalitites = mutableListOf("Choose Municipality")
                municipalitites.addAll(LocationData().getMunicipalityData(region, province))

                val adapter = ArrayAdapter(
                    this@EditProfile,
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

                if(municipality != "Choose Municipality"){
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

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        txthouseMember.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that characters within `charSequence` have been replaced
            }

            override fun afterTextChanged(editable: Editable?) {
                val enteredText = editable.toString()
                
                if(enteredText == "0"){
                    txthouseMember.error = "Family member including yourself"
                    txthouseMember.text.clear()
                    return
                }
                txthouseMember.error = null
            }
        })

        binding.btnLocationYes.setOnClickListener {
            fadeInAnimation(binding.rowLocation);
            fadeInAnimation(binding.rowRegion);
            fadeInAnimation(binding.rowProvince);
            fadeInAnimation(binding.rowMunicipality);
            changeLocation = true
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnLocationNo,
                buttonYes = binding.btnLocationYes,
                this
            )
        }

        binding.btnLocationNo.setOnClickListener {
            binding.rowLocation.visibility = View.GONE
            binding.rowRegion.visibility = View.GONE
            binding.rowProvince.visibility = View.GONE
            binding.rowMunicipality.visibility = View.GONE
            changeLocation = false
            spinnerProvince.setSelection(0)
            spinnerRegion.setSelection(0)
            spinnerMunicipality.setSelection(0)
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnLocationNo,
                buttonYes = binding.btnLocationYes,
                this
            )
        }

        binding.btnHouseMemberYes.setOnClickListener {
            fadeInAnimation(binding.rowHouseMember);
            fadeInAnimation(binding.rowHouse);
            changeHouseMember = true
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnHouseMemberNo,
                buttonYes = binding.btnHouseMemberYes,
                this
            )
        }

        binding.btnHouseMemberNo.setOnClickListener {
            binding.rowHouseMember.visibility = View.GONE
            binding.rowHouse.visibility = View.GONE
            changeHouseMember = false
            txthouseMember.text.clear()
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnHouseMemberNo,
                buttonYes = binding.btnHouseMemberYes,
                this
            )
        }

        binding.btnVehicleYes.setOnClickListener {
            changeVehicle = true
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnVehicleNo,
                buttonYes = binding.btnVehicleYes,
                this
            )
        }

        binding.btnVehicleNo.setOnClickListener {
            changeVehicle = false
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnVehicleNo,
                buttonYes = binding.btnVehicleYes,
                this
            )
        }

        binding.btnStatusYes.setOnClickListener {
            fadeInAnimation(binding.rowStatusType);
            fadeInAnimation(binding.rowStatus);
            changeStatus = true
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnStatusNo,
                buttonYes = binding.btnStatusYes,
                this
            )
        }

        binding.btnStatusNo.setOnClickListener {
            binding.rowStatusType.visibility = View.GONE
            binding.rowStatus.visibility = View.GONE
            changeStatus = false
            spinnerUserType.setSelection(0)
            ButtonChangingColor().setButtonBackgroundColorSelectNo(
                buttonNo = binding.btnStatusNo,
                buttonYes = binding.btnStatusYes,
                this
            )
        }

        binding.btnUpdateUserProfile.setOnClickListener {
            val region = spinnerRegion.selectedItem.toString()
            var province = ""
            var municipality = ""
            if(region.isNotEmpty() && region != "Choose Region"){
                province = spinnerProvince.selectedItem.toString()
            }
            if(province.isNotEmpty() && province != "Choose Province"){
                municipality = spinnerMunicipality.selectedItem.toString()
            }

            val status = spinnerUserType.selectedItem.toString()
            val houseMember = txthouseMember.text.toString()

            if(changeLocation){
                if (validateLocation(region = region, province = province, municipality = municipality, this)) {
                    user?.let {
                        updateUserProfileLocation(it, region, province, municipality)
                    }
                }
            }
            if(changeHouseMember){
                if (validateHouseMember(houseMember = houseMember, this)) {
                    user?.let {
                        updateUserProfileHouseMember(it, houseMember)
                    }
                }
            }

            if(changeStatus){
                if (validateStatus(status = status, this)) {
                    user?.let {
                        updateUserProfileUserType(it, userType)
                    }
                }
            }

            if(changeVehicle){
                binding.layoutUserProfile.visibility = View.GONE
                binding.layoutVehicle.visibility = View.VISIBLE

                val models = mutableListOf("Choose Model")
                models.addAll(VehicleData().getModelData())
                var adapter = ArrayAdapter(this, R.layout.simple_spinner_item, models)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerModel.adapter = adapter
            }else{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        spinnerVehicle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var vehicleID = parent?.getItemAtPosition(position).toString()

                if(vehicleID == "Insert a New Vehicle"){
                    binding.rowDelete.visibility = View.GONE
                    binding.rowDeleteButton.visibility = View.GONE
                    ButtonChangingColor().setButtonBackgroundColorSelectDefault(
                        buttonNo = binding.btnDeleteNo,
                        buttonYes = binding.btnDeleteYes,
                        this@EditProfile
                    )

                    binding.rowVehicle.visibility = View.GONE
                    fadeInAnimation(binding.rowNewVehicle)
                }else{
                    binding.txtVehicleName.text.clear()
                    val adapter =
                        ArrayAdapter<String>(this@EditProfile, android.R.layout.simple_spinner_item, listOf())
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
                        this@EditProfile
                    )
                    binding.txtOdometer.text.clear()

                    binding.rowNewVehicle.visibility = View.GONE
                    fadeInAnimation(binding.rowVehicle)
                    user?.let {
                        FirebaseQuery().readUserVehicle(it, vehicleID){
                            binding.txtVehicleNameExisting.text = it?.vehicleName
                            binding.txtVehicleModelExisting.text = it?.vehicleType
                            binding.txtVehicleTransmisisonExisting.text = it?.vehicleTransmission
                            binding.txtVehicleSizeExisting.text = it?.vehicleSize
                            binding.txtVehicleOdometerExisting.text = it?.vehicleOdometer
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected
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

                if (model != "Choose Model") {
                    binding.txtCarTypeError.error = null
                }

                // Assuming getProvinceData returns a list of province names based on the selected model
                val transmissions = mutableListOf("Choose Transmission")
                transmissions.addAll(VehicleData().getTransmissionData(model))

                val adapter = ArrayAdapter(
                    this@EditProfile,
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

                if (transmission != "Choose Transmission") {
                    binding.txtTransmissionTypeError.error = null
                }

                // Assuming getTransmissionData returns a list of transmission names based on the selected model
                val sizes = mutableListOf("Choose Size")
                sizes.addAll(VehicleData().getSizeData(model, transmission))

                val adapter = ArrayAdapter(
                    this@EditProfile,
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
                        this@EditProfile,
                        "Please Input the Efficiency",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.txtOdometer.text.clear()
                    binding.lblVehicleEmission.text = ""
                }

            }
        })

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
                if (customEfficiency) {
                    efficiency = "${1/binding.txtEfficiency.text.toString().toDouble()}"
                } else {
                    efficiency =
                        VehicleData().getEfficiencyData(model, transmission, size)!!.toString()
                }
            }
        })

        binding.btnDeleteVehicle.setOnClickListener {
            fadeInAnimation(binding.rowDeleteButton);
            fadeInAnimation(binding.rowDelete);
        }

        binding.btnDeleteYes.setOnClickListener {
            ButtonChangingColor().setButtonBackgroundColorSelectYes(
                buttonNo = binding.btnDeleteNo,
                buttonYes = binding.btnDeleteYes,
                this
            )

            user?.let{
                deleteUserVehicle(it, spinnerVehicle.selectedItem.toString())
            }

            FirebaseQuery().readUserVehicles(userId) { userVehicles ->
                val vehicles = mutableListOf("Insert a New Vehicle")
                var carArray = FirebaseQuery().populateCarArray(userVehicles)
                for (i in carArray.indices) {
                    if (carArray[i].size > 3) {
                        vehicles.add(carArray[i][0])
                    }
                }
                val adapterVehicle = ArrayAdapter(this, R.layout.simple_spinner_item, vehicles)
                spinnerVehicle.adapter = adapterVehicle
            }

            fadeOutAnimation(binding.rowDeleteButton);
            fadeOutAnimation(binding.rowDelete);
            spinnerVehicle.setSelection(0)

            ButtonChangingColor().setButtonBackgroundColorSelectDefault(
                buttonNo = binding.btnDeleteNo,
                buttonYes = binding.btnDeleteYes,
                this
            )
        }

        binding.btnDeleteNo.setOnClickListener {
            binding.rowDelete.visibility = View.GONE
            binding.rowDeleteButton.visibility = View.GONE
            ButtonChangingColor().setButtonBackgroundColorSelectDefault(
                buttonNo = binding.btnDeleteNo,
                buttonYes = binding.btnDeleteYes,
                this
            )
        }

        binding.btnUpdateVehicle.setOnClickListener {
            var fuel = binding.spinnerGasTypeExisting.selectedItem.toString()
            if(validateFuel(fuel = fuel, this)){
                user?.let{
                    updateUserVehicleFuel(it, fuel, spinnerVehicle.selectedItem.toString())
                }

                binding.spinnerGasTypeExisting.setSelection(0)
            }
            binding.txtVehicleName.text.clear()
            binding.spinnerCarType.setSelection(0)
            binding.spinnerGasType.setSelection(0)
            spinnerVehicle.setSelection(0)
            binding.rowDelete.visibility = View.GONE
            binding.rowDeleteButton.visibility = View.GONE
        }
        binding.btnSubmitVehicle.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnInsertVehicle.setOnClickListener {
            if (validateVehicle(
                    vehicleType = binding.spinnerCarType.selectedItem.toString(),
                    vehicleTransmission = binding.spinnerTransmissionType.selectedItem.toString(),
                    vehicleSize = binding.spinnerSizeType.selectedItem.toString(),
                    vehicleGasType = binding.spinnerGasType.selectedItem.toString(),
                    vehicleCustomEfficiency = customEfficiency,
                    vehicleEfficiency = efficiency,
                    vehicleOdometer = binding.txtOdometer.text.toString()
                )
            ) {
                Toast.makeText(
                    this@EditProfile,
                    "Success",
                    Toast.LENGTH_SHORT
                ).show()

                val name = binding.txtVehicleName.text.toString()
                val model = binding.spinnerCarType.selectedItem.toString()
                val transmission = binding.spinnerTransmissionType.selectedItem.toString()
                val size = binding.spinnerSizeType.selectedItem.toString()
                val fuel = binding.spinnerGasType.selectedItem.toString()
                val odometer= binding.txtOdometer.text.toString()
                val efficiency = efficiency
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
                binding.txtOdometer.text.clear()

                user?.let{
                    updateUserVehicleIndex(it, name, model, transmission, size, fuel, odometer, efficiency)
                }

               FirebaseQuery().readUserVehicles(userId) { userVehicles ->
                    val vehicles = mutableListOf("Insert a New Vehicle")
                    var carArray = FirebaseQuery().populateCarArray(userVehicles)
                    for (i in carArray.indices) {
                        if (carArray[i].size > 3) {
                            vehicles.add(carArray[i][0])
                        }
                    }
                    val adapterVehicle = ArrayAdapter(this, R.layout.simple_spinner_item, vehicles)
                    spinnerVehicle.adapter = adapterVehicle
                    adapterVehicle.notifyDataSetChanged()
                }
            }
        }
    }

    //Function to count the vehicle number
    fun countUserVehicles(userId: FirebaseUser) {
        val database = Firebase.database
        val ref = database.getReference("UserVehicle").child(userId.uid)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val vehicleCount = dataSnapshot.childrenCount.toInt()
                updateUserProfileCarNumber(userId, vehicleCount)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors while fetching the vehicle count
            }
        })
    }

    private fun deleteUserVehicle(user: FirebaseUser, key: String) {
        val userId = user.uid
        val vehicleId = key

        val database = Firebase.database
        val userVehicleTable = database.getReference("UserVehicle/$userId")

        // Create a reference to the specific vehicle data to be deleted
        val vehicleRef = userVehicleTable.child(vehicleId)

        // Remove the specific vehicle data
        vehicleRef.removeValue().addOnSuccessListener {
            Toast.makeText(this,"Deleted successfully", Toast.LENGTH_SHORT).show()
            countUserVehicles(user)
        }.addOnFailureListener {
            // Handle any errors that occurred during the deletion process
        }
    }

    private fun updateUserVehicleIndex(user: FirebaseUser, name: String, model: String, transmission: String, size: String, fuel: String, odometer: String, efficiency: String) {
        val userId = user.uid
        val database = Firebase.database
        val userVehicleTable = database.getReference("UserVehicle/$userId")

        val lastIndexQuery = userVehicleTable.orderByKey().limitToLast(1)
        lastIndexQuery.get().addOnSuccessListener { dataSnapshot ->
            var newIndex = 0
            if (dataSnapshot.exists()) {
                for (data in dataSnapshot.children) {
                    // Extract the last index and increment it by 1
                    newIndex = data.key!!.replace("Vehicle", "").toInt() + 1
                }
            }
            val newUserVehicleRef = userVehicleTable.child("Vehicle$newIndex")
            newUserVehicleRef.child("vehicleName").setValue(name)
            newUserVehicleRef.child("vehicleType").setValue(model)
            newUserVehicleRef.child("vehicleGasType").setValue(fuel)
            newUserVehicleRef.child("vehicleTransmission").setValue(transmission)
            newUserVehicleRef.child("vehicleSize").setValue(size)
            newUserVehicleRef.child("vehicleOdometer").setValue(odometer)
            newUserVehicleRef.child("vehicleEfficiency").setValue(efficiency)
            Toast.makeText(this,"Inserted successfully", Toast.LENGTH_SHORT).show()
            countUserVehicles(user)
        }
    }

    private fun updateUserProfileCarNumber(user: FirebaseUser, newIndex: Int) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userProfileTable = database.getReference("UserProfile")

        //Create a new UserProfile entry using unique id
        val newUserProfileRef = userProfileTable.child(userId)

        newUserProfileRef.child("userCarNumber").setValue(newIndex)
    }


    private fun updateUserVehicleFuel(user: FirebaseUser, fuel: String, key: String) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = FirebaseDatabase.getInstance().getReference("UserVehicle/$userId/$key")
        database.child("vehicleGasType").setValue(fuel)
        Toast.makeText(this,"Updated successfully", Toast.LENGTH_SHORT).show()
    }

    fun validateLocation(region: String,province: String,municipality: String, context: Context):Boolean{
        return when{
            region == "Choose Region" -> {
                Toast.makeText(context,"Select a region", Toast.LENGTH_SHORT).show()
                false
            }

            province == "Choose Province" -> {
                Toast.makeText(context,"Select a province", Toast.LENGTH_SHORT).show()
                false
            }

            municipality == "Choose Municipality" -> {
                Toast.makeText(context,"Select a municipality", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    fun validateHouseMember(houseMember: String, context: Context):Boolean{
        return when{
            houseMember.isEmpty() -> {
                Toast.makeText(context,"Complete this field", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    fun validateStatus(status: String, context: Context):Boolean{
        return when{
            status == "Choose an Option" -> {
                Toast.makeText(context,"Select a characteristic", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    fun validateVehicle(name: String, type: String, fuel: String, context: Context):Boolean{
        return when {
            name.isEmpty() -> {
                Toast.makeText(context,"Complete thi Field", Toast.LENGTH_SHORT).show()
                false
            }

            type == "Choose a Type of Vehicle" -> {
                Toast.makeText(context,"Select a Vehicle Type", Toast.LENGTH_SHORT).show()
                false
            }

            fuel == "Choose a Fuel" -> {
                Toast.makeText(context,"Select a Fuel Type", Toast.LENGTH_SHORT).show()
                false
            }

            else -> {
                true
            }
        }
    }

    private fun validateVehicle(
        vehicleType: String,
        vehicleTransmission: String,
        vehicleSize: String,
        vehicleGasType: String,
        vehicleCustomEfficiency: Boolean,
        vehicleEfficiency: String,
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

            (vehicleCustomEfficiency && vehicleEfficiency == "") -> {
                Toast.makeText(this, "Please Input the Efficiency", Toast.LENGTH_SHORT).show()
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

    fun validateFuel(fuel: String, context: Context):Boolean{
        return when{
            fuel == "Choose a Fuel" -> {
                Toast.makeText(context,"Select a characteristic", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun updateUserProfileUserType(user: FirebaseUser, userType: String) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userProfileTable = database.getReference("UserProfile")

        //Create a new UserProfile entry using unique id
        val newUserProfileRef = userProfileTable.child(userId)

        newUserProfileRef.child("userType").setValue(userType)
        Toast.makeText(this,"Updated successfully", Toast.LENGTH_SHORT).show()
    }

    private fun updateUserProfileHouseMember(user: FirebaseUser, houseMember: String) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userProfileTable = database.getReference("UserProfile")

        //Create a new UserProfile entry using unique id
        val newUserProfileRef = userProfileTable.child(userId)

        newUserProfileRef.child("userHouseMember").setValue(houseMember)
        Toast.makeText(this,"Updated successfully", Toast.LENGTH_SHORT).show()
    }

    private fun updateUserProfileLocation(user: FirebaseUser, region: String, province: String, municipality: String) {
        val userId = user.uid
        //Write user data to Realtime database
        val database = Firebase.database
        val userProfileTable = database.getReference("UserProfile")

        //Create a new UserProfile entry using unique id
        val newUserProfileRef = userProfileTable.child(userId)

        //Set UserProfile data
        newUserProfileRef.child("userRegion").setValue(region)
        newUserProfileRef.child("userProvince").setValue(province)
        newUserProfileRef.child("userMunicipality").setValue(municipality)
        Toast.makeText(this,"Updated successfully", Toast.LENGTH_SHORT).show()
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
}




