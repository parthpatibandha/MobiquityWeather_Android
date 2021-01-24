package org.android.application.presentation.utility

class AppConstant {

    companion object {

        const val PageStart = 0
        const val No = "N"
        const val Yes = "Y"


        //For temperature in Fahrenheit use units=imperial
        //For temperature in Celsius use units=metric
        const val UnitMetric = "metric"
        const val UniImperial = "imperial"

        const val PHOTO_1 = "http://openweathermap.org/img/wn/"
        const val PHOTO_2 = ".png"

        const val Location = "47.6204,-122.3491"
        const val radius = "2500"
        const val type = "restaurant"

        //intent
        const val INTENT_PERMISSION_SETTING = 104


        //Database info
        const val DB_NAME = "capermint_db"
        const val TABLE_NAME_CITY = "cities"


        const val FiveDays = "5"


        const val id = "id"
        const val name = "name"
        const val lat = "lat"
        const val lng = "lng"

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