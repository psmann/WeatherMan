package one.mann.weatherman.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_weather.*

import one.mann.weatherman.R
import one.mann.weatherman.data.WeatherData
import one.mann.weatherman.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObjects()
    }

    private fun initObjects() {
        weather_layout.visibility = View.INVISIBLE
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        weatherViewModel.weatherLiveData.observe(this, Observer { weatherData ->
            current_temp_result.text = weatherData!!.getWeatherData(WeatherData.CURRENT_TEMP)
            feels_like_result.text = weatherData.getWeatherData(WeatherData.FEELS_LIKE)
            max_temp_result.text = weatherData.getWeatherData(WeatherData.MAX_TEMP)
            min_temp_result.text = weatherData.getWeatherData(WeatherData.MIN_TEMP)
            pressure_result.text = weatherData.getWeatherData(WeatherData.PRESSURE)
            humidity_result.text = weatherData.getWeatherData(WeatherData.HUMIDITY)
            location_result.text = weatherData.getWeatherData(WeatherData.LOCATION)
            last_checked_result.text = weatherData.getWeatherData(WeatherData.LAST_CHECKED)
            last_updated_result.text = weatherData.getWeatherData(WeatherData.LAST_UPDATED)
            city_name.text = weatherData.getWeatherData(WeatherData.CITY_NAME)
            country_flag.text = weatherData.getWeatherData(WeatherData.COUNTRY_FLAG)
            sunrise_result.text = weatherData.getWeatherData(WeatherData.SUNRISE)
            sunset_result.text = weatherData.getWeatherData(WeatherData.SUNSET)
            day_length_result.text = weatherData.getWeatherData(WeatherData.DAY_LENGTH)
            clouds_result.text = weatherData.getWeatherData(WeatherData.CLOUDS)
            wind_speed_result.text = weatherData.getWeatherData(WeatherData.WIND_SPEED)
            wind_direction_result.text = weatherData.getWeatherData(WeatherData.WIND_DIRECTION)
            visibility_result.text = weatherData.getWeatherData(WeatherData.VISIBILITY)
            description.text = weatherData.getWeatherData(WeatherData.DESCRIPTION)
            GlideApp.with(this)
                    .load(weatherData.getWeatherData(WeatherData.ICON_CODE))
                    .skipMemoryCache(true)
                    .into(weather_icon)
        })
        weatherViewModel.displayUi.observe(this, Observer { aBoolean ->
            if (aBoolean!! && weather_layout.visibility == View.INVISIBLE)
                weather_layout.visibility = View.VISIBLE
        })
    }


}
