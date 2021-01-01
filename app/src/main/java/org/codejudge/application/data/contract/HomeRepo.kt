package org.codejudge.application.data.contract

import com.keffys.android.domain.exceptions.MyException
import org.codejudge.application.data.Either
import org.codejudge.application.data.models.RestaurantListPRQ
import org.codejudge.application.data.models.RestaurantListRS

interface HomeRepo {
    suspend fun getNearByRestaurant(restaurantListPRQ: RestaurantListPRQ) : Either<MyException, RestaurantListRS>
}