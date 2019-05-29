package one.mann.weatherman.ui.mainactivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import one.mann.weatherman.R
import one.mann.weatherman.framework.data.SharedPreferenceStorage
import one.mann.weatherman.ui.GlideApp
import one.mann.weatherman.ui.mainactivity.viewholder.*

internal class CityRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var sharedPreferenceStorage: SharedPreferenceStorage
    private var fragmentPosition = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        0 -> WeatherCity(LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_city, parent, false))
        1 -> WeatherMain(LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_main, parent, false))
        2 -> WeatherSun(LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_sun, parent, false))
        3 -> WeatherClouds(LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_clouds, parent, false))
        else -> WeatherForecast(LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_forecast, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            when (holder) {
                is WeatherCity -> {
                    holder.cityName.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.CITY_NAME
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.lastChecked.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.LAST_CHECKED
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.location.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.LOCATION
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.flagIcon.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.COUNTRY_FLAG
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                }
                is WeatherMain -> {
                    holder.lastUpdated.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.LAST_UPDATED
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.humidity.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.HUMIDITY
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.minTemp.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.MIN_TEMP
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.maxTemp.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.MAX_TEMP
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.feelsLike.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FEELS_LIKE
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.currentTemp.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.CURRENT_TEMP
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.description.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.DESCRIPTION
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    GlideApp.with(holder.itemView)
                            .load(sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.ICON_CODE
                                    , sharedPreferenceStorage.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.weatherIcon)
                }
                is WeatherSun -> {
                    holder.dayLength.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.DAY_LENGTH
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.sunrise.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.SUNRISE
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.sunset.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.SUNSET
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.sunGraphView.updateView(sharedPreferenceStorage.getSunPosition(
                            sharedPreferenceStorage.cityPref(fragmentPosition.toString())))
                }
                is WeatherClouds -> {
                    holder.visibility.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.VISIBILITY
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.pressure.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.PRESSURE
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.clouds.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.CLOUDS
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.windSpeed.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.WIND_SPEED
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.windDirection.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.WIND_DIRECTION
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                }
                is WeatherForecast -> {
                    holder.forecast1Day.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_DAY_1
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast1Min.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MIN_1
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast1Max.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MAX_1
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast2Day.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_DAY_2
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast2Min.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MIN_2
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast2Max.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MAX_2
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast3Day.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_DAY_3
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast3Min.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MIN_3
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast3Max.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MAX_3
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast4Day.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_DAY_4
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast4Min.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MIN_4
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast4Max.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MAX_4
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast5Day.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_DAY_5
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast5Min.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MIN_5
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast5Max.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MAX_5
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast6Day.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_DAY_6
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast6Min.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MIN_6
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast6Max.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MAX_6
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast7Day.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_DAY_7
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast7Min.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MIN_7
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    holder.forecast7Max.text = sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_MAX_7
                            , sharedPreferenceStorage.cityPref(fragmentPosition.toString()))
                    GlideApp.with(holder.itemView)
                            .load(sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_CODE_1
                                    , sharedPreferenceStorage.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast1Icon)
                    GlideApp.with(holder.itemView)
                            .load(sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_CODE_2
                                    , sharedPreferenceStorage.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast2Icon)
                    GlideApp.with(holder.itemView)
                            .load(sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_CODE_3
                                    , sharedPreferenceStorage.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast3Icon)
                    GlideApp.with(holder.itemView)
                            .load(sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_CODE_4
                                    , sharedPreferenceStorage.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast4Icon)
                    GlideApp.with(holder.itemView)
                            .load(sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_CODE_5
                                    , sharedPreferenceStorage.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast5Icon)
                    GlideApp.with(holder.itemView)
                            .load(sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_CODE_6
                                    , sharedPreferenceStorage.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast6Icon)
                    GlideApp.with(holder.itemView)
                            .load(sharedPreferenceStorage.getWeatherData(SharedPreferenceStorage.FORECAST_CODE_7
                                    , sharedPreferenceStorage.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast7Icon)
                }
            }
        }
    }

    override fun getItemCount(): Int = 5

    override fun getItemViewType(position: Int): Int = position

    fun bindData(sharedPreferenceStorage: SharedPreferenceStorage, fragmentPosition: Int) {
        this.sharedPreferenceStorage = sharedPreferenceStorage
        this.fragmentPosition = fragmentPosition
        notifyDataSetChanged()
    }
}