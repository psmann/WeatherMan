package one.mann.weatherman.api.openweathermap

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import one.mann.weatherman.api.Keys
import one.mann.weatherman.api.teleport.TeleportResult
import one.mann.weatherman.framework.data.CELSIUS
import one.mann.weatherman.framework.data.DEGREES
import one.mann.weatherman.framework.data.HECTOPASCAL
import one.mann.weatherman.framework.data.ICON_EXTENSION
import one.mann.weatherman.framework.data.METERS
import one.mann.weatherman.framework.data.METERS_PER_SECOND
import one.mann.weatherman.framework.data.PERCENT
import one.mann.weatherman.framework.data.UNITS
import one.mann.weatherman.framework.data.countryCodeToEmoji
import one.mann.weatherman.framework.data.feelsLike
import one.mann.weatherman.framework.data.lengthOfDay
import one.mann.weatherman.framework.data.sharedprefs.WeatherSharedPref
import one.mann.weatherman.framework.data.sunPositionBias
import one.mann.weatherman.framework.data.timeInMinutes
import one.mann.weatherman.framework.data.unixToDate
import one.mann.weatherman.framework.data.unixToDay
import one.mann.weatherman.framework.data.unixToTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.math.roundToInt

internal class OwmResult(private val weatherSharedPref: WeatherSharedPref) : TeleportResult.TimeZoneListener {

    private var savePrefCounter: Int = 0
    private val tzResult: TeleportResult = TeleportResult(this)

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        private const val ICON_URL = "http://openweathermap.org/img/w/"
    }

    fun weatherCall(geoCoordinates: Array<Double?>, cityPref: String, failedCallback: () -> Unit) {
        val shortCoords: Array<Double> = arrayOf(String.format("%.2f", geoCoordinates[0]).toDouble()
                , String.format("%.2f", geoCoordinates[1]).toDouble())
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val openWeatherMapApi = retrofit.create<OwmService>(OwmService::class.java)
        val weatherCall = openWeatherMapApi.getWeather(shortCoords[0], shortCoords[1], UNITS, Keys.APP_ID)

        weatherCall.enqueue(object : Callback<DtoWeather> {
            override fun onResponse(call: Call<DtoWeather>, response: Response<DtoWeather>) {
                if (!response.isSuccessful) {
                    weatherSharedPref.saveLoadingBar(false)
                    failedCallback()
                    return
                }
                val currentWeather = response.body()
                if (currentWeather == null) {
                    weatherSharedPref.saveLoadingBar(false)
                    return
                }
                GlobalScope.launch(Dispatchers.IO) {
                    saveWeather(cityPref, currentWeather, geoCoordinates)
                }
                forecastCall(shortCoords, cityPref)
                tzResult.getTimeZone(shortCoords[0], shortCoords[1], cityPref, currentWeather)
                weatherSharedPref.saveLoadingBar(false)
            }

            override fun onFailure(call: Call<DtoWeather>, t: Throwable) {
                weatherSharedPref.saveLoadingBar(false)
                failedCallback()
            }
        })
    }

    private fun forecastCall(geoCoordinates: Array<Double>, cityPref: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val openWeatherMapApi = retrofit.create<OwmService>(OwmService::class.java)
        val forecastCall = openWeatherMapApi.getForecast(geoCoordinates[0], geoCoordinates[1], UNITS, Keys.APP_ID)

        forecastCall.enqueue(object : Callback<DtoDailyForecast> {
            override fun onResponse(call: Call<DtoDailyForecast>, response: Response<DtoDailyForecast>) {
                if (!response.isSuccessful) return
                val dailyForecast = response.body() ?: return
                GlobalScope.launch(Dispatchers.IO) {
                    saveMaxMin(cityPref, dailyForecast.list[0].temp.min, dailyForecast.list[0].temp.max)
                    if (dailyForecast.list.size == 7) for (i in 0..6)
                        saveForecast(i, cityPref, dailyForecast.list[i].temp.min, dailyForecast.list[i].temp.max,
                                dailyForecast.list[i].weather[0].icon, dailyForecast.list[i].dt)
                }
            }

            override fun onFailure(call: Call<DtoDailyForecast>, t: Throwable) {}
        })
    }

    private fun saveWeather(cityPref: String, cw: DtoWeather, location: Array<Double?>) {
        val coordinates = String.format("%.4f", location[0]) + ", " + String.format("%.4f", location[1])
        val cityDataEditor = weatherSharedPref.cityPref(cityPref).edit()

        cityDataEditor.putString(WeatherSharedPref.CURRENT_TEMP, cw.main.temp.toString() + CELSIUS)
        cityDataEditor.putString(WeatherSharedPref.FEELS_LIKE, feelsLike(cw.main.temp, cw.main.humidity.toInt(),
                cw.wind.speed).toString() + CELSIUS)
        cityDataEditor.putString(WeatherSharedPref.PRESSURE, cw.main.pressure.toInt().toString() + HECTOPASCAL)
        cityDataEditor.putString(WeatherSharedPref.HUMIDITY, cw.main.humidity.toInt().toString() + PERCENT)
        cityDataEditor.putString(WeatherSharedPref.LOCATION, coordinates)
        cityDataEditor.putString(WeatherSharedPref.LATITUDE, location[0].toString())
        cityDataEditor.putString(WeatherSharedPref.LONGITUDE, location[1].toString())
        cityDataEditor.putString(WeatherSharedPref.CITY_NAME, cw.name)
        cityDataEditor.putString(WeatherSharedPref.LAST_UPDATED, unixToDate(cw.dt * 1000))
        cityDataEditor.putString(WeatherSharedPref.LAST_CHECKED, unixToDate(System.currentTimeMillis()))
        cityDataEditor.putString(WeatherSharedPref.SUNRISE,
                unixToTime(cw.sys.sunrise * 1000))
        cityDataEditor.putString(WeatherSharedPref.SUNSET,
                unixToTime(cw.sys.sunset * 1000))
        cityDataEditor.putString(WeatherSharedPref.DAY_LENGTH, lengthOfDay(cw.sys.sunrise, cw.sys.sunset))
        cityDataEditor.putString(WeatherSharedPref.COUNTRY_FLAG, countryCodeToEmoji(cw.sys.country))
        cityDataEditor.putString(WeatherSharedPref.CLOUDS, cw.clouds.all.toInt().toString() + PERCENT)
        cityDataEditor.putString(WeatherSharedPref.WIND_SPEED, cw.wind.speed.toString() + METERS_PER_SECOND)
        cityDataEditor.putString(WeatherSharedPref.WIND_DIRECTION, cw.wind.deg.toInt().toString() + DEGREES)
        cityDataEditor.putString(WeatherSharedPref.VISIBILITY, cw.visibility.toInt().toString() + METERS)
        cityDataEditor.putString(WeatherSharedPref.DESCRIPTION, cw.weather[0].main)
        cityDataEditor.putString(WeatherSharedPref.ICON_CODE, ICON_URL + cw.weather[0].icon + ICON_EXTENSION)
        cityDataEditor.apply()
        if (!weatherSharedPref.uiVisibility) {
            val uiDataEditor = weatherSharedPref.mainPref.edit()
            uiDataEditor.putBoolean(WeatherSharedPref.UI_VISIBILITY, true)
            uiDataEditor.apply()
        }
        updateSharedPrefListener()
    }

    private fun saveMaxMin(cityPref: String, min: Float, max: Float) {
        val cityDataEditor = weatherSharedPref.cityPref(cityPref).edit()
        cityDataEditor.putString(WeatherSharedPref.MIN_TEMP, min.toString() + CELSIUS)
        cityDataEditor.putString(WeatherSharedPref.MAX_TEMP, max.toString() + CELSIUS)
        cityDataEditor.apply()
        updateSharedPrefListener()
    }

    private fun saveForecast(dayCount: Int, cityPref: String, min: Float, max: Float,
                             icon: String, date: Long) {
        val cityDataEditor = weatherSharedPref.cityPref(cityPref).edit()
        when (dayCount) {
            0 -> {
                cityDataEditor.putString(WeatherSharedPref.FORECAST_CODE_1, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherSharedPref.FORECAST_DAY_1, unixToDay(date * 1000))
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MIN_1, min.roundToInt().toString())
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MAX_1, max.roundToInt().toString())
            }
            1 -> {
                cityDataEditor.putString(WeatherSharedPref.FORECAST_CODE_2, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherSharedPref.FORECAST_DAY_2, unixToDay(date * 1000))
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MIN_2, min.roundToInt().toString())
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MAX_2, max.roundToInt().toString())
            }
            2 -> {
                cityDataEditor.putString(WeatherSharedPref.FORECAST_CODE_3, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherSharedPref.FORECAST_DAY_3, unixToDay(date * 1000))
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MIN_3, min.roundToInt().toString())
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MAX_3, max.roundToInt().toString())
            }
            3 -> {
                cityDataEditor.putString(WeatherSharedPref.FORECAST_CODE_4, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherSharedPref.FORECAST_DAY_4, unixToDay(date * 1000))
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MIN_4, min.roundToInt().toString())
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MAX_4, max.roundToInt().toString())
            }
            4 -> {
                cityDataEditor.putString(WeatherSharedPref.FORECAST_CODE_5, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherSharedPref.FORECAST_DAY_5, unixToDay(date * 1000))
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MIN_5, min.roundToInt().toString())
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MAX_5, max.roundToInt().toString())
            }
            5 -> {
                cityDataEditor.putString(WeatherSharedPref.FORECAST_CODE_6, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherSharedPref.FORECAST_DAY_6, unixToDay(date * 1000))
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MIN_6, min.roundToInt().toString())
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MAX_6, max.roundToInt().toString())
            }
            6 -> {
                cityDataEditor.putString(WeatherSharedPref.FORECAST_CODE_7, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherSharedPref.FORECAST_DAY_7, unixToDay(date * 1000))
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MIN_7, min.roundToInt().toString())
                cityDataEditor.putString(WeatherSharedPref.FORECAST_MAX_7, max.roundToInt().toString())
            }
        }
        cityDataEditor.apply()
        updateSharedPrefListener()
    }

    private fun updateSharedPrefListener() { // Update livedata only after all shared preferences have been saved
        if (savePrefCounter == 9) {
            val uiDataEditor = weatherSharedPref.mainPref.edit()
            uiDataEditor.putBoolean(WeatherSharedPref.UPDATE_ALL, !weatherSharedPref.updateAll)
            uiDataEditor.apply()
            savePrefCounter = 0
        } else savePrefCounter++
    }

    override fun saveTimeZoneValue(tz: String, cityPref: String, dto: DtoWeather) {
        val cityDataEditor = weatherSharedPref.cityPref(cityPref).edit()
        cityDataEditor.putString(WeatherSharedPref.LAST_UPDATED,
                unixToDate(dto.dt * 1000, TimeZone.getTimeZone(tz)))
        cityDataEditor.putString(WeatherSharedPref.LAST_CHECKED,
                unixToDate(System.currentTimeMillis(), TimeZone.getTimeZone(tz)))
        cityDataEditor.putString(WeatherSharedPref.SUNRISE,
                unixToTime(dto.sys.sunrise * 1000, TimeZone.getTimeZone(tz)))
        cityDataEditor.putString(WeatherSharedPref.SUNSET,
                unixToTime(dto.sys.sunset * 1000, TimeZone.getTimeZone(tz)))
        cityDataEditor.putFloat(WeatherSharedPref.SUN_POSITION,
                sunPositionBias(timeInMinutes((dto.sys.sunrise * 1000), tz),
                        timeInMinutes((dto.sys.sunset * 1000), tz),
                        timeInMinutes(System.currentTimeMillis(), tz)))
        cityDataEditor.apply()
        updateSharedPrefListener()
    }
}