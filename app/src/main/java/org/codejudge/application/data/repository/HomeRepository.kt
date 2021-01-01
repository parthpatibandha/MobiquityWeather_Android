package org.codejudge.application.data.repository

import com.keffys.android.domain.exceptions.MyException
import org.codejudge.application.data.BaseRepository
import org.codejudge.application.data.Either
import org.codejudge.application.data.contract.HomeRepo
import org.codejudge.application.data.models.RestaurantListPRQ
import org.codejudge.application.data.models.RestaurantListRS
import org.codejudge.application.domain.network.HomeApiService

class HomeRepository constructor(
    private val homeApiService: HomeApiService
) : HomeRepo, BaseRepository() {

    override suspend fun getNearByRestaurant(restaurantListPRQ: RestaurantListPRQ): Either<MyException, RestaurantListRS> {
        return either {
            homeApiService.getNearByRestaurant(
                restaurantListPRQ.location.orEmpty(),
                restaurantListPRQ.radius.orEmpty(),
                restaurantListPRQ.type.orEmpty(),
                restaurantListPRQ.key.orEmpty(),
                restaurantListPRQ.keyword.orEmpty(),
                restaurantListPRQ.pagetoken.orEmpty()
            )
        }
    }
}