package com.example.carbonfootprint.database

class VehicleData {
    data class Model(val id: Int, val value: String)
    data class Transmission(val modelId: Int, val id: Int, val value: String)
    data class Size(val modelId: Int,val transmissionId: Int, val id: Int, val value: String)
    data class Capacity(val modelId: Int,val transmissionId: Int, val sizeId: Int, val value: Int)
    data class Efficiency(val modelId: Int,val transmissionId: Int, val sizeId: Int, val value: Double)

    val models = arrayOf(
        Model(1, "Sedan"),
        Model(2, "Pickups"),
        Model(3, "Light Truck"),
        Model(4, "Heavy Truck"),
        Model(5, "Hatchbacks"),
        Model(6, "Sport Utility Vehicles (SUVs)"),
        Model(7, "Crossovers"),
        Model(8, "Wagons"),
        Model(9, "Motorcycle"),
        Model(10, "Minivans"),
        Model(11, "Coupes"),
        Model(12, "Convertibles"),
        Model(13, "Tricycle")
    )

    val transmissions = arrayOf(
        Transmission(1, 1, "Manual"),
        Transmission(1, 2, "Automatic"),
        Transmission(2, 1, "Manual"),
        Transmission(2, 2, "Automatic"),
        Transmission(3, 1, "Manual"),
        Transmission(3, 2, "Automatic"),
        Transmission(4, 1, "Manual"),
        Transmission(4, 2, "Automatic"),
        Transmission(5, 1, "Manual"),
        Transmission(5, 2, "Automatic"),
        Transmission(6, 1, "Manual"),
        Transmission(6, 2, "Automatic"),
        Transmission(7, 1, "Manual"),
        Transmission(7, 2, "Automatic"),
        Transmission(8, 1, "Manual"),
        Transmission(8, 2, "Automatic"),
        Transmission(9, 1, "Manual"),
        Transmission(10, 1, "Manual"),
        Transmission(10, 2, "Automatic"),
        Transmission(11, 1, "Manual"),
        Transmission(11, 2, "Automatic"),
        Transmission(12, 1, "Manual"),
        Transmission(12, 2, "Automatic"),
        Transmission(13, 1, "Manual")
    )

    val sizes = arrayOf(
        Size(1, 1, 1, "Subcompact"),
        Size(1, 1, 2, "Compact"),
        Size(1, 1, 3, "Mid-size"),
        Size(1, 1, 4, "Full-size"),
        Size(1, 2, 1, "Subcompact"),
        Size(1, 2, 2, "Compact"),
        Size(1, 2, 3, "Mid-size"),
        Size(1, 2, 4, "Full-size"),
        Size(2, 1, 1, "Small"),
        Size(2, 2, 1, "Small"),
        Size(2, 1, 2, "Mid-size"),
        Size(2, 2, 2, "Mid-size"),
        Size(2, 1, 3, "Full-size"),
        Size(2, 2, 3, "Full-size"),
        Size(3, 1, 1, "Compact"),
        Size(3, 1, 2, "Mid-size"),
        Size(3, 2, 1, "Compact"),
        Size(3, 2, 2, "Mid-size"),
        Size(4, 1, 1, "Heavy-duty"),
        Size(4, 1, 2, "Semitruck"),
        Size(4, 2, 1, "Heavy-duty"),
        Size(4, 2, 2, "Semitruck"),
        Size(5, 1, 1, "Normal"),
        Size(5, 2, 1, "Normal"),
        Size(6, 2, 1, "Compact"),
        Size(6, 2, 2, "Mid-size"),
        Size(6, 2, 3, "Large"),
        Size(6, 1, 1, "Compact"),
        Size(6, 1, 2, "Mid-size"),
        Size(7, 2, 1, "Compact"),
        Size(7, 2, 2, "Mid-size"),
        Size(7, 1, 1, "Compact"),
        Size(7, 1, 2, "Mid-size"),
        Size(8, 1, 1, "Small"),
        Size(8, 2, 1, "Small"),
        Size(8, 1, 2, "Mid-size"),
        Size(8, 2, 2, "Mid-size"),
        Size(8, 1, 3, "Large"),
        Size(8, 2, 3, "Large"),
        Size(9, 1, 1, "125cc - 250cc"),
        Size(9, 1, 2, "300cc - 500cc"),
        Size(9, 1, 3, "600cc+"),
        Size(10, 1, 1, "Compact"),
        Size(10, 2, 1, "Compact"),
        Size(10, 1, 2, "Large"),
        Size(10, 2, 2, "Large"),
        Size(11, 1, 1, "Small"),
        Size(11, 2, 1, "Small"),
        Size(11, 1, 2, "Mid-size"),
        Size(11, 2, 2, "Mid-size"),
        Size(11, 1, 3, "Large"),
        Size(11, 2, 3, "Large"),
        Size(12, 1, 1, "Small"),
        Size(12, 2, 1, "Small"),
        Size(12, 1, 2, "Mid-size"),
        Size(12, 2, 2, "Mid-size"),
        Size(12, 1, 3, "Large"),
        Size(12, 2, 3, "Large"),
        Size(13, 1, 1, "125cc - 250cc"),
        Size(13, 1, 2, "300cc - 500cc"),
        Size(13, 1, 3, "600cc+")
    )

    val efficiencies = arrayOf(
        Efficiency(1, 1, 1, 0.0588235294117647),
        Efficiency(1, 1, 2, 0.0540540540540541),
        Efficiency(1, 1, 3, 0.05),
        Efficiency(1, 1, 4, 0.0555555555555556),
        Efficiency(1, 2, 1, 0.0666666666666667),
        Efficiency(1, 2, 2, 0.0606060606060606),
        Efficiency(1, 2, 3, 0.0571428571428571),
        Efficiency(1, 2, 4, 0.0625),
        Efficiency(2, 1, 1, 0.1),
        Efficiency(2, 2, 1, 0.117647058823529),
        Efficiency(2, 1, 2, 0.117647058823529),
        Efficiency(2, 2, 2, 0.133333333333333),
        Efficiency(2, 1, 3, 0.142857142857143),
        Efficiency(2, 2, 3, 0.166666666666667),
        Efficiency(3, 1, 1, 0.117647058823529),
        Efficiency(3, 1, 2, 0.142857142857143),
        Efficiency(3, 2, 1, 0.142857142857143),
        Efficiency(3, 2, 2, 0.166666666666667),
        Efficiency(4, 1, 1, 0.2),
        Efficiency(4, 1, 2, 0.25),
        Efficiency(4, 2, 1, 0.25),
        Efficiency(4, 2, 2, 0.333333333333333),
        Efficiency(5, 1, 1, 0.0465116279069767),
        Efficiency(5, 2, 1, 0.0526315789473684),
        Efficiency(6, 2, 1, 0.105263157894737),
        Efficiency(6, 2, 2, 0.117647058823529),
        Efficiency(6, 2, 3, 0.142857142857143),
        Efficiency(6, 1, 1, 0.0952380952380952),
        Efficiency(6, 1, 2, 0.105263157894737),
        Efficiency(7, 2, 1, 0.0571428571428571),
        Efficiency(7, 2, 2, 0.1),
        Efficiency(7, 1, 1, 0.0512820512820513),
        Efficiency(7, 1, 2, 0.0588235294117647),
        Efficiency(8, 1, 1, 0.0625),
        Efficiency(8, 2, 1, 0.0714285714285714),
        Efficiency(8, 1, 2, 0.0714285714285714),
        Efficiency(8, 2, 2, 0.0833333333333333),
        Efficiency(8, 1, 3, 0.0952380952380952),
        Efficiency(8, 2, 3, 0.117647058823529),
        Efficiency(9, 1, 1, 0.025),
        Efficiency(9, 1, 2, 0.0363636363636364),
        Efficiency(9, 1, 3, 0.05),
        Efficiency(10, 1, 1, 0.0740740740740741),
        Efficiency(10, 2, 1, 0.0869565217391304),
        Efficiency(10, 1, 2, 0.1),
        Efficiency(10, 2, 2, 0.111111111111111),
        Efficiency(11, 1, 1, 0.0666666666666667),
        Efficiency(11, 2, 1, 0.0769230769230769),
        Efficiency(11, 1, 2, 0.0952380952380952),
        Efficiency(11, 2, 2, 0.111111111111111),
        Efficiency(11, 1, 3, 0.142857142857143),
        Efficiency(11, 2, 3, 0.166666666666667),
        Efficiency(12, 1, 1, 0.0625),
        Efficiency(12, 2, 1, 0.0714285714285714),
        Efficiency(12, 1, 2, 0.0714285714285714),
        Efficiency(12, 2, 2, 0.0833333333333333),
        Efficiency(12, 1, 3, 0.1),
        Efficiency(12, 2, 3, 0.117647058823529),
        Efficiency(13, 1, 1, 0.025),
        Efficiency(13, 1, 2, 0.025),
        Efficiency(13, 1, 3, 0.025)
    )

    val capacities = arrayOf(
        Capacity(1, 1, 1, 4),
        Capacity(1, 1, 2, 5),
        Capacity(1, 1, 3, 5),
        Capacity(1, 1, 4, 7),
        Capacity(1, 2, 1, 4),
        Capacity(1, 2, 2, 5),
        Capacity(1, 2, 3, 5),
        Capacity(1, 2, 4, 7),
        Capacity(2, 1, 1, 5),
        Capacity(2, 2, 1, 5),
        Capacity(2, 1, 2, 6),
        Capacity(2, 2, 2, 6),
        Capacity(2, 1, 3, 7),
        Capacity(2, 2, 3, 7),
        Capacity(3, 1, 1, 5),
        Capacity(3, 1, 2, 6),
        Capacity(3, 2, 1, 5),
        Capacity(3, 2, 2, 6),
        Capacity(4, 1, 1, 5),
        Capacity(4, 1, 2, 6),
        Capacity(4, 2, 1, 5),
        Capacity(4, 2, 2, 6),
        Capacity(5, 1, 1, 5),
        Capacity(5, 2, 1, 5),
        Capacity(6, 2, 1, 7),
        Capacity(6, 2, 2, 8),
        Capacity(6, 2, 3, 9),
        Capacity(6, 1, 1, 7),
        Capacity(6, 1, 2, 8),
        Capacity(7, 2, 1, 5),
        Capacity(7, 2, 2, 7),
        Capacity(7, 1, 1, 5),
        Capacity(7, 1, 2, 7),
        Capacity(8, 1, 1, 5),
        Capacity(8, 2, 1, 5),
        Capacity(8, 1, 2, 7),
        Capacity(8, 2, 2, 7),
        Capacity(8, 1, 3, 8),
        Capacity(8, 2, 3, 8),
        Capacity(9, 1, 1, 2),
        Capacity(9, 1, 2, 2),
        Capacity(9, 1, 3, 2),
        Capacity(10, 1, 1, 7),
        Capacity(10, 2, 1, 7),
        Capacity(10, 1, 2, 8),
        Capacity(10, 2, 2, 8),
        Capacity(11, 1, 1, 3),
        Capacity(11, 2, 1, 3),
        Capacity(11, 1, 2, 4),
        Capacity(11, 2, 2, 4),
        Capacity(11, 1, 3, 4),
        Capacity(11, 2, 3, 4),
        Capacity(12, 1, 1, 2),
        Capacity(12, 2, 1, 2),
        Capacity(12, 1, 2, 4),
        Capacity(12, 2, 2, 4),
        Capacity(12, 1, 3, 5),
        Capacity(12, 2, 3, 5),
        Capacity(13, 1, 1, 4),
        Capacity(13, 1, 2, 4),
        Capacity(13, 1, 3, 4)
    )



    fun getModelData(): List<String> {
        return models.map { it.value }
    }

    fun getTransmissionData(modelName: String): List<String> {
        val modelIndex = models.find { it.value == modelName}?.id

        return transmissions
            .filter { it.modelId == modelIndex}
            .map { it.value }
    }

    fun getSizeData(modelName: String, transmissionName: String): List<String> {
        val modelIndex = models.find { it.value == modelName }?.id
        val transmissionIndex = transmissions.find { it.modelId == modelIndex && it.value == transmissionName }?.id

        return sizes
            .filter { it.modelId == modelIndex && it.transmissionId == transmissionIndex }
            .map { it.value }
    }

    fun getCapacityData(modelName: String, transmissionName: String, sizeName: String): Int? {
        val modelIndex = models.find { it.value == modelName }?.id
        val transmissionIndex = transmissions.find { it.modelId == modelIndex && it.value == transmissionName }?.id
        val sizeIndex = sizes.find { it.modelId == modelIndex && it.transmissionId == transmissionIndex && it.value == sizeName }?.id

        println("$modelIndex, $transmissionIndex, $sizeIndex")

        return capacities
            .firstOrNull { it.modelId == modelIndex && it.transmissionId == transmissionIndex && it.sizeId == sizeIndex }
            ?.value
    }

    fun getEfficiencyData(modelName: String, transmissionName: String, sizeName: String): Double? {
        val modelIndex = models.find { it.value == modelName }?.id
        val transmissionIndex = transmissions.find { it.modelId == modelIndex && it.value == transmissionName }?.id
        val sizeIndex = sizes.find { it.modelId == modelIndex && it.transmissionId == transmissionIndex && it.value == sizeName }?.id

        return efficiencies
            .firstOrNull { it.modelId == modelIndex && it.transmissionId == transmissionIndex && it.sizeId == sizeIndex }
            ?.value
    }
}