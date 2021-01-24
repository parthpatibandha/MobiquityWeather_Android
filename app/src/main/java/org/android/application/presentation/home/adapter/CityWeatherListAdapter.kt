package org.android.application.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pixplicity.easyprefs.library.Prefs
import org.android.application.R
import org.android.application.data.models.CityWeather
import org.android.application.databinding.CellCityWeatherItemBinding
import org.android.application.presentation.core.BaseRecyclerViewAdapter
import org.android.application.presentation.utility.*
import org.android.application.presentation.utility.DateTimeHelper.Companion.getFormattedDatetime

class CityWeatherListAdapter(val context: Context, val onClick: (cityItem: CityWeather) -> Unit) :
    BaseRecyclerViewAdapter<CityWeather>() {

    var unit = Prefs.getString(PrefKey.PREF_UNIT, AppConstant.UnitMetric)

    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CellCityWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindData(holder: RecyclerView.ViewHolder?, item: CityWeather?, position: Int) {
        if (holder != null && item != null) {
            val viewHolder = holder as ViewHolder
            viewHolder.bindData(item)
        }
    }

    inner class ViewHolder(private val itemBinding: CellCityWeatherItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {

        }

        fun bindData(item: CityWeather) {
            itemBinding.tvDate.text =
                item.dt.toString().getFormattedDatetime(DateTimeHelper.DDMMMYYYYHHMMA)

            var feelsLike = ""
            if(unit == AppConstant.UnitMetric) {
                itemBinding.tvTemprature.text = String.format(
                    context.getString(R.string.temp_with_degree),
                    item.main?.temp?.toInt().toString()
                )
                feelsLike = String.format(
                    context.getString(R.string.temp_with_degree),
                    item.main?.feelsLike?.toInt().toString()
                )
            } else {
                itemBinding.tvTemprature.text = String.format(
                    context.getString(R.string.temp_with_ferenhit),
                    item.main?.temp?.toInt().toString()
                )
                feelsLike = String.format(
                    context.getString(R.string.temp_with_ferenhit),
                    item.main?.feelsLike?.toInt().toString()
                )
            }
            itemBinding.tvTempratureLike.text = String.format(
                context.getString(R.string.feel_like), feelsLike
            )
            itemBinding.tvDescription.text = item.weather?.get(0)?.main.orEmpty()
            itemBinding.ivIcon.loadImage(context.getWeatherPhotoUrl(item.weather?.get(0)?.icon.orEmpty()))

            itemBinding.tvWind.text = item.wind?.speed.toString().orEmpty()
            itemBinding.tvHumidity.text = item.main?.humidity?.toString().orEmpty()
            itemBinding.tvSunrise.text =
                item.sys?.sunrise.toString().getFormattedDatetime(DateTimeHelper.HHMMA)
            itemBinding.tvSunset.text =
                item.sys?.sunset.toString().getFormattedDatetime(DateTimeHelper.HHMMA)
        }
    }
}