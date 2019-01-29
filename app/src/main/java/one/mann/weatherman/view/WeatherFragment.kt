package one.mann.weatherman.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
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

    private lateinit var weatherViewModel: WeatherViewModel
    private var position = 1

    companion object {
        private const val POSITION = "POSITION"
        @JvmStatic
        fun newInstance(position: Int) = WeatherFragment().apply {
            arguments = Bundle().apply { putInt(POSITION, position) }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getInt(POSITION)?.let { position = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObjects(position)
    }

    private fun initObjects(position: Int) {
        weather_layout.visibility = View.INVISIBLE
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        weatherViewModel.weatherLiveData.observe(this, Observer { weatherData ->
            current_temp_result.text = weatherData!!.getWeatherData(WeatherData.CURRENT_TEMP, weatherData.cityPref(position.toString()))
            feels_like_result.text = weatherData.getWeatherData(WeatherData.FEELS_LIKE, weatherData.cityPref(position.toString()))
            max_temp_result.text = weatherData.getWeatherData(WeatherData.MAX_TEMP, weatherData.cityPref(position.toString()))
            min_temp_result.text = weatherData.getWeatherData(WeatherData.MIN_TEMP, weatherData.cityPref(position.toString()))
            pressure_result.text = weatherData.getWeatherData(WeatherData.PRESSURE, weatherData.cityPref(position.toString()))
            humidity_result.text = weatherData.getWeatherData(WeatherData.HUMIDITY, weatherData.cityPref(position.toString()))
            location_result.text = weatherData.getWeatherData(WeatherData.LOCATION, weatherData.cityPref(position.toString()))
            last_checked_result.text = weatherData.getWeatherData(WeatherData.LAST_CHECKED, weatherData.cityPref(position.toString()))
            last_updated_result.text = weatherData.getWeatherData(WeatherData.LAST_UPDATED, weatherData.cityPref(position.toString()))
            city_name.text = weatherData.getWeatherData(WeatherData.CITY_NAME, weatherData.cityPref(position.toString()))
            country_flag.text = weatherData.getWeatherData(WeatherData.COUNTRY_FLAG, weatherData.cityPref(position.toString()))
            sunrise_result.text = weatherData.getWeatherData(WeatherData.SUNRISE, weatherData.cityPref(position.toString()))
            sunset_result.text = weatherData.getWeatherData(WeatherData.SUNSET, weatherData.cityPref(position.toString()))
            day_length_result.text = weatherData.getWeatherData(WeatherData.DAY_LENGTH, weatherData.cityPref(position.toString()))
            clouds_result.text = weatherData.getWeatherData(WeatherData.CLOUDS, weatherData.cityPref(position.toString()))
            wind_speed_result.text = weatherData.getWeatherData(WeatherData.WIND_SPEED, weatherData.cityPref(position.toString()))
            wind_direction_result.text = weatherData.getWeatherData(WeatherData.WIND_DIRECTION, weatherData.cityPref(position.toString()))
            visibility_result.text = weatherData.getWeatherData(WeatherData.VISIBILITY, weatherData.cityPref(position.toString()))
            description.text = weatherData.getWeatherData(WeatherData.DESCRIPTION, weatherData.cityPref(position.toString()))
            GlideApp.with(this)
                    .load(weatherData.getWeatherData(WeatherData.ICON_CODE, weatherData.cityPref(position.toString())))
                    .skipMemoryCache(true)
                    .into(weather_icon)
        })
        weatherViewModel.displayUi.observe(this, Observer { aBoolean ->
            if (aBoolean!! && weather_layout.visibility == View.INVISIBLE)
                weather_layout.visibility = View.VISIBLE
        })
    }
}
