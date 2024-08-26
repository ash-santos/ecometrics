package com.example.carbonfootprint

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carbonfootprint.CalculationFun.EmissionLevelIdentifier
import com.example.carbonfootprint.adapter.DataOffsetModel
import com.example.carbonfootprint.adapter.OffsetActivityAdapter
import com.example.carbonfootprint.database.FirebaseQuery
import com.example.carbonfootprint.databinding.RecommendationsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class RecommendationActivity : AppCompatActivity(), OffsetActivityAdapter.OnItemClickListener {

    private lateinit var binding: RecommendationsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecommendationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val user = auth.currentUser

        val context = applicationContext

        val recyclerOffsetView = binding.recyclerOffsetView
        val layoutOffsetManager = LinearLayoutManager(this)
        recyclerOffsetView.layoutManager = layoutOffsetManager
        val offsetList = ArrayList<DataOffsetModel>()

        FirebaseQuery().readOffsetActivity() { offsetActivity ->
            var offsetActivityArray = FirebaseQuery().populateOffsetArray(offsetActivity)
            for (activity in offsetActivityArray) {
                offsetList.add(DataOffsetModel(activity[1], activity[2], activity[3]))
            }

            val adapter = OffsetActivityAdapter(offsetList, this, this@RecommendationActivity)
            recyclerOffsetView.adapter = adapter
        }


        user?.let {
            FirebaseQuery().readUserInitialEmission(it) { emission ->
                val electricityLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "House",
                    "Electricity",
                    emission.electricityEmission.toFloat(),
                    context
                )
                val lpgLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "House",
                    "LPG",
                    emission.lpgEmission.toFloat(),
                    context
                )
                val charcoalLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "House",
                    "Charcoal",
                    emission.charcoalEmission.toFloat(),
                    context
                )
                val firewoodLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "House",
                    "Firewood",
                    emission.firewoodEmission.toFloat(),
                    context
                )

                if (electricityLevel || lpgLevel || charcoalLevel || firewoodLevel) {
                    binding.noEmission.visibility = View.GONE
                    binding.txtRecommendation.visibility = View.VISIBLE
                    binding.HouseRecommendationField.visibility = View.VISIBLE
                    if (electricityLevel) {
                        binding.rowElectricityRecommendation.visibility = View.VISIBLE
                        fadeInAnimation(binding.rowElectricityRecommendation1)
                        fadeInAnimation(binding.rowElectricityRecommendation2)
                    }
                    if (charcoalLevel) {
                        binding.rowCharcoalRecommendation.visibility = View.VISIBLE
                        fadeInAnimation(binding.rowCharcoalRecommendation1)
                        fadeInAnimation(binding.rowCharcoalRecommendation2)
                    }
                    if (lpgLevel) {
                        binding.rowLPGRecommendation.visibility = View.VISIBLE
                        fadeInAnimation(binding.rowLPGRecommendation1)
                        fadeInAnimation(binding.rowLPGRecommendation2)
                    }
                    if (firewoodLevel) {
                        binding.rowFirewoodRecommendation.visibility = View.VISIBLE
                        fadeInAnimation(binding.rowFirewoodRecommendation1)
                        fadeInAnimation(binding.rowFirewoodRecommendation2)
                    }
                }

                val meatLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Food",
                    "Meat",
                    emission.meatEmission.toFloat(),
                    context
                )
                val fishLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Food",
                    "Fish",
                    emission.fishEmission.toFloat(),
                    context
                )
                val crustaceansLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Food",
                    "Crustaceans",
                    emission.crustaceansEmission.toFloat(),
                    context
                )
                val mollusksLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Food",
                    "Mollusks",
                    emission.mollusksEmission.toFloat(),
                    context
                )

                if (meatLevel || fishLevel || crustaceansLevel || mollusksLevel) {
                    binding.noEmission.visibility = View.GONE
                    binding.txtRecommendation.visibility = View.VISIBLE
                    binding.FoodRecommendationField.visibility = View.VISIBLE
                    if (meatLevel) {
                        binding.rowMeatRecommendation.visibility = View.VISIBLE
                        fadeInAnimation(binding.rowMeatRecommendation1)
                        fadeInAnimation(binding.rowMeatRecommendation2)
                    }
                    if (fishLevel) {
                        binding.rowFishRecommendation.visibility = View.VISIBLE
                        fadeInAnimation(binding.rowFishRecommendation1)
                        fadeInAnimation(binding.rowFishRecommendation2)
                    }
                    if (crustaceansLevel) {
                        binding.rowCrustaceansRecommendation.visibility = View.VISIBLE
                        fadeInAnimation(binding.rowCrustaceansRecommendation1)
                        fadeInAnimation(binding.rowCrustaceansRecommendation2)
                    }
                    if (mollusksLevel) {
                        binding.rowMollusksRecommendation.visibility = View.VISIBLE
                        fadeInAnimation(binding.rowMollusksRecommendation1)
                        fadeInAnimation(binding.rowMollusksRecommendation2)
                    }
                }

                val dieselLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Vehicle",
                    "Diesel",
                    emission.dieselEmission.toFloat(),
                    context
                )
                val unleadedLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Vehicle",
                    "Unleaded",
                    emission.unleadedEmission.toFloat(),
                    context
                )
                val premiumLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Vehicle",
                    "Premium",
                    emission.premiumEmission.toFloat(),
                    context
                )

                if (dieselLevel || unleadedLevel || premiumLevel) {
                    binding.noEmission.visibility = View.GONE
                    binding.txtRecommendation.visibility = View.VISIBLE
                    binding.VehicleRecommendationField.visibility = View.VISIBLE
                    binding.rowVehicleRecommendation.visibility = View.VISIBLE
                    fadeInAnimation(binding.rowVehicleRecommendation1)
                    fadeInAnimation(binding.rowVehicleRecommendation2)
                }

                val tricycleLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Travel",
                    "Tricycle",
                    emission.tricycleEmission.toFloat(),
                    context
                )
                val pujLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Travel",
                    "Jeep",
                    emission.pujEmission.toFloat(),
                    context
                )
                val epujLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Travel",
                    "E-Jeep",
                    emission.epujEmission.toFloat(),
                    context
                )
                val epujaLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Travel",
                    "E-Jeep w/ Aircon",
                    emission.epujaEmission.toFloat(),
                    context
                )
                val pubLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Travel",
                    "Bus",
                    emission.pubEmission.toFloat(),
                    context
                )
                val pubeLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Travel",
                    "Economy Bus",
                    emission.pubeEmission.toFloat(),
                    context
                )
                val pubaLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Travel",
                    "Bus w/ Aircon",
                    emission.pubaEmission.toFloat(),
                    context
                )
                val taxiLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Travel",
                    "Taxi",
                    emission.taxiEmission.toFloat(),
                    context
                )
                val uvexpressLevel = EmissionLevelIdentifier().emissionLevelIdentifier(
                    "Travel",
                    "UV Express",
                    emission.uvexpressEmission.toFloat(),
                    context
                )

                if (tricycleLevel || pujLevel || epujLevel || epujaLevel || pubLevel || pubaLevel || pubeLevel || taxiLevel || uvexpressLevel) {
                    binding.noEmission.visibility = View.GONE
                    binding.txtRecommendation.visibility = View.VISIBLE
                    binding.TravelRecommendationField.visibility = View.VISIBLE
                    binding.rowTransportationRecommendation.visibility = View.VISIBLE
                    fadeInAnimation(binding.rowTransportationRecommendation1)
                    fadeInAnimation(binding.rowTransportationRecommendation2)
                }
            }
        }


        binding.btnToMainMenu.setOnClickListener {
            if (binding.btnToMainMenu.text == "NEXT") {
                binding.btnToMainMenu.text = "To Main Menu"
                binding.tblTitleActivity.visibility = View.VISIBLE
                binding.tblRecommendation.visibility = View.GONE
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

    override fun onItemClick(position: Int) {

    }
}