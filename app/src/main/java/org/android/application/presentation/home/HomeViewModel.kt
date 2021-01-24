package org.android.application.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.launch
import org.android.application.data.contract.HomeRepo
import org.android.application.data.models.*
import org.android.application.presentation.core.BaseViewModel
import org.android.application.presentation.utility.AppConstant
import org.android.application.presentation.utility.PrefKey

class HomeViewModel constructor(val homeRepo: HomeRepo) : BaseViewModel() {

    var keyWord : String? = ""

    var bookmarkUpdateLiveData : MutableLiveData<Boolean> = MutableLiveData()
    var cityListLiveData: MutableLiveData<LiveData<List<CityItem>>> = MutableLiveData()
    var cityWeatherRSLiveData : MutableLiveData<CityWeather> = MutableLiveData()
    var cityWeatherForecastListRSLiveData: MutableLiveData<CityWeatherListRS> = MutableLiveData()

    fun getCityListLocal() {
        launch {
            postValue(homeRepo.getCityListLocal(), cityListLiveData)
        }
    }

    fun bookmarkCity(cityItem: CityItem){
        launch {
            postValue(homeRepo.bookmarkCity(cityItem), bookmarkUpdateLiveData)
        }
    }

    fun removeFromBookmarkCity(cityItem: CityItem){
        launch {
            postValue(homeRepo.removeFromBookmarkCity(cityItem), bookmarkUpdateLiveData)
        }
    }

    fun getTodayWeatherByCity(cityName : String, appId : String){
        val unit = Prefs.getString(PrefKey.PREF_UNIT, AppConstant.UnitMetric)
        val cityWeatherPRQ = CityWeatherPRQ(
            cityName,
            appId,
            unit
        )
        launch {
            postValue(homeRepo.getTodayWeatherByCity(cityWeatherPRQ), cityWeatherRSLiveData)
        }
    }

    fun getForecast(cityName : String, appId : String){
        val unit = Prefs.getString(PrefKey.PREF_UNIT, AppConstant.UnitMetric)
        val cityWeatherPRQ = CityWeatherPRQ(
            cityName,
            appId,
            unit,
            AppConstant.FiveDays
        )
        launch {
            postValue(homeRepo.getForecast(cityWeatherPRQ), cityWeatherForecastListRSLiveData)
        }
    }

}