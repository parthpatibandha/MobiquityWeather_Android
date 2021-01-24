package org.android.application.data.contract

import androidx.lifecycle.LiveData
import com.keffys.android.domain.exceptions.MyException
import org.android.application.data.Either
import org.android.application.data.models.*

interface HomeRepo {
    suspend fun getCityListLocal() : Either<MyException, LiveData<List<CityItem>>>
    suspend fun bookmarkCity(cityItem: CityItem) : Either<MyException, Boolean>
    suspend fun removeFromBookmarkCity(cityItem: CityItem): Either<MyException, Boolean>

    suspend fun getTodayWeatherByCity(cityWeatherPRQ: CityWeatherPRQ): Either<MyException, CityWeather>
    suspend fun getForecast(cityWeatherPRQ: CityWeatherPRQ): Either<MyException, CityWeatherListRS>
}