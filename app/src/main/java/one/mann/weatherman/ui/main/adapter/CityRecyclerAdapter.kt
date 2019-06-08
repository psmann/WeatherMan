package one.mann.weatherman.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import one.mann.weatherman.R
import one.mann.weatherman.framework.data.sharedprefs.WeatherSharedPref
import one.mann.weatherman.ui.GlideApp
import one.mann.weatherman.ui.main.viewholder.*

internal class CityRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var weatherSharedPref: WeatherSharedPref
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
                    holder.cityName.text = weatherSharedPref.getWeatherData(WeatherSharedPref.CITY_NAME
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.lastChecked.text = weatherSharedPref.getWeatherData(WeatherSharedPref.LAST_CHECKED
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.location.text = weatherSharedPref.getWeatherData(WeatherSharedPref.LOCATION
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.flagIcon.text = weatherSharedPref.getWeatherData(WeatherSharedPref.COUNTRY_FLAG
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                }
                is WeatherMain -> {
                    holder.lastUpdated.text = weatherSharedPref.getWeatherData(WeatherSharedPref.LAST_UPDATED
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.humidity.text = weatherSharedPref.getWeatherData(WeatherSharedPref.HUMIDITY
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.minTemp.text = weatherSharedPref.getWeatherData(WeatherSharedPref.MIN_TEMP
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.maxTemp.text = weatherSharedPref.getWeatherData(WeatherSharedPref.MAX_TEMP
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.feelsLike.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FEELS_LIKE
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.currentTemp.text = weatherSharedPref.getWeatherData(WeatherSharedPref.CURRENT_TEMP
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.description.text = weatherSharedPref.getWeatherData(WeatherSharedPref.DESCRIPTION
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    GlideApp.with(holder.itemView)
                            .load(weatherSharedPref.getWeatherData(WeatherSharedPref.ICON_CODE
                                    , weatherSharedPref.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.weatherIcon)
                }
                is WeatherSun -> {
                    holder.dayLength.text = weatherSharedPref.getWeatherData(WeatherSharedPref.DAY_LENGTH
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.sunrise.text = weatherSharedPref.getWeatherData(WeatherSharedPref.SUNRISE
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.sunset.text = weatherSharedPref.getWeatherData(WeatherSharedPref.SUNSET
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.sunGraphView.updateView(weatherSharedPref.getSunPosition(
                            weatherSharedPref.cityPref(fragmentPosition.toString())))
                }
                is WeatherClouds -> {
                    holder.visibility.text = weatherSharedPref.getWeatherData(WeatherSharedPref.VISIBILITY
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.pressure.text = weatherSharedPref.getWeatherData(WeatherSharedPref.PRESSURE
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.clouds.text = weatherSharedPref.getWeatherData(WeatherSharedPref.CLOUDS
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.windSpeed.text = weatherSharedPref.getWeatherData(WeatherSharedPref.WIND_SPEED
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.windDirection.text = weatherSharedPref.getWeatherData(WeatherSharedPref.WIND_DIRECTION
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                }
                is WeatherForecast -> {
                    holder.forecast1Day.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_DAY_1
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast1Min.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MIN_1
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast1Max.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MAX_1
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast2Day.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_DAY_2
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast2Min.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MIN_2
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast2Max.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MAX_2
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast3Day.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_DAY_3
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast3Min.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MIN_3
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast3Max.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MAX_3
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast4Day.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_DAY_4
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast4Min.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MIN_4
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast4Max.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MAX_4
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast5Day.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_DAY_5
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast5Min.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MIN_5
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast5Max.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MAX_5
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast6Day.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_DAY_6
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast6Min.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MIN_6
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast6Max.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MAX_6
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast7Day.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_DAY_7
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast7Min.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MIN_7
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    holder.forecast7Max.text = weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_MAX_7
                            , weatherSharedPref.cityPref(fragmentPosition.toString()))
                    GlideApp.with(holder.itemView)
                            .load(weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_CODE_1
                                    , weatherSharedPref.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast1Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_CODE_2
                                    , weatherSharedPref.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast2Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_CODE_3
                                    , weatherSharedPref.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast3Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_CODE_4
                                    , weatherSharedPref.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast4Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_CODE_5
                                    , weatherSharedPref.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast5Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_CODE_6
                                    , weatherSharedPref.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast6Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherSharedPref.getWeatherData(WeatherSharedPref.FORECAST_CODE_7
                                    , weatherSharedPref.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast7Icon)
                }
            }
        }
    }

    override fun getItemCount(): Int = 5

    override fun getItemViewType(position: Int): Int = position

    fun bindData(weatherSharedPref: WeatherSharedPref, fragmentPosition: Int) {
        this.weatherSharedPref = weatherSharedPref
        this.fragmentPosition = fragmentPosition
        notifyDataSetChanged()
    }
}