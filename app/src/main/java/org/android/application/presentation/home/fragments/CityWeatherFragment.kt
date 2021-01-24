package org.android.application.presentation.home.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixplicity.easyprefs.library.Prefs
import org.android.application.R
import org.android.application.data.models.CityItem
import org.android.application.data.models.CityWeather
import org.android.application.databinding.FragmentCityWeatherBinding
import org.android.application.presentation.core.BaseActivity
import org.android.application.presentation.core.BaseFragment
import org.android.application.presentation.home.HomeViewModel
import org.android.application.presentation.home.adapter.CityWeatherListAdapter
import org.android.application.presentation.utility.*
import org.android.application.presentation.utility.DateTimeHelper.Companion.getFormattedDatetime
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.collections.HashMap

class CityWeatherFragment : BaseFragment() {

    private val homeViewModel: HomeViewModel by sharedViewModel()

    lateinit var binding: FragmentCityWeatherBinding

    private var adapter: CityWeatherListAdapter? = null

    private var cityWeather: CityWeather? = null
    private var cityItem: CityItem? = null
    private var isBookmark = false
    var unit = Prefs.getString(PrefKey.PREF_UNIT, AppConstant.UnitMetric)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        setupToolbar()
        attachObserver()
        setupAdapter()
        setupData()
        checkForBackground()
        getForecast()

        binding.ivBookmark.setOnClickListener {
            performBookmarkAction()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setTitle(getString(R.string.add_location))
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.ctlLayout.setExpandedTitleColor(Color.TRANSPARENT)
        binding.ctlLayout.isTitleEnabled = false
    }

    private fun attachObserver() {
        homeViewModel.bookmarkUpdateLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                it?.apply {
                    checkCityIsBookmarkedOrNot()
                }
            })

        homeViewModel.cityWeatherForecastListRSLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                it?.apply {
                    (activity as BaseActivity).hideProgress()
                    setForcastData(this.list.orEmpty())
                }
            })
    }

    private fun checkForBackground() {
        val calendar = Calendar.getInstance()
        val time: Int = calendar.get(Calendar.HOUR_OF_DAY)
        if (time >= 6 && time < 18) {
            binding.cardView.setBackgroundResource(R.drawable.color_morning)
        } else {
            binding.cardView.setBackgroundResource(R.drawable.color_evening)
        }
    }

    private fun setupData() {
        cityWeather = homeViewModel.cityWeatherRSLiveData.value
        cityWeather?.apply {
            binding.toolbar.setTitle(this.name.orEmpty())
            binding.tvName.text = (this.name.orEmpty()).plus(this.getCountryName())
            checkCityIsBookmarkedOrNot()

            cityItem = CityItem(
                this.id ?: 0,
                this.name,
                this.coord?.lat.toString(),
                this.coord?.lon.toString()
            )

            binding.tvDate.text =
                this.dt.toString().getFormattedDatetime(DateTimeHelper.DDMMMYYYYHHMMA)


            var feelsLike = ""
            if(unit == AppConstant.UnitMetric) {
                binding.tvTemprature.text = String.format(
                    context!!.getString(R.string.temp_with_degree),
                    this.main?.temp?.toInt().toString()
                )
                feelsLike = String.format(
                    context!!.getString(R.string.temp_with_degree),
                    this.main?.feelsLike?.toInt().toString()
                )
            } else {
                binding.tvTemprature.text = String.format(
                    context!!.getString(R.string.temp_with_ferenhit),
                    this.main?.temp?.toInt().toString()
                )
                feelsLike = String.format(
                    context!!.getString(R.string.temp_with_ferenhit),
                    this.main?.feelsLike?.toInt().toString()
                )
            }


            binding.tvTempratureLike.text = String.format(
                getString(R.string.feel_like), feelsLike
            )
            binding.tvDescription.text = this.weather?.get(0)?.main.orEmpty()
            binding.ivIcon.loadImage(activity!!.getWeatherPhotoUrl(this.weather?.get(0)?.icon.orEmpty()))

            binding.tvWind.text = this.wind?.speed.toString().orEmpty()
            binding.tvHumidity.text = this.main?.humidity?.toString().orEmpty()
            binding.tvSunrise.text =
                this.sys?.sunrise.toString().getFormattedDatetime(DateTimeHelper.HHMMA)
            binding.tvSunset.text =
                this.sys?.sunset.toString().getFormattedDatetime(DateTimeHelper.HHMMA)

        }
    }

    private fun setForcastData(list : List<CityWeather>){
        val filteredList : MutableList<CityWeather> = arrayListOf()
        val map : HashMap<String, CityWeather> = hashMapOf()
        list.forEach {
            val date = it.dt.toString().getFormattedDatetime(DateTimeHelper.MMMMMDD)
            if(!map.containsKey(date)){
                map.put(date, it)
                filteredList.add(it)
            }
        }
        if(filteredList.size > AppConstant.FiveDays.toInt()) {
            filteredList.removeAt(0)
        }
        adapter?.setData(filteredList)
    }

    private fun setupAdapter() {
        adapter = CityWeatherListAdapter(activity!!) {

        }
        binding.recyclerView.adapter = adapter
    }

    private fun getForecast() {
        homeViewModel.getForecast(
            cityWeather?.name.orEmpty(),
            getString(R.string.open_weather_map_api)
        )
    }

    private fun checkCityIsBookmarkedOrNot() {
        homeViewModel.cityListLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.apply {
                    this.find {
                        it.name == cityWeather?.name
                    }.apply {
                        if (this != null) {
                            isBookmark = true
                            binding.ivBookmark.isSelected = true
                        } else {
                            isBookmark = false
                            binding.ivBookmark.isSelected = false
                        }
                    }
                }
            })
        })
    }

    private fun performBookmarkAction() {
        if (isBookmark) {
            homeViewModel.removeFromBookmarkCity(cityItem!!)
        } else {
            homeViewModel.bookmarkCity(cityItem!!)
        }
    }

}