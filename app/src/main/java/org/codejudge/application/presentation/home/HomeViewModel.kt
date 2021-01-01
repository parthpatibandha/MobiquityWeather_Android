package org.codejudge.application.presentation.home

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.codejudge.application.data.contract.HomeRepo
import org.codejudge.application.data.models.RestaurantListPRQ
import org.codejudge.application.data.models.RestaurantListRS
import org.codejudge.application.presentation.core.BaseViewModel
import org.codejudge.application.presentation.utility.AppConstant

class HomeViewModel constructor(val homeRepo: HomeRepo) : BaseViewModel() {

    var restaurantListRSLiveData : MutableLiveData<RestaurantListRS> = MutableLiveData()

    var page = AppConstant.PageStart
    var isLoading = false
    var hasMore: String? = AppConstant.No

    fun getRestaurantList(key : String, keyword : String? = "", pageToken : String? = ""){
        val restaurantListPRQ = RestaurantListPRQ(
            AppConstant.Location,
            AppConstant.radius,
            AppConstant.type,
            key,
            keyword,
            pageToken
        )
        isLoading = true
        launch {
            postValue(homeRepo.getNearByRestaurant(restaurantListPRQ), restaurantListRSLiveData)
        }
    }
}