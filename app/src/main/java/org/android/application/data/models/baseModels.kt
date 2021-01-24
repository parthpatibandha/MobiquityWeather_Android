package org.android.application.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.android.application.presentation.utility.AppConstant
import java.util.*


open class RestaurantListPRQ(
    @SerializedName("location") var location: String? = "",
    @SerializedName("radius") var radius: String? = "",
    @SerializedName("type") var type: String? = "",
    @SerializedName("key") var key: String? = "",
    @SerializedName("keyword") var keyword: String? = "",
    @SerializedName("pagetoken") var pagetoken: String? = ""
)

open class BaseResponse(
    @Expose
    @SerializedName("status")
    var status: String? = "",
    @Expose
    @SerializedName("message")
    var message: String? = ""
) {
    fun isSuccess(): Boolean {
        return status.equals("OK")
    }
}

data class RestaurantListRS(
    @SerializedName("html_attributions")
    var htmlAttributions: List<Any>? = listOf(),
    @SerializedName("next_page_token")
    var nextPageToken: String? = "",
    @SerializedName("results")
    var results: List<Restaurant>? = listOf()
) : BaseResponse()

data class Restaurant(
    @SerializedName("business_status")
    var businessStatus: String? = "",
    @SerializedName("geometry")
    var geometry: Geometry? = Geometry(),
    @SerializedName("icon")
    var icon: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("opening_hours")
    var openingHours: OpeningHours? = OpeningHours(),
    @SerializedName("photos")
    var photos: List<Photo>? = listOf(),
    @SerializedName("place_id")
    var placeId: String? = "",
    @SerializedName("plus_code")
    var plusCode: PlusCode? = PlusCode(),
    @SerializedName("price_level")
    var priceLevel: Int? = 0,
    @SerializedName("rating")
    var rating: Double? = 0.0,
    @SerializedName("reference")
    var reference: String? = "",
    @SerializedName("scope")
    var scope: String? = "",
    @SerializedName("types")
    var types: List<String>? = listOf(),
    @SerializedName("user_ratings_total")
    var userRatingsTotal: Int? = 0,
    @SerializedName("vicinity")
    var vicinity: String? = "",
    @SerializedName("permanently_closed")
    var permanentlyClosed: Boolean? = false
) {
    fun getRestaurantFoodTypes(): String {
        val foodTypes: MutableList<String> = arrayListOf()
        types?.forEach {
            val a = it.replace("_", " ")
            foodTypes.add(a.capitalize(Locale.getDefault()))
        }
        return foodTypes.joinToString()
    }
}

data class Geometry(
    @SerializedName("location")
    var location: Location? = Location(),
    @SerializedName("viewport")
    var viewport: Viewport? = Viewport()
)

data class OpeningHours(
    @SerializedName("open_now")
    var openNow: Boolean? = false
)

data class Photo(
    @SerializedName("height")
    var height: Int? = 0,
    @SerializedName("html_attributions")
    var htmlAttributions: List<String>? = listOf(),
    @SerializedName("photo_reference")
    var photoReference: String? = "",
    @SerializedName("width")
    var width: Int? = 0
)

data class PlusCode(
    @SerializedName("compound_code") var compoundCode: String? = "",
    @SerializedName("global_code") var globalCode: String? = ""
)

data class Location(
    @SerializedName("lat") var lat: Double? = 0.0,
    @SerializedName("lng") var lng: Double? = 0.0
)

data class Viewport(
    @SerializedName("northeast") var northeast: Northeast? = Northeast(),
    @SerializedName("southwest") var southwest: Southwest? = Southwest()
)

data class Northeast(
    @SerializedName("lat") var lat: Double? = 0.0,
    @SerializedName("lng") var lng: Double? = 0.0
)

data class Southwest(
    @SerializedName("lat") var lat: Double? = 0.0,
    @SerializedName("lng") var lng: Double? = 0.0
)

data class CityWeatherPRQ(
    var cityName: String,
    var appId: String,
    var unit: String,
    var cnt: String? = ""
)

data class CityWeather(
    @SerializedName("coord") var coord: Coord? = null,
    @SerializedName("weather") var weather: List<Weather>? = listOf(),
    @SerializedName("base") var base: String? = "",
    @SerializedName("main") var main: Main? = null,
    @SerializedName("visibility") var visibility: Int? = 0,
    @SerializedName("wind") var wind: Wind? = null,
    @SerializedName("clouds") var clouds: Clouds? = null,
    @SerializedName("dt") var dt: Int? = 0,
    @SerializedName("sys") var sys: Sys? = Sys(),
    @SerializedName("timezone") var timezone: Int? = 0,
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("name") var name: String? = "",
    @SerializedName("cod") var cod: Int? = 0,
    @SerializedName("pop") var pop: Int? = 0,
    @SerializedName("dt_txt") var dtTxt: String? = "",
    @SerializedName("country") var country: String? = ""
) {
    fun getCountryName() : String {
        var strCountry = ""
        if(country?.isNotEmpty() == true){
            strCountry = ", ".plus(country)
        }
        return strCountry
    }
}

data class Coord(
    @SerializedName("lon") var lon: Double? = 0.0,
    @SerializedName("lat") var lat: Double? = 0.0
)

data class Weather(
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("main") var main: String? = "",
    @SerializedName("description") var description: String? = "",
    @SerializedName("icon") var icon: String? = ""
)

data class Main(
    @SerializedName("temp") var temp: Double? = 0.0,
    @SerializedName("feels_like") var feelsLike: Double? = 0.0,
    @SerializedName("temp_min") var tempMin: Double? = 0.0,
    @SerializedName("temp_max") var tempMax: Double? = 0.0,
    @SerializedName("pressure") var pressure: Int? = 0,
    @SerializedName("humidity") var humidity: Int? = 0,
    @SerializedName("sea_level") var seaLevel: Int? = 0,
    @SerializedName("grnd_level") var grndLevel: Int? = 0,
    @SerializedName("temp_kf") var tempKf: Double? = 0.0
)

data class Wind(
    @SerializedName("speed") var speed: Double? = 0.0,
    @SerializedName("deg") var deg: Int? = 0
)

data class Clouds(
    @SerializedName("all") var all: Int? = 0
)

data class Sys(
    @SerializedName("type") var type: Int? = 0,
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("country") var country: String? = "",
    @SerializedName("sunrise") var sunrise: Int? = 0,
    @SerializedName("sunset") var sunset: Int? = 0
)


data class CityWeatherListRS(
    @SerializedName("cod") var cod: String? = "",
    @SerializedName("message") var message: Int? = 0,
    @SerializedName("cnt") var cnt: Int? = 0,
    @SerializedName("list") var list: List<CityWeather>? = listOf(),
    @SerializedName("city") var city: City? = null
)

data class City(
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("name") var name: String? = "",
    @SerializedName("coord") var coord: Coord? = Coord(),
    @SerializedName("country") var country: String? = "",
    @SerializedName("population") var population: Int? = 0,
    @SerializedName("timezone") var timezone: Int? = 0,
    @SerializedName("sunrise") var sunrise: Int? = 0,
    @SerializedName("sunset") var sunset: Int? = 0
)


@Entity(tableName = AppConstant.TABLE_NAME_CITY)
data class CityItem(

    @PrimaryKey(autoGenerate = false)
    @SerializedName(AppConstant.id) var id: Int = 0,

    @ColumnInfo(name = AppConstant.name)
    @SerializedName(AppConstant.name) var name: String? = "",

    @ColumnInfo(name = AppConstant.lat)
    @SerializedName(AppConstant.lat) var lat: String? = "",

    @ColumnInfo(name = AppConstant.lng)
    @SerializedName(AppConstant.lng) var lng: String? = ""
)


