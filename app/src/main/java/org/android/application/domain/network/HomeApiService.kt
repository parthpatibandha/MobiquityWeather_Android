package org.android.application.domain.network

import org.android.application.data.models.CityWeather
import org.android.application.data.models.CityWeatherListRS
import org.android.application.data.models.RestaurantListRS
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeApiService {

    @GET("place/nearbysearch/json")
    suspend fun getNearByRestaurant(
        @Query("location") location: String,
        @Query("radius") nearbyradius: String,
        @Query("type") searchType: String,
        @Query("key") mapKey: String,
        @Query("keyword") keyword: String? = "",
        @Query("pagetoken") pagetoken: String? = ""
    ): RestaurantListRS

    @POST("weather")
    suspend fun getTodayWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): CityWeather

    @POST("forecast")
    suspend fun getForecast(
        @Query("q") cityName: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): CityWeatherListRS

}