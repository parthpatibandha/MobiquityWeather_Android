package org.codejudge.application.domain.network

import org.codejudge.application.data.models.RestaurantListRS
import retrofit2.http.GET
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

}