package one.mann.weatherman.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import one.mann.weatherman.R
import one.mann.weatherman.data.WeatherData
import one.mann.weatherman.view.GlideApp
import one.mann.weatherman.view.viewholder.*

class CityRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var weatherData: WeatherData
    private var fragmentPosition = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
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
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            when (holder) {
                is WeatherCity -> {
                    holder.cityName.text = weatherData.getWeatherData(WeatherData.CITY_NAME
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.lastChecked.text = weatherData.getWeatherData(WeatherData.LAST_CHECKED
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.location.text = weatherData.getWeatherData(WeatherData.LOCATION
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.flagIcon.text = weatherData.getWeatherData(WeatherData.COUNTRY_FLAG
                            , weatherData.cityPref(fragmentPosition.toString()))
                }
                is WeatherMain -> {
                    holder.lastUpdated.text = weatherData.getWeatherData(WeatherData.LAST_UPDATED
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.humidity.text = weatherData.getWeatherData(WeatherData.HUMIDITY
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.minTemp.text = weatherData.getWeatherData(WeatherData.MIN_TEMP
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.maxTemp.text = weatherData.getWeatherData(WeatherData.MAX_TEMP
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.feelsLike.text = weatherData.getWeatherData(WeatherData.FEELS_LIKE
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.currentTemp.text = weatherData.getWeatherData(WeatherData.CURRENT_TEMP
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.description.text = weatherData.getWeatherData(WeatherData.DESCRIPTION
                            , weatherData.cityPref(fragmentPosition.toString()))
                    GlideApp.with(holder.itemView)
                            .load(weatherData.getWeatherData(WeatherData.ICON_CODE
                                    , weatherData.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.weatherIcon)
                }
                is WeatherSun -> {
                    holder.dayLength.text = weatherData.getWeatherData(WeatherData.DAY_LENGTH
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.sunrise.text = weatherData.getWeatherData(WeatherData.SUNRISE
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.sunset.text = weatherData.getWeatherData(WeatherData.SUNSET
                            , weatherData.cityPref(fragmentPosition.toString()))
                }
                is WeatherClouds -> {
                    holder.visibility.text = weatherData.getWeatherData(WeatherData.VISIBILITY
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.pressure.text = weatherData.getWeatherData(WeatherData.PRESSURE
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.clouds.text = weatherData.getWeatherData(WeatherData.CLOUDS
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.windSpeed.text = weatherData.getWeatherData(WeatherData.WIND_SPEED
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.windDirection.text = weatherData.getWeatherData(WeatherData.WIND_DIRECTION
                            , weatherData.cityPref(fragmentPosition.toString()))
                }
                is WeatherForecast -> {
                    holder.forecast1Day.text = weatherData.getWeatherData(WeatherData.FORECAST_DAY_1
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast1Min.text = weatherData.getWeatherData(WeatherData.FORECAST_MIN_1
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast1Max.text = weatherData.getWeatherData(WeatherData.FORECAST_MAX_1
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast2Day.text = weatherData.getWeatherData(WeatherData.FORECAST_DAY_2
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast2Min.text = weatherData.getWeatherData(WeatherData.FORECAST_MIN_2
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast2Max.text = weatherData.getWeatherData(WeatherData.FORECAST_MAX_2
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast3Day.text = weatherData.getWeatherData(WeatherData.FORECAST_DAY_3
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast3Min.text = weatherData.getWeatherData(WeatherData.FORECAST_MIN_3
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast3Max.text = weatherData.getWeatherData(WeatherData.FORECAST_MAX_3
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast4Day.text = weatherData.getWeatherData(WeatherData.FORECAST_DAY_4
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast4Min.text = weatherData.getWeatherData(WeatherData.FORECAST_MIN_4
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast4Max.text = weatherData.getWeatherData(WeatherData.FORECAST_MAX_4
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast5Day.text = weatherData.getWeatherData(WeatherData.FORECAST_DAY_5
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast5Min.text = weatherData.getWeatherData(WeatherData.FORECAST_MIN_5
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast5Max.text = weatherData.getWeatherData(WeatherData.FORECAST_MAX_5
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast6Day.text = weatherData.getWeatherData(WeatherData.FORECAST_DAY_6
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast6Min.text = weatherData.getWeatherData(WeatherData.FORECAST_MIN_6
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast6Max.text = weatherData.getWeatherData(WeatherData.FORECAST_MAX_6
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast7Day.text = weatherData.getWeatherData(WeatherData.FORECAST_DAY_7
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast7Min.text = weatherData.getWeatherData(WeatherData.FORECAST_MIN_7
                            , weatherData.cityPref(fragmentPosition.toString()))
                    holder.forecast7Max.text = weatherData.getWeatherData(WeatherData.FORECAST_MAX_7
                            , weatherData.cityPref(fragmentPosition.toString()))
                    GlideApp.with(holder.itemView)
                            .load(weatherData.getWeatherData(WeatherData.FORECAST_CODE_1
                                    , weatherData.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast1Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherData.getWeatherData(WeatherData.FORECAST_CODE_2
                                    , weatherData.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast2Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherData.getWeatherData(WeatherData.FORECAST_CODE_3
                                    , weatherData.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast3Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherData.getWeatherData(WeatherData.FORECAST_CODE_4
                                    , weatherData.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast4Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherData.getWeatherData(WeatherData.FORECAST_CODE_5
                                    , weatherData.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast5Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherData.getWeatherData(WeatherData.FORECAST_CODE_6
                                    , weatherData.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast6Icon)
                    GlideApp.with(holder.itemView)
                            .load(weatherData.getWeatherData(WeatherData.FORECAST_CODE_7
                                    , weatherData.cityPref(fragmentPosition.toString())))
                            .skipMemoryCache(true)
                            .into(holder.forecast7Icon)
                }
            }
        }
    }

    override fun getItemCount(): Int = 5

    override fun getItemViewType(position: Int): Int = position

    fun bindData(weatherData: WeatherData, fragmentPosition: Int) {
        this.weatherData = weatherData
        this.fragmentPosition = fragmentPosition
        notifyDataSetChanged()
    }
}