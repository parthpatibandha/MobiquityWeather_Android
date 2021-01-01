package org.codejudge.application.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
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
    fun getRestaurantFoodTypes() : String {
        val foodTypes : MutableList<String> = arrayListOf()
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