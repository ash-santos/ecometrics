package com.example.carbonfootprint.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProductPrices {
    private lateinit var database: DatabaseReference

    data class EmissionPriceForProvince(
        val electricityPrice: Double,
        val charcoalPrice: Double,
        val lpgPrice: Double,
        val unleadedGasPrice: Double,
        val premiumGasPrice: Double,
        val dieselGasPrice: Double
    )

    data class PUJPriceResult(
        val success: Boolean,
        val price: TransportationPublicUtilityJeepPrice?
    )

    data class TransportationTricyclePrice(
        val minimumPrice: Double,
        val ratePrice: Double,
        val disctMinimumPrice: Double,
        val disctRatePrice: Double
    )

    data class TransportationPublicUtilityJeepPrice(
        val minimumPrice: Double,
        val ratePrice: Double,
        val disctMinimumPrice: Double,
        val disctRatePrice: Double
    )

    data class TransportationElectricPublicUtilityJeepPrice(
        val minimumPrice: Double,
        val ratePrice: Double,
        val disctMinimumPrice: Double,
        val disctRatePrice: Double
    )

    data class TransportationElectricPublicUtilityJeepAirconPrice(
        val minimumPrice: Double,
        val ratePrice: Double,
        val disctMinimumPrice: Double,
        val disctRatePrice: Double
    )

    data class TransportationPublicUtilityBusPrice(
        val minimumPrice: Double,
        val ratePrice: Double,
        val disctMinimumPrice: Double,
        val disctRatePrice: Double
    )

    data class TransportationPublicUtilityBusEconomyPrice(
        val minimumPrice: Double,
        val ratePrice: Double,
        val disctMinimumPrice: Double,
        val disctRatePrice: Double
    )

    data class TransportationPublicUtilityBusAirconPrice(
        val minimumPrice: Double,
        val ratePrice: Double,
        val disctMinimumPrice: Double,
        val disctRatePrice: Double
    )

    data class TransportationUVExpressVanPrice(
    val minimumPrice: Double,
    val ratePrice: Double,
    val disctMinimumPrice: Double,
    val disctRatePrice: Double
    )

    data class TransportationUVExpressTaxiPrice(
        val minimumPrice: Double,
        val ratePrice: Double,
        val disctMinimumPrice: Double,
        val disctRatePrice: Double
    )

    fun readEmissionPriceForProvince(
        province: String,
        callback: (EmissionPriceForProvince) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("EmissionPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val electricityPrice = it.child("ElectricityPrice").value.toString().toDouble()
                val charcoalPrice = it.child("CharcoalPrice").value.toString().toDouble()
                val lpgPrice = it.child("LPGPrice").value.toString().toDouble()
                val unleadedGasPrice = it.child("UnleadedGasPrice").value.toString().toDouble()
                val premiumGasPrice = it.child("PremiumGasPrice").value.toString().toDouble()
                val dieselGasPrice = it.child("DieselGasPrice").value.toString().toDouble()

                val provinceData = EmissionPriceForProvince(
                    electricityPrice,
                    charcoalPrice,
                    lpgPrice,
                    unleadedGasPrice,
                    premiumGasPrice,
                    dieselGasPrice
                )
                callback.invoke(provinceData)
            } else {
                readEmissionDefault {
                    val provinceData = EmissionPriceForProvince(
                        electricityPrice = it.electricityPrice,
                        charcoalPrice = it.charcoalPrice,
                        lpgPrice = it.lpgPrice,
                        unleadedGasPrice = it.unleadedGasPrice,
                        premiumGasPrice = it.premiumGasPrice,
                        dieselGasPrice = it.dieselGasPrice
                    )
                    callback.invoke(provinceData)
                }
            }
        }
    }

    fun readEmissionDefault(
        callback: (EmissionPriceForProvince) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("EmissionPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val electricityPrice = it.child("ElectricityPrice").value.toString().toDouble()
                val charcoalPrice = it.child("CharcoalPrice").value.toString().toDouble()
                val lpgPrice = it.child("LPGPrice").value.toString().toDouble()
                val unleadedGasPrice = it.child("UnleadedGasPrice").value.toString().toDouble()
                val premiumGasPrice = it.child("PremiumGasPrice").value.toString().toDouble()
                val dieselGasPrice = it.child("DieselGasPrice").value.toString().toDouble()

                val provinceData = EmissionPriceForProvince(
                    electricityPrice,
                    charcoalPrice,
                    lpgPrice,
                    unleadedGasPrice,
                    premiumGasPrice,
                    dieselGasPrice
                )
                callback.invoke(provinceData)
            }
        }
    }

    fun readTricyclePriceForProvince(
        province: String,
        callback: (TransportationTricyclePrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("TricycleMinPrice").value.toString().toDouble()
                val ratePrice = it.child("TricycleRatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("TricycleDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("TricycleDisctRatePrice").value.toString().toDouble()

                val provinceTricyclePrice = TransportationTricyclePrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceTricyclePrice)
            } else {
                readTricyclePriceDefault() {
                    val provinceTricyclePrice = TransportationTricyclePrice(
                        minimumPrice = it.minimumPrice,
                        ratePrice = it.ratePrice,
                        disctMinimumPrice = it.disctMinimumPrice,
                        disctRatePrice = it.disctRatePrice
                    )
                    callback.invoke(provinceTricyclePrice)
                }
            }
        }
    }

    fun readTricyclePriceDefault(
        callback: (TransportationTricyclePrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("TricycleMinPrice").value.toString().toDouble()
                val ratePrice = it.child("TricyleRatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("TricyleDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("TricyleDisctRatePrice").value.toString().toDouble()

                val provinceTricyclePrice = TransportationTricyclePrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceTricyclePrice)
            }
        }
    }

    fun readUVExpressVanPriceForProvince(
        province: String,
        callback: (TransportationUVExpressVanPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("UVExpressVanMinPrice").value.toString().toDouble()
                val ratePrice = it.child("UVExpressVanRatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("UVExpressVanDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("UVExpressVanDisctRatePrice").value.toString().toDouble()

                val provinceUVVanPrice = TransportationUVExpressVanPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceUVVanPrice)
            } else {
                readUVExpressVanPriceDefault() {
                    val provinceUVVanPrice = TransportationUVExpressVanPrice(
                        minimumPrice = it.minimumPrice,
                        ratePrice = it.ratePrice,
                        disctMinimumPrice = it.disctMinimumPrice,
                        disctRatePrice = it.disctRatePrice
                    )
                    callback.invoke(provinceUVVanPrice)
                }
            }
        }
    }

    fun readUVExpressVanPriceDefault(
        callback: (TransportationUVExpressVanPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("UVExpressVanMinPrice").value.toString().toDouble()
                val ratePrice = it.child("UVExpressVanRatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("UVExpressVanDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("UVExpressVanDisctRatePrice").value.toString().toDouble()

                val provinceUVVanPrice = TransportationUVExpressVanPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceUVVanPrice)
            }
        }
    }

    fun readUVExpressTaxiPriceForProvince(
        province: String,
        callback: (TransportationUVExpressTaxiPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("UVExpressTaxiMinPrice").value.toString().toDouble()
                val ratePrice = it.child("UVExpressTaxiRatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("UVExpressTaxiDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("UVExpressTaxiDisctRatePrice").value.toString().toDouble()

                val provinceUVTaxiPrice = TransportationUVExpressTaxiPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceUVTaxiPrice)
            } else {
                readUVExpressTaxiPriceDefault() {
                    val provinceUVTaxiPrice = TransportationUVExpressTaxiPrice(
                        minimumPrice = it.minimumPrice,
                        ratePrice = it.ratePrice,
                        disctMinimumPrice = it.disctMinimumPrice,
                        disctRatePrice = it.disctRatePrice
                    )
                    callback.invoke(provinceUVTaxiPrice)
                }
            }
        }
    }

    fun readUVExpressTaxiPriceDefault(
        callback: (TransportationUVExpressTaxiPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("UVExpressTaxiMinPrice").value.toString().toDouble()
                val ratePrice = it.child("UVExpressTaxiRatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("UVExpressTaxiDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("UVExpressTaxiDisctRatePrice").value.toString().toDouble()

                val provinceUVTaxiPrice = TransportationUVExpressTaxiPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceUVTaxiPrice)
            }
        }
    }

    fun readPUJPriceForProvince(
        province: String,
        callback: (TransportationPublicUtilityJeepPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("PUJMinPrice").value.toString().toDouble()
                val ratePrice = it.child("PUJRatePrice").value.toString().toDouble()
                val disctMinimumPrice = it.child("PUJDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("PUJDisctRatePrice").value.toString().toDouble()

                val provincePUJPrice = TransportationPublicUtilityJeepPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provincePUJPrice)
            } else {
                readPUJPriceDefault {
                    val provincePUJPrice = TransportationPublicUtilityJeepPrice(
                        minimumPrice = it.minimumPrice,
                        ratePrice = it.ratePrice,
                        disctMinimumPrice = it.disctMinimumPrice,
                        disctRatePrice = it.disctRatePrice
                    )
                    callback.invoke(provincePUJPrice)
                }
            }
        }
    }
    fun readPUJPriceDefault(
        callback: (TransportationPublicUtilityJeepPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("PUJMinPrice").value.toString().toDouble()
                val ratePrice = it.child("PUJRatePrice").value.toString().toDouble()
                val disctMinimumPrice = it.child("PUJDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("PUJDisctRatePrice").value.toString().toDouble()

                val provincePUJPrice = TransportationPublicUtilityJeepPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provincePUJPrice)
            }
        }
    }

    fun readEPUJPriceForProvince(
        province: String,
        callback: (TransportationElectricPublicUtilityJeepPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("EPUJMinPrice").value.toString().toDouble()
                val ratePrice = it.child("EPUJRatePrice").value.toString().toDouble()
                val disctMinimumPrice = it.child("EPUJDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("EPUJDisctRatePrice").value.toString().toDouble()

                val provinceEPUJPrice = TransportationElectricPublicUtilityJeepPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceEPUJPrice)
            } else {
                readEPUJPriceDefault() {
                    val provinceEPUJPrice = TransportationElectricPublicUtilityJeepPrice(
                        minimumPrice = it.minimumPrice,
                        ratePrice = it.ratePrice,
                        disctMinimumPrice = it.disctMinimumPrice,
                        disctRatePrice = it.disctRatePrice
                    )
                    callback.invoke(provinceEPUJPrice)
                }
            }
        }
    }

    fun readEPUJPriceDefault(
        callback: (TransportationElectricPublicUtilityJeepPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("EPUJMinPrice").value.toString().toDouble()
                val ratePrice = it.child("EPUJRatePrice").value.toString().toDouble()
                val disctMinimumPrice = it.child("EPUJDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("EPUJDisctRatePrice").value.toString().toDouble()

                val provinceEPUJPrice = TransportationElectricPublicUtilityJeepPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceEPUJPrice)
            }
        }
    }


    fun readEPUJAPriceForProvince(
        province: String,
        callback: (TransportationElectricPublicUtilityJeepAirconPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("EPUJAMinPrice").value.toString().toDouble()
                val ratePrice = it.child("EPUJARatePrice").value.toString().toDouble()
                val disctMinimumPrice = it.child("EPUJADisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("EPUJADisctRatePrice").value.toString().toDouble()

                val provinceEPUJAPrice = TransportationElectricPublicUtilityJeepAirconPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceEPUJAPrice)
            } else {
                readEPUJAPriceDefault() {
                    val provinceEPUJAPrice = TransportationElectricPublicUtilityJeepAirconPrice(
                        minimumPrice = it.minimumPrice,
                        ratePrice = it.ratePrice,
                        disctMinimumPrice = it.disctMinimumPrice,
                        disctRatePrice = it.disctRatePrice
                    )
                    callback.invoke(provinceEPUJAPrice)
                }
            }
        }
    }

    fun readEPUJAPriceDefault(
        callback: (TransportationElectricPublicUtilityJeepPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("EPUJMinPrice").value.toString().toDouble()
                val ratePrice = it.child("EPUJRatePrice").value.toString().toDouble()
                val disctMinimumPrice = it.child("EPUJDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("EPUJDisctRatePrice").value.toString().toDouble()

                val provinceEPUJAPrice = TransportationElectricPublicUtilityJeepPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provinceEPUJAPrice)
            }
        }
    }

    fun readPUBPriceForProvince(
        province: String,
        callback: (TransportationPublicUtilityBusPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("PUBMinPrice").value.toString().toDouble()
                val ratePrice = it.child("PUBRatePrice").value.toString().toDouble()
                val disctMinimumPrice = it.child("PUBDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("PUBDisctRatePrice").value.toString().toDouble()

                val provincePUBPrice = TransportationPublicUtilityBusPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provincePUBPrice)
            } else {
                readPUBPriceDefault() {
                    val provincePUBPrice = TransportationPublicUtilityBusPrice(
                        minimumPrice = it.minimumPrice,
                        ratePrice = it.ratePrice,
                        disctMinimumPrice = it.disctMinimumPrice,
                        disctRatePrice = it.disctRatePrice
                    )
                    callback.invoke(provincePUBPrice)
                }
            }
        }
    }

    fun readPUBPriceDefault(
        callback: (TransportationPublicUtilityBusPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("PUBMinPrice").value.toString().toDouble()
                val ratePrice = it.child("PUBRatePrice").value.toString().toDouble()
                val disctMinimumPrice = it.child("PUBDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("PUBDisctRatePrice").value.toString().toDouble()

                val provincePUBPrice = TransportationPublicUtilityBusPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provincePUBPrice)
            }
        }
    }

    fun readPUBEPriceForProvince(
        province: String,
        callback: (TransportationPublicUtilityBusEconomyPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("PUBEMinPrice").value.toString().toDouble()
                val ratePrice = it.child("PUBERatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("PUBEDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("PUBEDisctRatePrice").value.toString().toDouble()

                val provincePUBEPrice = TransportationPublicUtilityBusEconomyPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provincePUBEPrice)
            } else {
                readPUBEPriceDefault() {
                    val provincePUBEPrice = TransportationPublicUtilityBusEconomyPrice(
                        minimumPrice = it.minimumPrice,
                        ratePrice = it.ratePrice,
                        disctMinimumPrice = it.disctMinimumPrice,
                        disctRatePrice = it.disctRatePrice
                    )
                    callback.invoke(provincePUBEPrice)
                }
            }
        }
    }

    fun readPUBEPriceDefault(
        callback: (TransportationPublicUtilityBusEconomyPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("PUBEMinPrice").value.toString().toDouble()
                val ratePrice = it.child("PUBERatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("PUBEDisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("PUBEDisctRatePrice").value.toString().toDouble()

                val provincePUBEPrice = TransportationPublicUtilityBusEconomyPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provincePUBEPrice)
            }
        }
    }

    fun readPUBAPriceForProvince(
        province: String,
        callback: (TransportationPublicUtilityBusAirconPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child(province).get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("PUBAMinPrice").value.toString().toDouble()
                val ratePrice = it.child("PUBARatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("PUBADisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("PUBADisctRatePrice").value.toString().toDouble()

                val provincePUBAPrice = TransportationPublicUtilityBusAirconPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provincePUBAPrice)
            } else {
                readPUBAPriceDefault() {
                    val provincePUBAPrice = TransportationPublicUtilityBusAirconPrice(
                        minimumPrice = it.minimumPrice,
                        ratePrice = it.ratePrice,
                        disctMinimumPrice = it.disctMinimumPrice,
                        disctRatePrice = it.disctRatePrice
                    )
                    callback.invoke(provincePUBAPrice)
                }
            }
        }
    }

    fun readPUBAPriceDefault(
        callback: (TransportationPublicUtilityBusAirconPrice) -> Unit
    ) {
        database = FirebaseDatabase.getInstance().getReference("TransportationPriceList")
        database.child("Choose Province").get().addOnSuccessListener {
            if (it.exists()) {
                val minimumPrice = it.child("PUBAMinPrice").value.toString().toDouble()
                val ratePrice = it.child("PUBARatePrice").value.toString().toDouble()
                val disctMinimumPrice =
                    it.child("PUBADisctMinPrice").value.toString().toDouble()
                val disctRatePrice = it.child("PUBADisctRatePrice").value.toString().toDouble()

                val provincePUBAPrice = TransportationPublicUtilityBusAirconPrice(
                    minimumPrice,
                    ratePrice,
                    disctMinimumPrice,
                    disctRatePrice
                )
                callback.invoke(provincePUBAPrice)
            }
        }
    }

}