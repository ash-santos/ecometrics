package com.example.carbonfootprint.database

class FoodData {
    data class Category(val id: Int, val value: String)
    data class Food(val categoryId: Int, val id: Int, val value: String)
    data class ID(val categoryId: Int, val foodId: Int, val value: String)

    val categories = arrayOf(
        Category(1, "Meat"),
        Category(2, "Fish"),
        Category(3, "Crustaceans"),
        Category(4, "Mollusks")
    )

    val foods = arrayOf(
        Food(1, 1, "Beef"),
        Food(1, 2, "Pork"),
        Food(1, 3, "Chicken"),
        Food(2, 1, "Tilapia"),
        Food(2, 2, "Bangus"),
        Food(2, 3, "Galunggong"),
        Food(2, 4, "Lapu-Lapu"),
        Food(2, 5, "Fish Tamban"),
        Food(2, 6, "Bisugo"),
        Food(2, 7, "Talakitok"),
        Food(2, 8, "Hasa-Hasa"),
        Food(3, 1, "Shrimp"),
        Food(3, 2, "Crab"),
        Food(3, 3, "Lobster"),
        Food(4, 1, "Tulya"),
        Food(4, 2, "Tahong"),
        Food(4, 3, "Talaba"),
        Food(4, 4, "Tahung Tahung"),
        Food(4, 5, "Snail Tamban"),
        Food(4, 6, "Snail Gisado"),
        Food(4, 7, "Pusit"),
        Food(4, 8, "Lupon")
    )

    val IDs = arrayOf(
        ID(1, 1, "Beef"),
        ID(1, 2, "Pork"),
        ID(1, 3, "Chicken"),
        ID(2, 1, "Tilapia"),
        ID(2, 2, "Bangus"),
        ID(2, 3, "Galunggong"),
        ID(2, 4, "LapuLapu"),
        ID(2, 5, "FishTamban"),
        ID(2, 6, "Bisugo"),
        ID(2, 7, "Talakitok"),
        ID(2, 8, "HasaHasa"),
        ID(3, 1, "Shrimp"),
        ID(3, 2, "Crab"),
        ID(3, 3, "Lobster"),
        ID(4, 1, "Tulya"),
        ID(4, 2, "Tahong"),
        ID(4, 3, "Talaba"),
        ID(4, 4, "TahungTahung"),
        ID(4, 5, "SnailTamban"),
        ID(4, 6, "SnailGisado"),
        ID(4, 7, "Pusit"),
        ID(4, 8, "Lupon")
        )


    fun getCategoryData(): List<String> {
        return categories.map { it.value }
    }

    fun getFoodData(categoryName: String): List<String> {
        val categoryIndex = categories.find { it.value == categoryName }?.id

        return foods
            .filter { it.categoryId == categoryIndex }
            .map { it.value }
    }

    fun getIDData(categoryName: String, foodName: String): String? {
        val categoryIndex = categories.find { it.value == categoryName }?.id
        val foodIndex = foods.find { it.categoryId == categoryIndex && it.value == foodName }?.id

        return IDs
            .firstOrNull { it.categoryId == categoryIndex && it.foodId == foodIndex }
            ?.value
    }

}