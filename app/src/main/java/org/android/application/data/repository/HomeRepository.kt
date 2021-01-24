package org.android.application.data.repository

import androidx.lifecycle.LiveData
import com.keffys.android.domain.exceptions.MyException
import org.android.application.data.BaseRepository
import org.android.application.data.Either
import org.android.application.data.contract.HomeRepo
import org.android.application.data.database.AppDao
import org.android.application.data.models.*
import org.android.application.domain.network.HomeApiService

class HomeRepository constructor(
    private val homeApiService: HomeApiService,
    private val appDao: AppDao
) : HomeRepo, BaseRepository() {

    override suspend fun getCityListLocal(): Either<MyException, LiveData<List<CityItem>>> {
        return either {
            appDao.getCityListLocal()
        }
    }

    override suspend fun bookmarkCity(cityItem: CityItem): Either<MyException, Boolean> {
        return either {
            appDao.insertCity(cityItem)
            return@either true
        }
    }

    override suspend fun removeFromBookmarkCity(cityItem: CityItem): Either<MyException, Boolean> {
        return either {
            appDao.deleteCity(cityItem)
            return@either true
        }
    }

    override suspend fun getTodayWeatherByCity(cityWeatherPRQ: CityWeatherPRQ): Either<MyException, CityWeather> {
        return either {
            homeApiService.getTodayWeatherByCity(
                cityWeatherPRQ.cityName,
                cityWeatherPRQ.appId,
                cityWeatherPRQ.unit
            )
        }
    }

    override suspend fun getForecast(cityWeatherPRQ: CityWeatherPRQ): Either<MyException, CityWeatherListRS> {
        return either {
            homeApiService.getForecast(
                cityWeatherPRQ.cityName,
                cityWeatherPRQ.appId,
                cityWeatherPRQ.unit
            )
        }
    }
}