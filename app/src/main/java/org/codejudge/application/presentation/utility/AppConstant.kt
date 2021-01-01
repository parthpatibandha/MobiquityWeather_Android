package org.codejudge.application.presentation.utility

class AppConstant {

    companion object {

        const val PageStart = 0
        const val No = "N"
        const val Yes = "Y"

        const val PHOTO_1 =
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
        const val PHOTO_2 = "&sensor=false&key="

        const val Location = "47.6204,-122.3491"
        const val radius = "2500"
        const val type = "restaurant"

        const val PriceLevelFree = "Free"
        const val PriceLevelInexpensive = "Inexpensive"
        const val PriceLevelModerate = "Moderate"
        const val PriceLevelExpensive = "Expensive"
        const val PriceLevelVery = "Very Expensive"

        fun getPriceRangeFromLevel(priceLevel: Int): String {
            return when (priceLevel) {
                0 -> PriceLevelFree
                1 -> PriceLevelInexpensive
                2 -> PriceLevelModerate
                3 -> PriceLevelExpensive
                4 -> PriceLevelVery
                else -> PriceLevelFree
            }
        }

    }
}