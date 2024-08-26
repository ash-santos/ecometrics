package com.example.carbonfootprint.database

import androidx.appcompat.app.AppCompatActivity
import com.example.carbonfootprint.RegistrationActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import org.osmdroid.util.GeoPoint


class FirebaseQuery : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    data class ProfileChecker(
        var exist: Boolean,
    )
    data class UserInfo(
        var email: String,
        var firstName: String,
        var lastName: String
    )
    data class UserProfile(
        var region: String,
        var province: String,
        var municipality: String,
        var houseMember: Int,
        var carNumber: Int,
        var userType: String
    )

    data class UserVehicle(
        var vehicleKey: String,
        var vehicleName: String,
        var vehicleModel: String,
        var fuelType: String,
        val vehicleEfficiency: String,
        val vehicleOdometer: String,
        val vehicleTransmission: String,
        val vehicleSize: String
    )

    data class OffsetActivity(
        var offsetKey : String,
        var title: String,
        var description: String,
        var link: String,
        var status: String,
    )

    data class ReadUserVehicle(
        var vehicleName: String,
        var vehicleType: String,
        var vehicleTransmission: String,
        var vehicleSize: String,
        var vehicleOdometer: String
    )

    data class UserMonthlyEmission(
        var houseEmission: Double,
        var foodEmission: Double,
        var vehicleEmission: Double,
        var travelEmission: Double
    )

    data class UserDailyEmission(
        var houseEmission: Double,
        var foodEmission: Double,
        var vehicleEmission: Double,
        var travelEmission: Double
    )

    data class DailyEmission(
        var day: Int,
        var houseEmission: Double,
        var foodEmission: Double,
        var vehicleEmission: Double,
        var travelEmission: Double
    )

    data class UserInitialEmission(
        var houseEmission: Double,
        var electricityEmission: Double,
        var charcoalEmission: Double,
        var lpgEmission: Double,
        var firewoodEmission: Double,
        var foodEmission: Double,
        var meatEmission: Double,
        var fishEmission: Double,
        var crustaceansEmission: Double,
        var mollusksEmission: Double,
        var travelEmission: Double,
        var tricycleEmission : Double,
        var pujEmission : Double,
        var epujEmission : Double,
        var epujaEmission : Double,
        var pubEmission : Double,
        var pubaEmission : Double,
        var pubeEmission : Double,
        var taxiEmission : Double,
        var uvexpressEmission : Double,
        var vehicleEmission: Double,
        var premiumEmission: Double,
        var dieselEmission: Double,
        var unleadedEmission: Double
    )

    data class ProvinceEmission(
        var totalEmission: Double,
        var electricityEmission: Double,
        var charcoalEmission: Double,
        var lpgEmission: Double,
        var firewoodEmission: Double,
        var meatEmission: Double,
        var fishEmission: Double,
        var crustaceansEmission: Double,
        var mollusksEmission: Double,
        var tricycleEmission : Double,
        var pujEmission : Double,
        var epujEmission : Double,
        var epujaEmission : Double,
        var pubEmission : Double,
        var pubaEmission : Double,
        var pubeEmission : Double,
        var taxiEmission : Double,
        var uvexpressEmission : Double,
        var premiumEmission: Double,
        var dieselEmission: Double,
        var unleadedEmission: Double,
        var houseEmission: Double,
        var foodEmission: Double,
        var travelEmission: Double,
        var vehicleEmission: Double
    )

    data class MunicipalityEmission(
        var totalEmission: Double,
        var electricityEmission: Double,
        var charcoalEmission: Double,
        var lpgEmission: Double,
        var firewoodEmission: Double,
        var meatEmission: Double,
        var fishEmission: Double,
        var crustaceansEmission: Double,
        var mollusksEmission: Double,
        var tricycleEmission : Double,
        var pujEmission : Double,
        var epujEmission : Double,
        var epujaEmission : Double,
        var pubEmission : Double,
        var pubaEmission : Double,
        var pubeEmission : Double,
        var taxiEmission : Double,
        var uvexpressEmission : Double,
        var premiumEmission: Double,
        var dieselEmission: Double,
        var unleadedEmission: Double,
        var houseEmission: Double,
        var foodEmission: Double,
        var travelEmission: Double,
        var vehicleEmission: Double
    )
    data class UserHouseEmission(
        var electricityEmission: Double,
        var charcoalEmission: Double,
        var lpgEmission: Double,
        var firewoodEmission: Double
    )

    data class UserVehicleEmission(
        var premiumEmission: Double,
        var unleadedEmission: Double,
        var dieselEmission: Double
    )
    data class UserFoodEmission(
        var meatEmission: Double,
        var fishEmission: Double,
        var crustaceansEmission: Double,
        var mollusksEmission: Double
    )

    data class UserTravelEmission(
        var tricycleEmission :Double,
        var jeepEmission :Double,
        var electricJeepEmission :Double,
        var airconditionedElectricJeepEmission :Double,
        var busEmission :Double,
        var economyBusEmission :Double,
        var airconditionedBusEmission :Double,
        var uvVanEmission :Double,
        var uvTaxiEmission :Double
    )

    data class LocationEmission(
        var regionEmission: Double,
        var provinceEmission: Double,
        var municipalityEmission: Double
        /*var municipalityElectricityEmission: Double,
        var municipalityCharcoalEmission: Double,
        var municipalityLPGEmission: Double,
        var municipalityFirewoodEmission: Double*/
    )

    data class CategoryEmission(
        var currentEmission: Double,
    )
    data class MapCategoryEmission(
        var regionCategoryEmission: Double,
        var provinceCategoryEmission: Double,
        var municipalityCategoryEmission: Double
    )
    data class MapTypeEmission(
        var regionTypeEmission: Double,
        var provinceTypeEmission: Double,
        var municipalityTypeEmission: Double
    )

    data class MapTotalEmission(
        var regionEmission: Double,
        var provinceEmission: Double,
        var municipalityEmission: Double
    )

    data class MapMunicipalityTotalEmission(
        var abucayEmission : Double,
        var bagacEmission : Double,
        var balangaEmission : Double,
        var dinalupihanEmission : Double,
        var hermosaEmission : Double,
        var limayEmission : Double,
        var marivelesEmission : Double,
        var morongEmission : Double,
        var oraniEmission : Double,
        var orionEmission : Double,
        var pilarEmission : Double,
        var samalEmission : Double
    )

    data class DailyFoodEmission(
        var day: Int,
        var meatEmission: Double,
        var fishEmission: Double,
        var crustaceansEmission: Double,
        var mollusksEmission: Double
    )

    data class MonthlyFoodEmission(
        var month: Int,
        var meatEmission: Double,
        var fishEmission: Double,
        var crustaceansEmission: Double,
        var mollusksEmission: Double
    )

    data class DailyHouseEmission(
        var day: Int,
        var electricityEmission: Double,
        var charcoalEmission: Double,
        var firewoodEmission: Double,
        var lpgEmission: Double
    )

    data class MonthlyHouseEmission(
        var month: Int,
        var electricityEmission: Double,
        var charcoalEmission: Double,
        var firewoodEmission: Double,
        var lpgEmission: Double
    )

    data class DailyVehicleEmission(
        var day: Int,
        var premiumEmission: Double,
        var dieselEmission: Double,
        var unleadedEmission: Double
    )
    data class MonthlyVehicleEmission(
        var month: Int,
        var premiumEmission: Double,
        var dieselEmission: Double,
        var unleadedEmission: Double
    )

    data class DailyTravelEmission(
        var day: Int,
        var tricycleEmission :Double,
        var jeepEmission :Double,
        var electricJeepEmission :Double,
        var airconditionedElectricJeepEmission :Double,
        var busEmission :Double,
        var economyBusEmission :Double,
        var airconditionedBusEmission :Double,
        var uvVanEmission :Double,
        var uvTaxiEmission :Double
    )

    data class MonthlyTravelEmission(
        var month: Int,
        var tricycleEmission :Double,
        var jeepEmission :Double,
        var electricJeepEmission :Double,
        var airconditionedElectricJeepEmission :Double,
        var busEmission :Double,
        var economyBusEmission :Double,
        var airconditionedBusEmission :Double,
        var uvVanEmission :Double,
        var uvTaxiEmission :Double
    )

    data class MonthlyEmission(
        var month: Int,
        var houseEmission: Double,
        var foodEmission: Double,
        var vehicleEmission: Double,
        var travelEmission: Double
    )

    fun readUserTravelYearlyEmissions(user: FirebaseUser, year: Int, callback: (UserTravelEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")
        var tricycleEmission = 0.0
        var jeepEmission = 0.0
        var electricJeepEmission = 0.0
        var airconditionedElectricJeepEmission = 0.0
        var busEmission = 0.0
        var economyBusEmission = 0.0
        var airconditionedBusEmission = 0.0
        var uvVanEmission = 0.0
        var uvTaxiEmission = 0.0
        var monthsProcessed = 0


        for (month in 1..12) {
            val dayRef = ref.child(month.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val tricycle = dataSnapshot.child("Travel/Tricycle").getValue(Double::class.java) ?: 0.0
                    val jeep = dataSnapshot.child("Travel/Jeep").getValue(Double::class.java) ?: 0.0
                    val electricJeep = dataSnapshot.child("Travel/Electric Jeep").getValue(Double::class.java) ?: 0.0
                    val airconditionedElectricJeep = dataSnapshot.child("Travel/Airconditioned Electric Jeep").getValue(Double::class.java) ?: 0.0
                    val bus = dataSnapshot.child("Travel/Bus").getValue(Double::class.java) ?: 0.0
                    val economyBus = dataSnapshot.child("Travel/Economy Bus").getValue(Double::class.java) ?: 0.0
                    val airconditionedBus = dataSnapshot.child("Travel/Airconditioned Bus").getValue(Double::class.java) ?: 0.0
                    val uvVan = dataSnapshot.child("Travel/UV Express Van").getValue(Double::class.java) ?: 0.0
                    val uvTaxi = dataSnapshot.child("Travel/UV Express Taxi").getValue(Double::class.java) ?: 0.0
                    tricycleEmission += tricycle
                    jeepEmission += jeep
                    electricJeepEmission += electricJeep
                    airconditionedElectricJeepEmission += airconditionedElectricJeep
                    busEmission += bus
                    economyBusEmission += economyBus
                    airconditionedBusEmission += airconditionedBus
                    uvVanEmission += uvVan
                    uvTaxiEmission += uvTaxi
                    monthsProcessed++

                    if (monthsProcessed == 12) {
                        // All months processed, invoke the callback with the final emission data
                        val emissionData = UserTravelEmission(
                            tricycleEmission, jeepEmission, electricJeepEmission, airconditionedElectricJeepEmission, busEmission, economyBusEmission, airconditionedBusEmission, uvVanEmission, uvTaxiEmission
                        )
                        callback.invoke(emissionData)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    fun readUserVehicleYearlyEmissions(user: FirebaseUser, year: Int, callback: (UserVehicleEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")
        var premiumEmission = 0.0
        var unleadedEmission = 0.0
        var dieselEmission = 0.0
        var monthsProcessed = 0

        for (month in 1..12) {
            val dayRef = ref.child(month.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val premium = dataSnapshot.child("Vehicle/premiumEmission").getValue(Double::class.java) ?: 0.0
                    val unleaded = dataSnapshot.child("Vehicle/unleadedEmission").getValue(Double::class.java) ?: 0.0
                    val diesel = dataSnapshot.child("Vehicle/dieselEmission").getValue(Double::class.java) ?: 0.0

                    premiumEmission += premium
                    unleadedEmission +=unleaded
                    dieselEmission += diesel
                    monthsProcessed++

                    if (monthsProcessed == 12) {
                        // All months processed, invoke the callback with the final emission data
                        val emissionData = UserVehicleEmission(
                            premiumEmission,
                            unleadedEmission,
                            dieselEmission
                        )
                        callback.invoke(emissionData)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    fun readUserHouseYearlyEmissions(user: FirebaseUser, year: Int, callback: (UserHouseEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")
        var electricityEmission = 0.0
        var charcoalEmission = 0.0
        var firewoodEmission = 0.0
        var lpgEmission = 0.0
        var monthsProcessed = 0

        for (month in 1..12) {
            val dayRef = ref.child(month.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val electricity = dataSnapshot.child("House/electricityEmission").getValue(Double::class.java) ?: 0.0
                    val charcoal = dataSnapshot.child("House/charcoalEmission").getValue(Double::class.java) ?: 0.0
                    val firewood = dataSnapshot.child("House/firewoodEmission").getValue(Double::class.java) ?: 0.0
                    val lpg = dataSnapshot.child("House/lpgEmission").getValue(Double::class.java) ?: 0.0

                    electricityEmission += electricity
                    charcoalEmission += charcoal
                    firewoodEmission += firewood
                    lpgEmission += lpg
                    monthsProcessed++

                    if (monthsProcessed == 12) {
                        // All months processed, invoke the callback with the final emission data
                        val emissionData = UserHouseEmission(
                            electricityEmission,
                            charcoalEmission,
                            lpgEmission,
                            firewoodEmission
                        )
                        callback.invoke(emissionData)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }
    fun readUserFoodYearlyEmissions(user: FirebaseUser, year: Int, callback: (UserFoodEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")
        var meatEmission = 0.0
        var fishEmission = 0.0
        var crustaceansEmission = 0.0
        var mollusksEmission = 0.0
        var monthsProcessed = 0

        for (month in 1..12) {
            val dayRef = ref.child(month.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val meat = dataSnapshot.child("Food/meatEmission").getValue(Double::class.java) ?: 0.0
                    val fish = dataSnapshot.child("Food/fishEmission").getValue(Double::class.java) ?: 0.0
                    val crustaceans = dataSnapshot.child("Food/crustaceansEmission").getValue(Double::class.java) ?: 0.0
                    val mollusks = dataSnapshot.child("Food/mollusksEmission").getValue(Double::class.java) ?: 0.0

                    meatEmission += meat
                    fishEmission += fish
                    crustaceansEmission += crustaceans
                    mollusksEmission += mollusks
                    monthsProcessed++

                    if (monthsProcessed == 12) {
                        // All months processed, invoke the callback with the final emission data
                        val emissionData = UserFoodEmission(
                            meatEmission,
                            fishEmission,
                            crustaceansEmission,
                            mollusksEmission
                        )
                        callback.invoke(emissionData)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    fun readUserYearlyEmission(user: FirebaseUser, year: Int, callback: (UserMonthlyEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")
        var houseEmission = 0.0
        var foodEmission = 0.0
        var vehicleEmission = 0.0
        var travelEmission = 0.0

        for (month in 1..12) {
            val dayRef = ref.child(month.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val house = dataSnapshot.child("houseEmission").getValue(Double::class.java) ?: 0.0
                    val food = dataSnapshot.child("foodEmission").getValue(Double::class.java) ?: 0.0
                    val vehicle = dataSnapshot.child("vehicleEmission").getValue(Double::class.java) ?: 0.0
                    val travel = dataSnapshot.child("travelEmission").getValue(Double::class.java) ?: 0.0

                    houseEmission += house
                    foodEmission += food
                    vehicleEmission += vehicle
                    travelEmission += travel


                    if (month == 12) {
                        // All months processed, invoke the callback with the final emission data
                        val emissionData = UserMonthlyEmission(
                            houseEmission,
                            foodEmission,
                            vehicleEmission,
                            travelEmission
                        )
                        callback.invoke(emissionData)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    fun readYearlyEmission(user: FirebaseUser,year: Int, onDailyEmissionsReceived: (List<MonthlyEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")

        val dailyEmissions = mutableListOf<MonthlyEmission>()

        for (month in 1..12) {
            val dayRef = ref.child(month.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val house = dataSnapshot.child("houseEmission").getValue(Double::class.java) ?: 0.0
                    val food = dataSnapshot.child("foodEmission").getValue(Double::class.java) ?: 0.0
                    val vehicle = dataSnapshot.child("vehicleEmission").getValue(Double::class.java) ?: 0.0
                    val travel = dataSnapshot.child("travelEmission").getValue(Double::class.java) ?: 0.0

                    dailyEmissions.add(MonthlyEmission(month, house, food, vehicle, travel))

                    if (month == 12) {
                        onDailyEmissionsReceived(dailyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateYearlyEmissionArray(dailyEmissions: List<MonthlyEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(5) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.month.toString()
            emissionArray[index][1] = dailyEmission.houseEmission.toString()
            emissionArray[index][2] = dailyEmission.foodEmission.toString()
            emissionArray[index][3] = dailyEmission.vehicleEmission.toString()
            emissionArray[index][4] = dailyEmission.travelEmission.toString()
        }

        return emissionArray
    }

    fun readDailyTravelEmissions(user: FirebaseUser,year: Int, month: Int, onDailyEmissionsReceived: (List<DailyTravelEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("DailyTravelEmission/$year/$month")

        val dailyEmissions = mutableListOf<DailyTravelEmission>()

        for (day in 1..31) {
            val dayRef = ref.child(day.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    val tricycleEmission = data.child("Tricycle").getValue(Double::class.java) ?: 0.0
                    val jeepEmission = data.child("Jeep").getValue(Double::class.java) ?: 0.0
                    val electricJeepEmission = data.child("Electric Jeep").getValue(Double::class.java) ?: 0.0
                    val airconditionedElectricJeepEmission = data.child("Airconditioned Electric Jeep").getValue(Double::class.java) ?: 0.0
                    val busEmission = data.child("Bus").getValue(Double::class.java) ?: 0.0
                    val economyBusEmission = data.child("Economy Bus").getValue(Double::class.java) ?: 0.0
                    val airconditionedBusEmission = data.child("Airconditioned Bus").getValue(Double::class.java) ?: 0.0
                    val uvVanEmission = data.child("UV Express Van").getValue(Double::class.java) ?: 0.0
                    val uvTaxiEmission = data.child("UV Express Taxi").getValue(Double::class.java) ?: 0.0
                    dailyEmissions.add(DailyTravelEmission(
                        day,
                        tricycleEmission,
                        jeepEmission,
                        electricJeepEmission,
                        airconditionedElectricJeepEmission,
                        busEmission,
                        economyBusEmission,
                        airconditionedBusEmission,
                        uvVanEmission,
                        uvTaxiEmission))

                    if (day == 31) {
                        onDailyEmissionsReceived(dailyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateDailyTravelEmissionArray(dailyEmissions: List<DailyTravelEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(10) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.day.toString()
            emissionArray[index][1] = dailyEmission.tricycleEmission.toString()
            emissionArray[index][2] = dailyEmission.jeepEmission.toString()
            emissionArray[index][3] = dailyEmission.electricJeepEmission.toString()
            emissionArray[index][4] = dailyEmission.airconditionedElectricJeepEmission.toString()
            emissionArray[index][5] = dailyEmission.busEmission.toString()
            emissionArray[index][6] = dailyEmission.economyBusEmission.toString()
            emissionArray[index][7] = dailyEmission.airconditionedBusEmission.toString()
            emissionArray[index][8] = dailyEmission.uvVanEmission.toString()
            emissionArray[index][9] = dailyEmission.uvTaxiEmission.toString()
        }

        return emissionArray
    }

    fun readYearlyTravelEmissions(user: FirebaseUser,year: Int, onDailyEmissionsReceived: (List<MonthlyTravelEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")

        val dailyEmissions = mutableListOf<MonthlyTravelEmission>()

        for (day in 1..12) {
            val dayRef = ref.child(day.toString()).child(userId).child("Travel")
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(data: DataSnapshot) {
                    val tricycleEmission = data.child("Tricycle").getValue(Double::class.java) ?: 0.0
                    val jeepEmission = data.child("Jeep").getValue(Double::class.java) ?: 0.0
                    val electricJeepEmission = data.child("Electric Jeep").getValue(Double::class.java) ?: 0.0
                    val airconditionedElectricJeepEmission = data.child("Airconditioned Electric Jeep").getValue(Double::class.java) ?: 0.0
                    val busEmission = data.child("Bus").getValue(Double::class.java) ?: 0.0
                    val economyBusEmission = data.child("Economy Bus").getValue(Double::class.java) ?: 0.0
                    val airconditionedBusEmission = data.child("Airconditioned Bus").getValue(Double::class.java) ?: 0.0
                    val uvVanEmission = data.child("UV Express Van").getValue(Double::class.java) ?: 0.0
                    val uvTaxiEmission = data.child("UV Express Taxi").getValue(Double::class.java) ?: 0.0
                    dailyEmissions.add(MonthlyTravelEmission(
                        day,
                        tricycleEmission,
                        jeepEmission,
                        electricJeepEmission,
                        airconditionedElectricJeepEmission,
                        busEmission,
                        economyBusEmission,
                        airconditionedBusEmission,
                        uvVanEmission,
                        uvTaxiEmission))

                    if (day == 12) {
                        onDailyEmissionsReceived(dailyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateYearlyTravelEmissionArray(dailyEmissions: List<MonthlyTravelEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(10) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.month.toString()
            emissionArray[index][1] = dailyEmission.tricycleEmission.toString()
            emissionArray[index][2] = dailyEmission.jeepEmission.toString()
            emissionArray[index][3] = dailyEmission.electricJeepEmission.toString()
            emissionArray[index][4] = dailyEmission.airconditionedElectricJeepEmission.toString()
            emissionArray[index][5] = dailyEmission.busEmission.toString()
            emissionArray[index][6] = dailyEmission.economyBusEmission.toString()
            emissionArray[index][7] = dailyEmission.airconditionedBusEmission.toString()
            emissionArray[index][8] = dailyEmission.uvVanEmission.toString()
            emissionArray[index][9] = dailyEmission.uvTaxiEmission.toString()
        }

        return emissionArray
    }

    fun readDailyHouseEmissions(user: FirebaseUser,year: Int, month: Int, onDailyEmissionsReceived: (List<DailyHouseEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("DailyHomeEmission/$year/$month")

        val dailyEmissions = mutableListOf<DailyHouseEmission>()

        for (day in 1..31) {
            val dayRef = ref.child(day.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val electricity = dataSnapshot.child("electricityEmission").getValue(Double::class.java) ?: 0.0
                    val charcoal = dataSnapshot.child("charcoalEmission").getValue(Double::class.java) ?: 0.0
                    val firewood = dataSnapshot.child("firewoodEmission").getValue(Double::class.java) ?: 0.0
                    val lpg = dataSnapshot.child("lpgEmission").getValue(Double::class.java) ?: 0.0

                    dailyEmissions.add(DailyHouseEmission(day, electricity, charcoal, firewood, lpg))

                    if (day == 31) {
                        onDailyEmissionsReceived(dailyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateDailyHouseEmissionArray(dailyEmissions: List<DailyHouseEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(5) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.day.toString()
            emissionArray[index][1] = dailyEmission.electricityEmission.toString()
            emissionArray[index][2] = dailyEmission.charcoalEmission.toString()
            emissionArray[index][3] = dailyEmission.firewoodEmission.toString()
            emissionArray[index][4] = dailyEmission.lpgEmission.toString()
        }

        return emissionArray
    }

    fun readYearlyHouseEmissions(user: FirebaseUser,year: Int, onDailyEmissionsReceived: (List<MonthlyHouseEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")

        val monthlyEmissions = mutableListOf<MonthlyHouseEmission>()

        for (month in 1..12) {
            val dayRef = ref.child(month.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val electricity = dataSnapshot.child("House/electricityEmission").getValue(Double::class.java) ?: 0.0
                    val charcoal = dataSnapshot.child("House/charcoalEmission").getValue(Double::class.java) ?: 0.0
                    val firewood = dataSnapshot.child("House/firewoodEmission").getValue(Double::class.java) ?: 0.0
                    val lpg = dataSnapshot.child("House/lpgEmission").getValue(Double::class.java) ?: 0.0

                    monthlyEmissions.add(MonthlyHouseEmission(month, electricity, charcoal, firewood, lpg))

                    if (month == 12) {
                        onDailyEmissionsReceived(monthlyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateYearlyHouseEmissionArray(dailyEmissions: List<MonthlyHouseEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(5) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.month.toString()
            emissionArray[index][1] = dailyEmission.electricityEmission.toString()
            emissionArray[index][2] = dailyEmission.charcoalEmission.toString()
            emissionArray[index][3] = dailyEmission.lpgEmission.toString()
            emissionArray[index][4] = dailyEmission.firewoodEmission.toString()
        }

        return emissionArray
    }

    fun readDailyVehicleEmissions(user: FirebaseUser,year: Int, month: Int, onDailyEmissionsReceived: (List<DailyVehicleEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("DailyVehicleEmission/$year/$month")

        val dailyEmissions = mutableListOf<DailyVehicleEmission>()

        for (day in 1..31) {
            val dayRef = ref.child(day.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val premium = dataSnapshot.child("PremiumEmission").getValue(Double::class.java) ?: 0.0
                    val diesel = dataSnapshot.child("DieselEmission").getValue(Double::class.java) ?: 0.0
                    val unleaded = dataSnapshot.child("UnleadedEmission").getValue(Double::class.java) ?: 0.0

                    dailyEmissions.add(DailyVehicleEmission(day, premium, diesel, unleaded))

                    if (day == 31) {
                        onDailyEmissionsReceived(dailyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateDailyVehicleEmissionArray(dailyEmissions: List<DailyVehicleEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(5) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.day.toString()
            emissionArray[index][1] = dailyEmission.premiumEmission.toString()
            emissionArray[index][2] = dailyEmission.dieselEmission.toString()
            emissionArray[index][3] = dailyEmission.unleadedEmission.toString()
        }

        return emissionArray
    }

    fun readYearlyVehicleEmissions(user: FirebaseUser,year: Int, onDailyEmissionsReceived: (List<MonthlyVehicleEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")

        val monthlyEmissions = mutableListOf<MonthlyVehicleEmission>()

        for (month in 1..12) {
            val dayRef = ref.child(month.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val premium = dataSnapshot.child("Vehicle/premiumEmission").getValue(Double::class.java) ?: 0.0
                    val diesel = dataSnapshot.child("Vehicle/dieselEmission").getValue(Double::class.java) ?: 0.0
                    val unleaded = dataSnapshot.child("Vehicle/unleadedEmission").getValue(Double::class.java) ?: 0.0

                    monthlyEmissions.add(MonthlyVehicleEmission(month, premium, diesel, unleaded))

                    if (month == 12) {
                        onDailyEmissionsReceived(monthlyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateYearlyVehicleEmissionArray(dailyEmissions: List<MonthlyVehicleEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(5) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.month.toString()
            emissionArray[index][1] = dailyEmission.premiumEmission.toString()
            emissionArray[index][2] = dailyEmission.dieselEmission.toString()
            emissionArray[index][3] = dailyEmission.unleadedEmission.toString()
        }

        return emissionArray
    }

    fun readDailyFoodEmissions(user: FirebaseUser,year: Int, month: Int, onDailyEmissionsReceived: (List<DailyFoodEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("DailyFoodEmission/$year/$month")

        val dailyEmissions = mutableListOf<DailyFoodEmission>()

        for (day in 1..31) {
            val dayRef = ref.child(day.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val meat = dataSnapshot.child("meatEmission").getValue(Double::class.java) ?: 0.0
                    val fish = dataSnapshot.child("fishEmission").getValue(Double::class.java) ?: 0.0
                    val crustaceans = dataSnapshot.child("crustaceansEmission").getValue(Double::class.java) ?: 0.0
                    val mollusks = dataSnapshot.child("mollusksEmission").getValue(Double::class.java) ?: 0.0

                    dailyEmissions.add(DailyFoodEmission(day, meat, fish, crustaceans, mollusks))

                    if (day == 31) {
                        onDailyEmissionsReceived(dailyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateDailyFoodEmissionArray(dailyEmissions: List<DailyFoodEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(5) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.day.toString()
            emissionArray[index][1] = dailyEmission.meatEmission.toString()
            emissionArray[index][2] = dailyEmission.fishEmission.toString()
            emissionArray[index][3] = dailyEmission.crustaceansEmission.toString()
            emissionArray[index][4] = dailyEmission.mollusksEmission.toString()
        }

        return emissionArray
    }

    fun readMonthlyFoodEmissions(user: FirebaseUser,year: Int, onDailyEmissionsReceived: (List<MonthlyFoodEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserMonthlyEmission/$year")

        val monthlyEmissions = mutableListOf<MonthlyFoodEmission>()

        for (month in 1..12) {
            val monthRef = ref.child(month.toString()).child(userId)
            monthRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val meat = dataSnapshot.child("Food/meatEmission").getValue(Double::class.java) ?: 0.0
                    val fish = dataSnapshot.child("Food/fishEmission").getValue(Double::class.java) ?: 0.0
                    val crustaceans = dataSnapshot.child("Food/crustaceansEmission").getValue(Double::class.java) ?: 0.0
                    val mollusks = dataSnapshot.child("Food/mollusksEmission").getValue(Double::class.java) ?: 0.0

                    monthlyEmissions.add(MonthlyFoodEmission(month, meat, fish, crustaceans, mollusks))

                    if (month == 12) {
                        onDailyEmissionsReceived(monthlyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateMonthlyFoodEmissionArray(dailyEmissions: List<MonthlyFoodEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(5) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.month.toString()
            emissionArray[index][1] = dailyEmission.meatEmission.toString()
            emissionArray[index][2] = dailyEmission.fishEmission.toString()
            emissionArray[index][3] = dailyEmission.crustaceansEmission.toString()
            emissionArray[index][4] = dailyEmission.mollusksEmission.toString()
        }

        return emissionArray
    }

    fun readDailyEmissions(user: FirebaseUser,year: Int, month: Int, onDailyEmissionsReceived: (List<DailyEmission>) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserDailyEmission/$year/$month")

        val dailyEmissions = mutableListOf<DailyEmission>()

        for (day in 1..31) {
            val dayRef = ref.child(day.toString()).child(userId)
            dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val house = dataSnapshot.child("houseEmission").getValue(Double::class.java) ?: 0.0
                    val food = dataSnapshot.child("foodEmission").getValue(Double::class.java) ?: 0.0
                    val vehicle = dataSnapshot.child("vehicleEmission").getValue(Double::class.java) ?: 0.0
                    val travel = dataSnapshot.child("travelEmission").getValue(Double::class.java) ?: 0.0

                    dailyEmissions.add(DailyEmission(day, house, food, vehicle, travel))

                    if (day == 31) {
                        onDailyEmissionsReceived(dailyEmissions)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    // Function to populate a 2D array with daily emission data
    fun populateDailyEmissionArray(dailyEmissions: List<DailyEmission>): Array<Array<String>> {
        val emissionArray = Array(dailyEmissions.size) { Array(5) { "" } }

        dailyEmissions.forEachIndexed { index, dailyEmission ->
            emissionArray[index][0] = dailyEmission.day.toString()
            emissionArray[index][1] = dailyEmission.houseEmission.toString()
            emissionArray[index][2] = dailyEmission.foodEmission.toString()
            emissionArray[index][3] = dailyEmission.vehicleEmission.toString()
            emissionArray[index][4] = dailyEmission.travelEmission.toString()
        }

        return emissionArray
    }

    /*fun readDailyEmission(user: FirebaseUser, year: Int, month: Int, callback: (List<DailyEmission>) -> Unit) {
        val userId = user.uid
        val database = Firebase.database
        val userMonthlyEmissionRef = database.getReference("UserDailyEmission/$year/$month")

        // Query to retrieve the daily emissions for the month for the given user
        userMonthlyEmissionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userDailyEmission = mutableListOf<DailyEmission>()

                // For loop of the daily emissions for the month
                for (daySnapshot in dataSnapshot.children) {
                    val day = daySnapshot.key // Get the day of the month
                    val userEmissionRef = daySnapshot.child(userId) // Get the user's emission for that day
                    val house = userEmissionRef.child("houseEmission").getValue(Double::class.java) ?: 0.0
                    val food = userEmissionRef.child("foodEmission").getValue(Double::class.java) ?: 0.0
                    val vehicle = userEmissionRef.child("vehicleEmission").getValue(Double::class.java) ?: 0.0
                    val travel = userEmissionRef.child("travelEmission").getValue(Double::class.java) ?: 0.0
                    if(day != null){
                        userDailyEmission.add(DailyEmission(
                            day.toInt(),
                            house,
                            food,
                            vehicle,
                            travel
                        ))
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    } */

    fun readMunicipalityTypeEmission(type: String, check: Boolean, year: Int, month: Int, callback: (MapMunicipalityTotalEmission) -> Unit) {
        if(check){
            database = FirebaseDatabase.getInstance().getReference("MapEmission")
        } else {
            database = FirebaseDatabase.getInstance().getReference("LocationEmission/$year/$month")
        }
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                if(type == "Overall") {
                    val abucayEmission = data.child("Region III/Bataan/Abucay/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val bagacEmission = data.child("Region III/Bataan/Bagac/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val balangaEmission = data.child("Region III/Bataan/Balanga/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val dinalupihanEmission =
                        data.child("Region III/Bataan/Dinalupihan/TotalEmission")
                            .getValue(Double::class.java) ?: 0.0
                    val hermosaEmission = data.child("Region III/Bataan/Hermosa/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val limayEmission = data.child("Region III/Bataan/Limay/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val marivelesEmission = data.child("Region III/Bataan/Mariveles/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val morongEmission = data.child("Region III/Bataan/Morong/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val oraniEmission = data.child("Region III/Bataan/Orani/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val orionEmission = data.child("Region III/Bataan/Orion/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val pilarEmission = data.child("Region III/Bataan/Pilar/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val samalEmission = data.child("Region III/Bataan/Samal/TotalEmission")
                        .getValue(Double::class.java) ?: 0.0
                    val emissionData = MapMunicipalityTotalEmission(
                        abucayEmission,
                        bagacEmission,
                        balangaEmission,
                        dinalupihanEmission,
                        hermosaEmission,
                        limayEmission,
                        marivelesEmission,
                        morongEmission,
                        oraniEmission,
                        orionEmission,
                        pilarEmission,
                        samalEmission
                    )
                    callback.invoke(emissionData)
                } else {
                    val abucayEmission = data.child("Region III/Bataan/Abucay/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val bagacEmission = data.child("Region III/Bataan/Bagac/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val balangaEmission = data.child("Region III/Bataan/Balanga/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val dinalupihanEmission = data.child("Region III/Bataan/Dinalupihan/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val hermosaEmission = data.child("Region III/Bataan/Hermosa/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val limayEmission = data.child("Region III/Bataan/Limay/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val marivelesEmission = data.child("Region III/Bataan/Mariveles/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val morongEmission = data.child("Region III/Bataan/Morong/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val oraniEmission = data.child("Region III/Bataan/Orani/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val orionEmission = data.child("Region III/Bataan/Orion/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val pilarEmission = data.child("Region III/Bataan/Pilar/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val samalEmission = data.child("Region III/Bataan/Samal/$type/TotalEmission").getValue()?.toString()?.toDoubleOrNull() ?: 0.0
                    val emissionData = MapMunicipalityTotalEmission(
                        abucayEmission,
                        bagacEmission,
                        balangaEmission,
                        dinalupihanEmission,
                        hermosaEmission,
                        limayEmission,
                        marivelesEmission,
                        morongEmission,
                        oraniEmission,
                        orionEmission,
                        pilarEmission,
                        samalEmission
                    )
                    callback.invoke(emissionData)
                }
            } else {
                val emissionData = MapMunicipalityTotalEmission( 0.0, 0.0,0.0, 0.0, 0.0, 0.0,0.0, 0.0, 0.0, 0.0,0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readMapTypeEmission(region: String, province: String, municipality: String, type: String, callback: (MapTypeEmission) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("MapEmission")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val regionTypeEmission = data.child("$region/$type/TotalEmission").getValue(Double::class.java) ?: 0.0
                val provinceTypeEmission = data.child("$region/$province/$type/TotalEmission").getValue(Double::class.java) ?: 0.0
                val municipalityTypeEmission = data.child("$region/$province/$municipality/$type/TotalEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = MapTypeEmission(
                    regionTypeEmission,
                    provinceTypeEmission,
                    municipalityTypeEmission
                )
                callback.invoke(emissionData)
            } else {
                val emissionData = MapTypeEmission( 0.0, 0.0,0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }
    fun readMapTotalEmission(region: String, province: String, municipality: String, callback: (MapTotalEmission) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("MapEmission")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val regionEmission = data.child("$region/TotalEmission").getValue(Double::class.java) ?: 0.0
                val provinceEmission = data.child("$region/$province/TotalEmission").getValue(Double::class.java) ?: 0.0
                val municipalityEmission = data.child("$region/$province/$municipality/TotalEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = MapTotalEmission(
                    regionEmission,
                    provinceEmission,
                    municipalityEmission
                )
                callback.invoke(emissionData)
            } else {
                val emissionData = MapTotalEmission(0.0, 0.0,0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readMapCategoryEmission(region: String, province: String, municipality: String, type: String, category: String, callback: (MapCategoryEmission) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("MapEmission")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val regionCategoryEmission = data.child("$region/$type/$category/TotalEmission").getValue(Double::class.java) ?: 0.0
                val provinceCategoryEmission = data.child("$region/$province/$type/$category/TotalEmission").getValue(Double::class.java) ?: 0.0
                val municipalityCategoryEmission = data.child("$region/$province/$municipality/$type/$category/TotalEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = MapCategoryEmission(
                    regionCategoryEmission,
                    provinceCategoryEmission,
                    municipalityCategoryEmission
                )
                callback.invoke(emissionData)
            } else {
                val emissionData = MapCategoryEmission( 0.0, 0.0,0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readRegionCategoryEmission(year: Int, month: Int, region: String, type: String, category: String, callback: (CategoryEmission) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("LocationEmission/$year/$month/$region/$type/$category")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val currentEmission = data.child("TotalEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = CategoryEmission(
                    currentEmission
                )
                callback.invoke(emissionData)
            } else {
                val emissionData = CategoryEmission(0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readProvinceCategoryEmission(year: Int, month: Int, region: String, province: String, type: String, category: String, callback: (CategoryEmission) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("LocationEmission/$year/$month/$region/$province/$type/$category")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val currentEmission = data.child("TotalEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = CategoryEmission(
                    currentEmission
                )
                callback.invoke(emissionData)
            } else {
                val emissionData = CategoryEmission(0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readMunicipalityCategoryEmission(year: Int, month: Int, region: String, province: String, municipality: String, type: String, category: String, callback: (CategoryEmission) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("LocationEmission/$year/$month/$region/$province/$type/$category")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val currentEmission = data.child("TotalEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = CategoryEmission(
                    currentEmission
                )
                callback.invoke(emissionData)
            } else {
                val emissionData = CategoryEmission(0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readProvinceEmission(year: Int, month: Int, region: String, province: String, callback: (ProvinceEmission) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("LocationEmission/$year/$month/$region/$province")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val totalEmission = data.child("TotalEmission").getValue(Double::class.java) ?: 0.0
                val electricityEmission = data.child("House/Electric/TotalEmission").getValue(Double::class.java) ?: 0.0
                val charcoalEmission = data.child("House/Charcoal/TotalEmission").getValue(Double::class.java) ?: 0.0
                val lpgEmission = data.child("House/LPG/TotalEmission").getValue(Double::class.java) ?: 0.0
                val firewoodEmission = data.child("House/Firewood/TotalEmission").getValue(Double::class.java) ?: 0.0
                val meatEmission = data.child("Food/Meat/TotalEmission").getValue(Double::class.java) ?: 0.0
                val fishEmission = data.child("Food/Fish/TotalEmission").getValue(Double::class.java) ?: 0.0
                val crustaceansEmission = data.child("Food/crustaceansEmission").getValue(Double::class.java) ?: 0.0
                val mollusksEmission = data.child("Food/Mollusks/TotalEmission").getValue(Double::class.java) ?: 0.0
                val tricycleEmission = data.child("Travel/Tricycle/TotalEmission").getValue(Double::class.java) ?: 0.0
                val pujEmission = data.child("Travel/Jeep/TotalEmission").getValue(Double::class.java) ?: 0.0
                val epujEmission = data.child("Travel/Electric Jeep/TotalEmission").getValue(Double::class.java) ?: 0.0
                val epujaEmission = data.child("Travel/Airconditioned Electric Jeep/TotalEmission").getValue(Double::class.java) ?: 0.0
                val pubEmission = data.child("Travel/Bus/TotalEmission").getValue(Double::class.java) ?: 0.0
                val pubaEmission = data.child("Travel/Airconditioned Bus/TotalEmission").getValue(Double::class.java) ?: 0.0
                val pubeEmission = data.child("Travel/Economy Bus/TotalEmission").getValue(Double::class.java) ?: 0.0
                val taxiEmission = data.child("Travel/UV Express Taxi/TotalEmission").getValue(Double::class.java) ?: 0.0
                val uvexpressEmission = data.child("Travel/UV Express Van/TotalEmission").getValue(Double::class.java) ?: 0.0
                val premiumEmission = data.child("Vehicle/Premium/TotalEmission").getValue(Double::class.java) ?: 0.0
                val dieselEmission = data.child("Vehicle/Diesel/TotalEmission").getValue(Double::class.java) ?: 0.0
                val unleadedEmission = data.child("Vehicle/Unleaded/TotalEmission").getValue(Double::class.java) ?: 0.0
                val houseEmission = electricityEmission + charcoalEmission + lpgEmission + firewoodEmission
                val foodEmission = meatEmission + fishEmission + crustaceansEmission + mollusksEmission
                val travelEmission = tricycleEmission + pujEmission + epujEmission + epujaEmission + pubEmission + pubeEmission + pubaEmission + taxiEmission + uvexpressEmission
                val vehicleEmission = premiumEmission + dieselEmission + unleadedEmission

                val emissionData = ProvinceEmission(
                    totalEmission,
                    electricityEmission,
                    charcoalEmission,
                    lpgEmission,
                    firewoodEmission,
                    meatEmission,
                    fishEmission,
                    crustaceansEmission,
                    mollusksEmission,
                    tricycleEmission,
                    pujEmission,
                    epujEmission,
                    epujaEmission,
                    pubEmission,
                    pubaEmission,
                    pubeEmission,
                    taxiEmission,
                    uvexpressEmission,
                    premiumEmission,
                    dieselEmission,
                    unleadedEmission,
                    houseEmission,
                    foodEmission,
                    travelEmission,
                    vehicleEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = ProvinceEmission(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
        }

    fun readMunicipalityEmission(year: Int, month: Int, region: String, province: String, municipality: String, callback: (MunicipalityEmission) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("LocationEmission/$year/$month/$region/$province/$municipality")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val totalEmission = data.child("TotalEmission").getValue(Double::class.java) ?: 0.0
                val electricityEmission = data.child("House/Electric/TotalEmission").getValue(Double::class.java) ?: 0.0
                val charcoalEmission = data.child("House/Charcoal/TotalEmission").getValue(Double::class.java) ?: 0.0
                val lpgEmission = data.child("House/LPG/TotalEmission").getValue(Double::class.java) ?: 0.0
                val firewoodEmission = data.child("House/Firewood/TotalEmission").getValue(Double::class.java) ?: 0.0
                val meatEmission = data.child("Food/Meat/TotalEmission").getValue(Double::class.java) ?: 0.0
                val fishEmission = data.child("Food/Fish/TotalEmission").getValue(Double::class.java) ?: 0.0
                val crustaceansEmission = data.child("Food/crustaceansEmission").getValue(Double::class.java) ?: 0.0
                val mollusksEmission = data.child("Food/Mollusks/TotalEmission").getValue(Double::class.java) ?: 0.0
                val tricycleEmission = data.child("Travel/Tricycle/TotalEmission").getValue(Double::class.java) ?: 0.0
                val pujEmission = data.child("Travel/Jeep/TotalEmission").getValue(Double::class.java) ?: 0.0
                val epujEmission = data.child("Travel/Electric Jeep/TotalEmission").getValue(Double::class.java) ?: 0.0
                val epujaEmission = data.child("Travel/Airconditioned Electric Jeep/TotalEmission").getValue(Double::class.java) ?: 0.0
                val pubEmission = data.child("Travel/Bus/TotalEmission").getValue(Double::class.java) ?: 0.0
                val pubaEmission = data.child("Travel/Airconditioned Bus/TotalEmission").getValue(Double::class.java) ?: 0.0
                val pubeEmission = data.child("Travel/Economy Bus/TotalEmission").getValue(Double::class.java) ?: 0.0
                val taxiEmission = data.child("Travel/UV Express Taxi/TotalEmission").getValue(Double::class.java) ?: 0.0
                val uvexpressEmission = data.child("Travel/UV Express Van/TotalEmission").getValue(Double::class.java) ?: 0.0
                val premiumEmission = data.child("Vehicle/Premium/TotalEmission").getValue(Double::class.java) ?: 0.0
                val dieselEmission = data.child("Vehicle/Diesel/TotalEmission").getValue(Double::class.java) ?: 0.0
                val unleadedEmission = data.child("Vehicle/Unleaded/TotalEmission").getValue(Double::class.java) ?: 0.0
                val houseEmission = electricityEmission + charcoalEmission + lpgEmission + firewoodEmission
                val foodEmission = meatEmission + fishEmission + crustaceansEmission + mollusksEmission
                val travelEmission = tricycleEmission + pujEmission + epujEmission + epujaEmission + pubEmission + pubeEmission + pubaEmission + taxiEmission + uvexpressEmission
                val vehicleEmission = premiumEmission + dieselEmission + unleadedEmission

                val emissionData =MunicipalityEmission(
                    totalEmission,
                    electricityEmission,
                    charcoalEmission,
                    lpgEmission,
                    firewoodEmission,
                    meatEmission,
                    fishEmission,
                    crustaceansEmission,
                    mollusksEmission,
                    tricycleEmission,
                    pujEmission,
                    epujEmission,
                    epujaEmission,
                    pubEmission,
                    pubaEmission,
                    pubeEmission,
                    taxiEmission,
                    uvexpressEmission,
                    premiumEmission,
                    dieselEmission,
                    unleadedEmission,
                    houseEmission,
                    foodEmission,
                    travelEmission,
                    vehicleEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = MunicipalityEmission(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readOffsetActivity(onOffsetActivityReceived: (List<OffsetActivity>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("OffsetActivity")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val offsetActivity = mutableListOf<OffsetActivity>()
                dataSnapshot.children.forEach { activitySnapshot ->
                    val offsetActivityKey = activitySnapshot.key
                    val title = activitySnapshot.child("Title").getValue(String::class.java) ?: ""
                    val description = activitySnapshot.child("Description").getValue(String::class.java) ?: ""
                    val link = activitySnapshot.child("Link").getValue(String::class.java) ?: ""
                    val status = activitySnapshot.child("Status").getValue(String::class.java) ?: ""
                    if(status == "Active" && offsetActivityKey != null){
                        offsetActivity.add(OffsetActivity(
                            offsetKey = offsetActivityKey,
                            title = title,
                            description = description,
                            link = link,
                            status = status
                        )
                        )
                    }
                }
                onOffsetActivityReceived(offsetActivity)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

    // Function to populate a 2D array with offset activity data
    fun populateOffsetArray(offsetActivity: List<OffsetActivity>): Array<Array<String>> {
        val offsetActivityArray = Array(offsetActivity.size) { Array(5) { "" } }

        offsetActivity.forEachIndexed { index, offsetActivity ->
            offsetActivityArray[index][0] = offsetActivity.offsetKey
            offsetActivityArray[index][1] = offsetActivity.title
            offsetActivityArray[index][2] = offsetActivity.description
            offsetActivityArray[index][3] = offsetActivity.link
            offsetActivityArray[index][4] = offsetActivity.status
        }

        return offsetActivityArray
    }

    fun readUserInitialEmission(user: FirebaseUser, callback: (UserInitialEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("UserInitialEmission/$userId")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val houseEmission = data.child("houseEmission").getValue(Double::class.java) ?: 0.0
                val electricityEmission = data.child("House/electricityEmission").getValue(Double::class.java) ?: 0.0
                val charcoalEmission = data.child("House/charcoalEmission").getValue(Double::class.java) ?: 0.0
                val lpgEmission = data.child("House/lpgEmission").getValue(Double::class.java) ?: 0.0
                val firewoodEmission = data.child("House/firewoodEmission").getValue(Double::class.java) ?: 0.0
                val foodEmission = data.child("foodEmission").getValue(Double::class.java) ?: 0.0
                val meatEmission = data.child("Food/meatEmission").getValue(Double::class.java) ?: 0.0
                val fishEmission = data.child("Food/fishEmission").getValue(Double::class.java) ?: 0.0
                val crustaceansEmission = data.child("Food/crustaceansEmission").getValue(Double::class.java) ?: 0.0
                val mollusksEmission = data.child("Food/mollusksEmission").getValue(Double::class.java) ?: 0.0
                val travelEmission = data.child("travelEmission").getValue(Double::class.java) ?: 0.0
                val tricycleEmission = data.child("Travel/Tricycle").getValue(Double::class.java) ?: 0.0
                val pujEmission = data.child("Travel/Jeep").getValue(Double::class.java) ?: 0.0
                val epujEmission = data.child("Travel/Electric Jeep").getValue(Double::class.java) ?: 0.0
                val epujaEmission = data.child("Travel/Airconditioned Electric Jeep").getValue(Double::class.java) ?: 0.0
                val pubEmission = data.child("Travel/Bus").getValue(Double::class.java) ?: 0.0
                val pubaEmission = data.child("Travel/Airconditioned Bus").getValue(Double::class.java) ?: 0.0
                val pubeEmission = data.child("Travel/Economy Bus").getValue(Double::class.java) ?: 0.0
                val taxiEmission = data.child("Travel/UV Express taxi").getValue(Double::class.java) ?: 0.0
                val uvexpressEmission = data.child("Travel/UV Express Van").getValue(Double::class.java) ?: 0.0
                val vehicleEmission = data.child("vehicleEmission").getValue(Double::class.java) ?: 0.0
                val premiumEmission = data.child("Vehicle/premiumEmission").getValue(Double::class.java) ?: 0.0
                val dieselEmission = data.child("Vehicle/dieselEmission").getValue(Double::class.java) ?: 0.0
                val unleadedEmission = data.child("Vehicle/unleadedEmission").getValue(Double::class.java) ?: 0.0

                val emissionData = UserInitialEmission(
                    houseEmission,
                    electricityEmission,
                    charcoalEmission,
                    lpgEmission,
                    firewoodEmission,
                    foodEmission,
                    meatEmission,
                    fishEmission,
                    crustaceansEmission,
                    mollusksEmission,
                    travelEmission,
                    tricycleEmission,
                    pujEmission,
                    epujEmission,
                    epujaEmission,
                    pubEmission,
                    pubaEmission,
                    pubeEmission,
                    taxiEmission,
                    uvexpressEmission,
                    vehicleEmission,
                    premiumEmission,
                    dieselEmission,
                    unleadedEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserInitialEmission(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserDailyEmission(user: FirebaseUser, year: Int, month: Int, day: Int, callback: (UserDailyEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("UserDailyEmission/$year/$month/$day/$userId")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val houseEmission = data.child("houseEmission").getValue(Double::class.java) ?: 0.0
                val foodEmission = data.child("foodEmission").getValue(Double::class.java) ?: 0.0
                val travelEmission = data.child("travelEmission").getValue(Double::class.java) ?: 0.0
                val vehicleEmission = data.child("vehicleEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = UserDailyEmission(houseEmission, foodEmission, vehicleEmission, travelEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserDailyEmission(0.0, 0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserHouseEmission(user: FirebaseUser, year: Int, month: Int, callback: (UserHouseEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("UserMonthlyEmission")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val electricityEmission = data.child("$year/$month/$userId/House/electricityEmission").getValue(Double::class.java) ?: 0.0
                val charcoalEmission = data.child("$year/$month/$userId/House/charcoalEmission").getValue(Double::class.java) ?: 0.0
                val lpgEmission = data.child("$year/$month/$userId/House/lpgEmission").getValue(Double::class.java) ?: 0.0
                val firewoodEmission = data.child("$year/$month/$userId/House/firewoodEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = UserHouseEmission(electricityEmission, charcoalEmission, lpgEmission, firewoodEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserHouseEmission(0.0, 0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserDailyHouseEmission(user: FirebaseUser, year: Int, month: Int, day: Int, callback: (UserHouseEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("DailyHomeEmission/$year/$month/$day/$userId")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val electricityEmission = data.child("electricityEmission").getValue(Double::class.java) ?: 0.0
                val charcoalEmission = data.child("charcoalEmission").getValue(Double::class.java) ?: 0.0
                val lpgEmission = data.child("lpgEmission").getValue(Double::class.java) ?: 0.0
                val firewoodEmission = data.child("firewoodEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = UserHouseEmission(electricityEmission, charcoalEmission, lpgEmission, firewoodEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserHouseEmission(0.0, 0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserFoodEmission(user: FirebaseUser, year: Int, month: Int, callback: (UserFoodEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("UserMonthlyEmission")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val meatEmission = data.child("$year/$month/$userId/Food/meatEmission").getValue(Double::class.java) ?: 0.0
                val fishEmission = data.child("$year/$month/$userId/Food/fishEmission").getValue(Double::class.java) ?: 0.0
                val crustaceansEmission = data.child("$year/$month/$userId/Food/crustaceansEmission").getValue(Double::class.java) ?: 0.0
                val mollusksEmission = data.child("$year/$month/$userId/Food/mollusksEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = UserFoodEmission(meatEmission, fishEmission, crustaceansEmission, mollusksEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserFoodEmission(0.0, 0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserDailyFoodEmission(user: FirebaseUser, year: Int, month: Int, day: Int, callback: (UserFoodEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("DailyFoodEmission/$year/$month/$day/$userId")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val meatEmission = data.child("meatEmission").getValue(Double::class.java) ?: 0.0
                val fishEmission = data.child("fishEmission").getValue(Double::class.java) ?: 0.0
                val crustaceansEmission = data.child("crustaceansEmission").getValue(Double::class.java) ?: 0.0
                val mollusksEmission = data.child("mollusksEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = UserFoodEmission(meatEmission, fishEmission, crustaceansEmission, mollusksEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserFoodEmission(0.0, 0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserVehicleEmission(user: FirebaseUser, year: Int, month: Int, callback: (UserVehicleEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("UserMonthlyEmission")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val premiumEmission = data.child("$year/$month/$userId/Vehicle/premiumEmission").getValue(Double::class.java) ?: 0.0
                val unleadedEmission = data.child("$year/$month/$userId/Vehicle/unleadedEmission").getValue(Double::class.java) ?: 0.0
                val dieselEmission = data.child("$year/$month/$userId/Vehicle/dieselEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = UserVehicleEmission(premiumEmission, unleadedEmission, dieselEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserVehicleEmission(0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserDailyVehicleEmission(user: FirebaseUser, year: Int, month: Int, day: Int, callback: (UserVehicleEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("DailyVehicleEmission/$year/$month/$day/$userId/")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val premiumEmission = data.child("PremiumEmission").getValue(Double::class.java) ?: 0.0
                val unleadedEmission = data.child("UnleadedEmission").getValue(Double::class.java) ?: 0.0
                val dieselEmission = data.child("DieselEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = UserVehicleEmission(premiumEmission, unleadedEmission, dieselEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserVehicleEmission(0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserTravelEmission(user: FirebaseUser, year: Int, month: Int, callback: (UserTravelEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("UserMonthlyEmission")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val tricycleEmission = data.child("$year/$month/$userId/Travel/Tricycle").getValue(Double::class.java) ?: 0.0
                val jeepEmission = data.child("$year/$month/$userId/Travel/Jeep").getValue(Double::class.java) ?: 0.0
                val electricJeepEmission = data.child("$year/$month/$userId/Travel/Electric Jeep").getValue(Double::class.java) ?: 0.0
                val airconditionedElectricJeepEmission = data.child("$year/$month/$userId/Travel/Airconditioned Electric Jeep").getValue(Double::class.java) ?: 0.0
                val busEmission = data.child("$year/$month/$userId/Travel/Bus").getValue(Double::class.java) ?: 0.0
                val economyBusEmission = data.child("$year/$month/$userId/Travel/Economy Bus").getValue(Double::class.java) ?: 0.0
                val airconditionedBusEmission = data.child("$year/$month/$userId/Travel/Airconditioned Bus").getValue(Double::class.java) ?: 0.0
                val uvVanEmission = data.child("$year/$month/$userId/Travel/UV Express Van").getValue(Double::class.java) ?: 0.0
                val uvTaxiEmission = data.child("$year/$month/$userId/Travel/UV Express Taxi").getValue(Double::class.java) ?: 0.0
                val emissionData = UserTravelEmission(
                    tricycleEmission,
                    jeepEmission,
                    electricJeepEmission,
                    airconditionedElectricJeepEmission,
                    busEmission,
                    economyBusEmission,
                    airconditionedBusEmission,
                    uvVanEmission,
                    uvTaxiEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserTravelEmission(0.0, 0.0, 0.0, 0.0,0.0,0.0,0.0,0.0,0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserDailyTravelEmission(user: FirebaseUser, year: Int, month: Int, day: Int, callback: (UserTravelEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("DailyTravelEmission/$year/$month/$day/$userId")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val tricycleEmission = data.child("Tricycle").getValue(Double::class.java) ?: 0.0
                val jeepEmission = data.child("Jeep").getValue(Double::class.java) ?: 0.0
                val electricJeepEmission = data.child("Electric Jeep").getValue(Double::class.java) ?: 0.0
                val airconditionedElectricJeepEmission = data.child("Airconditioned Electric Jeep").getValue(Double::class.java) ?: 0.0
                val busEmission = data.child("Bus").getValue(Double::class.java) ?: 0.0
                val economyBusEmission = data.child("Economy Bus").getValue(Double::class.java) ?: 0.0
                val airconditionedBusEmission = data.child("Airconditioned Bus").getValue(Double::class.java) ?: 0.0
                val uvVanEmission = data.child("UV Express Van").getValue(Double::class.java) ?: 0.0
                val uvTaxiEmission = data.child("UV Express Taxi").getValue(Double::class.java) ?: 0.0
                val emissionData = UserTravelEmission(
                    tricycleEmission,
                    jeepEmission,
                    electricJeepEmission,
                    airconditionedElectricJeepEmission,
                    busEmission,
                    economyBusEmission,
                    airconditionedBusEmission,
                    uvVanEmission,
                    uvTaxiEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserTravelEmission(0.0, 0.0, 0.0, 0.0,0.0,0.0,0.0,0.0,0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }


    fun readLocationEmission(region: String, province: String, municipality: String ,year: Int, month: Int, callback: (LocationEmission) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("LocationEmission")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val regionEmission = data.child("$year/$month/$region/TotalEmission").getValue(Double::class.java) ?: 0.0
                val provinceEmission = data.child("$year/$month/$region/$province/TotalEmission").getValue(Double::class.java) ?: 0.0
                val municipalityEmission = data.child("$year/$month/$region/$province/$municipality/TotalEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = LocationEmission(
                    regionEmission,
                    provinceEmission,
                    municipalityEmission
                )
                callback.invoke(emissionData)
            } else {
                val emissionData = LocationEmission(
                    0.0,
                    0.0,
                    0.0
                )
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserMonthlyEmission(user: FirebaseUser, year: Int, month: Int, callback: (UserInitialEmission) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("UserMonthlyEmission/$year/$month/$userId")
        database.get().addOnSuccessListener { data ->
            if (data.exists()) {
                val houseEmission = data.child("houseEmission").getValue(Double::class.java) ?: 0.0
                val foodEmission = data.child("foodEmission").getValue(Double::class.java) ?: 0.0
                val travelEmission = data.child("travelEmission").getValue(Double::class.java) ?: 0.0
                val vehicleEmission = data.child("vehicleEmission").getValue(Double::class.java) ?: 0.0
                val electricityEmission = data.child("House/electricityEmission").getValue(Double::class.java) ?: 0.0
                val charcoalEmission = data.child("House/charcoalEmission").getValue(Double::class.java) ?: 0.0
                val lpgEmission = data.child("House/lpgEmission").getValue(Double::class.java) ?: 0.0
                val firewoodEmission = data.child("House/firewoodEmission").getValue(Double::class.java) ?: 0.0
                val meatEmission = data.child("Food/meatEmission").getValue(Double::class.java) ?: 0.0
                val fishEmission = data.child("Food/fishEmission").getValue(Double::class.java) ?: 0.0
                val crustaceansEmission = data.child("Food/crustaceansEmission").getValue(Double::class.java) ?: 0.0
                val mollusksEmission = data.child("Food/mollusksEmission").getValue(Double::class.java) ?: 0.0
                val tricycleEmission = data.child("Travel/Tricycle").getValue(Double::class.java) ?: 0.0
                val pujEmission = data.child("Travel/Jeep").getValue(Double::class.java) ?: 0.0
                val epujEmission = data.child("Travel/Electric Jeep").getValue(Double::class.java) ?: 0.0
                val epujaEmission = data.child("Travel/Airconditioned Electric Jeep").getValue(Double::class.java) ?: 0.0
                val pubEmission = data.child("Travel/Bus").getValue(Double::class.java) ?: 0.0
                val pubaEmission = data.child("Travel/Airconditioned Bus").getValue(Double::class.java) ?: 0.0
                val pubeEmission = data.child("Travel/Economy Bus").getValue(Double::class.java) ?: 0.0
                val taxiEmission = data.child("Travel/UV Express taxi").getValue(Double::class.java) ?: 0.0
                val uvexpressEmission = data.child("Travel/UV Express Van").getValue(Double::class.java) ?: 0.0
                val premiumEmission = data.child("Vehicle/premiumEmission").getValue(Double::class.java) ?: 0.0
                val dieselEmission = data.child("Vehicle/dieselEmission").getValue(Double::class.java) ?: 0.0
                val unleadedEmission = data.child("Vehicle/unleadedEmission").getValue(Double::class.java) ?: 0.0
                val emissionData = UserInitialEmission(
                    houseEmission,
                    electricityEmission,
                    charcoalEmission,
                    lpgEmission,
                    firewoodEmission,
                    foodEmission,
                    meatEmission,
                    fishEmission,
                    crustaceansEmission,
                    mollusksEmission,
                    travelEmission,
                    tricycleEmission,
                    pujEmission,
                    epujEmission,
                    epujaEmission,
                    pubEmission,
                    pubaEmission,
                    pubeEmission,
                    taxiEmission,
                    uvexpressEmission,
                    vehicleEmission,
                    premiumEmission,
                    dieselEmission,
                    unleadedEmission)
                callback.invoke(emissionData)
            } else {
                val emissionData = UserInitialEmission(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                callback.invoke(emissionData)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }

    fun readUserVehicle(user: FirebaseUser, key: String, callback: (ReadUserVehicle?) -> Unit) {
        val userId = user.uid
        val database = FirebaseDatabase.getInstance().getReference("UserVehicle/$userId/$key")
        database.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val vehicleName = dataSnapshot.child("vehicleName").value.toString()
                val vehicleType = dataSnapshot.child("vehicleType").value.toString()
                val vehicleSize = dataSnapshot.child("vehicleSize").value.toString()
                val vehicleTransmission = dataSnapshot.child("vehicleTransmission").value.toString()
                val vehicleOdometer = dataSnapshot.child("vehicleOdometer").value.toString()

                val vehicle = ReadUserVehicle(vehicleName, vehicleType, vehicleTransmission, vehicleSize, vehicleOdometer)
                callback.invoke(vehicle)
            } else {
                // If the vehicle data doesn't exist at the specified index, invoke the callback with null
                callback.invoke(null)
            }
        }.addOnFailureListener { exception ->
            // Handle any errors that occurred while retrieving the vehicle data
            callback.invoke(null) // Invoke the callback with null in case of failure
        }
    }

    fun retrievePolygonFromDatabase(location: String, onComplete: (List<GeoPoint>) -> Unit) {
        val database = Firebase.database
        val reference = database.getReference("polygons/$location")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val geoPoints = mutableListOf<GeoPoint>()
                val sortedChildren = dataSnapshot.children.sortedBy { it.key?.toInt() }
                for (childSnapshot in sortedChildren) {
                    val latitude = childSnapshot.child("latitude").getValue(Double::class.java) ?: 0.0
                    val longitude = childSnapshot.child("longitude").getValue(Double::class.java) ?: 0.0
                    geoPoints.add(GeoPoint(latitude, longitude))
                }
                onComplete(geoPoints)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error case
            }
        })
    }

    fun checkUserProfile(user: FirebaseUser, callback: (ProfileChecker) -> Unit) {
        val userId = user.uid
        database = FirebaseDatabase.getInstance().getReference("UserProfile")
        database.child(userId).get().addOnSuccessListener {
            if (it.exists()) {
                val userProfile = ProfileChecker(
                    true
                )
                callback.invoke(userProfile)
            } else {
                val userProfile = ProfileChecker(
                    false
                )
                callback.invoke(userProfile)
            }
        }
    }

    fun checkInitialEmission(user: FirebaseUser, callback: (ProfileChecker) -> Unit) {
        val userId = user.uid
        database = FirebaseDatabase.getInstance().getReference("UserInitialEmission")
        database.child(userId).get().addOnSuccessListener {
            if (it.exists()) {
                val userProfile = ProfileChecker(
                    true
                )
                callback.invoke(userProfile)
            } else {
                val userProfile = ProfileChecker(
                    false
                )
                callback.invoke(userProfile)
            }
        }
    }

    fun readUserInfo(user: FirebaseUser, callback: (UserInfo) -> Unit) {
        val userId = user.uid
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(userId).get().addOnSuccessListener {
            if (it.exists()) {
                val email = it.child("email").value.toString()
                val firstName = it.child("firstName").value.toString()
                val lastName = it.child("lastName").value.toString()

                val userProfile = UserInfo(
                    email,
                    firstName,
                    lastName
                )
                callback.invoke(userProfile)
            } else {
                //Insert Default value here
            }
        }
    }

    fun readUserProfile(userId: String, callback: (UserProfile) -> Unit) {
        database = FirebaseDatabase.getInstance().getReference("UserProfile")
        database.child(userId).get().addOnSuccessListener {
            if (it.exists()) {
                val region = it.child("userRegion").value.toString()
                val province = it.child("userProvince").value.toString()
                val municipality = it.child("userMunicipality").value.toString()
                val houseMember = it.child("userHouseMember").value.toString().toInt()
                val carNumber = it.child("userCarNumber").value.toString().toInt()
                val userType = it.child("userType").value.toString()

                val userProfile = UserProfile(
                    region,
                    province,
                    municipality,
                    houseMember,
                    carNumber,
                    userType
                )
                callback.invoke(userProfile)
            } else {
                val userProfile = UserProfile(
                    "",
                    "",
                    "",
                    0,
                    0,
                    ""
                )
                callback.invoke(userProfile)
            }
        }
    }


    fun readUserVehicles(userId: String, onUserVehiclesReceived: (List<UserVehicle>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("UserVehicle").child(userId)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userVehicles = mutableListOf<UserVehicle>()
                dataSnapshot.children.forEach { carSnapshot ->
                    val vehicleKey = carSnapshot.key
                    val vehicleName = carSnapshot.child("vehicleName").getValue(String::class.java) ?: ""
                    val vehicleModel = carSnapshot.child("vehicleType").getValue(String::class.java) ?: ""
                    val vehicleSize = carSnapshot.child("vehicleSize").getValue(String::class.java) ?: ""
                    val vehicleTransmission = carSnapshot.child("vehicleTransmission").getValue(String::class.java) ?: ""
                    val vehicleFuel = carSnapshot.child("vehicleGasType").getValue(String::class.java) ?: ""
                    val vehicleEfficiency = carSnapshot.child("vehicleEfficiency").getValue(String::class.java) ?: ""
                    val vehicleOdometer = carSnapshot.child("vehicleOdometer").getValue(String::class.java) ?: ""
                    if(vehicleKey != null){
                        userVehicles.add(UserVehicle(
                            vehicleKey = vehicleKey,
                            vehicleName = vehicleName,
                            vehicleModel = vehicleModel,
                            vehicleTransmission = vehicleTransmission,
                            vehicleSize = vehicleSize,
                            fuelType =  vehicleFuel,
                            vehicleEfficiency =  vehicleEfficiency,
                            vehicleOdometer = vehicleOdometer
                        ))
                    }
                }
                onUserVehiclesReceived(userVehicles)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

    // Function to populate a 2D array with user vehicle data
    fun populateCarArray(userVehicles: List<UserVehicle>): Array<Array<String>> {
        val carArray = Array(userVehicles.size) { Array(8) { "" } }

        userVehicles.forEachIndexed { index, userVehicle ->
            carArray[index][0] = userVehicle.vehicleKey
            carArray[index][1] = userVehicle.vehicleName
            carArray[index][2] = userVehicle.vehicleModel
            carArray[index][3] = userVehicle.vehicleTransmission
            carArray[index][4] = userVehicle.vehicleSize
            carArray[index][5] = userVehicle.fuelType
            carArray[index][6] = userVehicle.vehicleEfficiency
            carArray[index][7] = userVehicle.vehicleOdometer
        }

        return carArray
    }

    fun createEmailUser(userData: RegistrationActivity.User): Boolean {

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(userData.email,userData.password).addOnSuccessListener {
        val user = firebaseAuth.currentUser

                firebaseAuth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        user?.let {
                            updateUserDatabase(it, userData.firstName, userData.lastName)
                        }
                    }

        }
        return false
    }

    private fun updateUserDatabase(user: FirebaseUser,firstName: String, lastName: String) {
        val userId = user.uid
        val firstName = firstName
        val lastName = lastName
        val email = user.email
        //Write user data to Realtime database
        val database = Firebase.database
        val usersRef = database.getReference("Users")

        //Create a new user entry using unique user id
        val userRef = usersRef.child(userId)

        //Set user data
        userRef.child("firstName").setValue(firstName)
        userRef.child("lastName").setValue(lastName)
        userRef.child("email").setValue(email)
    }


}