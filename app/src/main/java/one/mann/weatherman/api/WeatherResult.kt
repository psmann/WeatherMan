package one.mann.weatherman.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import one.mann.weatherman.data.WeatherData
import one.mann.weatherman.model.openweathermap.forecastdaily.DailyForecast
import one.mann.weatherman.model.openweathermap.weather.CurrentWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.exp
import java.lang.Math.pow
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherResult(private val weatherData: WeatherData) : TimeZoneResult.TimeZoneListener {

    private var savePrefCounter: Int = 0
    private val dateFormat: DateFormat
    private val dayFormat: DateFormat
    private val hoursFormat: DateFormat
    private val tzResult: TimeZoneResult = TimeZoneResult(this)

    companion object {
        private const val BASE_URL_WEATHER = "http://api.openweathermap.org/data/2.5/"
        private const val BASE_URL_FORECAST = "http://api.openweathermap.org/data/2.5/forecast/"
        private const val ICON_URL = "http://openweathermap.org/img/w/"
        private const val DATE_PATTERN = "d MMM, h:mm aa"
        private const val DAY_PATTERN = "E"
        private const val HOURS_PATTERN = "H 'Hours and' m 'Minutes'"
        private const val UNITS = "metric"
        private const val ICON_EXTENSION = ".png"
        private const val CELSIUS = " C"
        private const val HECTOPASCAL = " hPa"
        private const val PERCENT = " %"
        private const val METERS = " m"
        private const val METERS_PER_SECOND = " m/s"
        private const val DEGREES = " °"
        private const val FLAG_OFFSET = 0x1F1E6 // Regional Indicator Symbol for letter A
        private const val ASCII_OFFSET = 0x41 // Uppercase letter A
    }

    init {
        dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        dayFormat = SimpleDateFormat(DAY_PATTERN, Locale.getDefault())
        hoursFormat = SimpleDateFormat(HOURS_PATTERN, Locale.getDefault())
        hoursFormat.timeZone = TimeZone.getTimeZone("UTC") // Removes time offset
    }

    fun weatherCall(geoCoordinates: Array<Double?>, cityPref: String, failedCallback: () -> Unit) {
        val shortCoords: Array<Double> = arrayOf(String.format("%.2f", geoCoordinates[0]).toDouble()
                , String.format("%.2f", geoCoordinates[1]).toDouble())
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_WEATHER)
                .addConverterFactory(GsonConverterFactory.create()).build()
        val openWeatherMapApi = retrofit.create<OpenWeatherMapApi>(OpenWeatherMapApi::class.java)
        val weatherCall = openWeatherMapApi.getWeather(shortCoords[0], shortCoords[1], UNITS, Keys.APP_ID)

        weatherCall.enqueue(object : Callback<CurrentWeather> {
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                if (!response.isSuccessful) {
                    weatherData.saveLoadingBar(false)
                    failedCallback()
                    return
                }
                val currentWeather = response.body()
                if (currentWeather == null) {
                    weatherData.saveLoadingBar(false)
                    return
                }
                GlobalScope.launch(Dispatchers.IO) {
                    saveWeather(cityPref, currentWeather, geoCoordinates)
                }
                forecastCall(shortCoords, cityPref)
                tzResult.getTimeZone(shortCoords[0], shortCoords[1], cityPref, currentWeather)
                weatherData.saveLoadingBar(false)
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                weatherData.saveLoadingBar(false)
                failedCallback()
            }
        })
    }

    private fun forecastCall(geoCoordinates: Array<Double>, cityPref: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_FORECAST)
                .addConverterFactory(GsonConverterFactory.create()).build()
        val openWeatherMapApi = retrofit.create<OpenWeatherMapApi>(OpenWeatherMapApi::class.java)
        val forecastCall = openWeatherMapApi.getForecast(geoCoordinates[0], geoCoordinates[1], UNITS, Keys.APP_ID)

        forecastCall.enqueue(object : Callback<DailyForecast> {
            override fun onResponse(call: Call<DailyForecast>, response: Response<DailyForecast>) {
                if (!response.isSuccessful) return
                val dailyForecast = response.body() ?: return
                GlobalScope.launch(Dispatchers.IO) {
                    saveMaxMin(cityPref, dailyForecast.list!![0].temp!!.min, dailyForecast.list!![0].temp!!.max)
                    if (dailyForecast.list!!.size == 7) for (i in 0..6)
                        saveForecast(i, cityPref, dailyForecast.list!![i].temp!!.min, dailyForecast.list!![i].temp!!.max,
                                dailyForecast.list!![i].weather!![0].icon!!, dailyForecast.list!![i].dt)
                }
            }

            override fun onFailure(call: Call<DailyForecast>, t: Throwable) {}
        })
    }

    private fun countryCodeToEmoji(code: String): String {
        val firstChar = Character.codePointAt(code, 0) - ASCII_OFFSET + FLAG_OFFSET
        val secondChar = Character.codePointAt(code, 1) - ASCII_OFFSET + FLAG_OFFSET
        return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
    }

    private fun lengthOfDay(sunrise: Long, sunset: Long): String {
        val length = sunset - sunrise
        return hoursFormat.format(Date(length * 1000)).toString() // Convert to nanosecond
    }

    private fun feelsLike(temperature: Float, humidity: Int, wind: Float): Float {
        // Using https://blog.metservice.com/FeelsLikeTemp for reference
        var feelsLike: Double
        when {
            temperature < 14 -> { // = Wind Chill using JAG/TI formula
                val k = pow(wind.toDouble() * 3.6, 0.16) // Wind speed converted to km/h and raised to the power
                feelsLike = 13.12 + (0.6215 * temperature) - (11.35 * k) + (0.396 * temperature * k)
                if (temperature > 10) // Roll-over Zone
                    feelsLike = temperature - (((temperature - feelsLike) * (14 - temperature)) / 4)
            }
            else -> { // = Heat Index or Apparent Temperature using Steadman's formula
                val e = humidity / 100 * 6.105 * exp(17.27 * temperature / (237.7 + temperature)) // Pressure
                feelsLike = temperature + (0.33 * e) - (0.7 * wind) - 4
            }
        }
        return BigDecimal.valueOf(feelsLike)
                .setScale(2, RoundingMode.HALF_UP).toFloat() // Set precision to match current temp
    }

    private fun saveWeather(cityPref: String, cw: CurrentWeather, location: Array<Double?>) {
        val coordinates = String.format("%.4f", location[0]) + ", " + String.format("%.4f", location[1])
        val cityDataEditor = weatherData.cityPref(cityPref).edit()
        cityDataEditor.putString(WeatherData.CURRENT_TEMP, cw.main?.temp.toString() + CELSIUS)
        cityDataEditor.putString(WeatherData.FEELS_LIKE, feelsLike(cw.main?.temp!!, cw.main.humidity.toInt(),
                cw.wind?.speed!!).toString() + CELSIUS)
        cityDataEditor.putString(WeatherData.PRESSURE, cw.main.pressure.toInt().toString() + HECTOPASCAL)
        cityDataEditor.putString(WeatherData.HUMIDITY, cw.main.humidity.toInt().toString() + PERCENT)
        cityDataEditor.putString(WeatherData.LOCATION, coordinates)
        cityDataEditor.putString(WeatherData.LATITUDE, location[0].toString())
        cityDataEditor.putString(WeatherData.LONGITUDE, location[1].toString())
        cityDataEditor.putString(WeatherData.CITY_NAME, cw.name)
        cityDataEditor.putString(WeatherData.LAST_UPDATED, dateFormat.format(Date(cw.dt * 1000)).toString())
        cityDataEditor.putString(WeatherData.LAST_CHECKED, dateFormat.format(Date(System.currentTimeMillis())).toString())
        cityDataEditor.putString(WeatherData.SUNRISE, dateFormat.format(Date(cw.sys!!.sunrise * 1000)).toString())
        cityDataEditor.putString(WeatherData.SUNSET, dateFormat.format(Date(cw.sys!!.sunset * 1000)).toString())
        cityDataEditor.putString(WeatherData.DAY_LENGTH, lengthOfDay(cw.sys!!.sunrise, cw.sys.sunset))
        cityDataEditor.putString(WeatherData.COUNTRY_FLAG, countryCodeToEmoji(cw.sys.country.toString()))
        cityDataEditor.putString(WeatherData.CLOUDS, cw.clouds?.all?.toInt().toString() + PERCENT)
        cityDataEditor.putString(WeatherData.WIND_SPEED, cw.wind.speed.toString() + METERS_PER_SECOND)
        cityDataEditor.putString(WeatherData.WIND_DIRECTION, cw.wind.deg.toInt().toString() + DEGREES)
        cityDataEditor.putString(WeatherData.VISIBILITY, cw.visibility.toInt().toString() + METERS)
        cityDataEditor.putString(WeatherData.DESCRIPTION, cw.weather!![0].main.toString())
        cityDataEditor.putString(WeatherData.ICON_CODE, ICON_URL + cw.weather[0].icon.toString() + ICON_EXTENSION)
        cityDataEditor.apply()
        if (!weatherData.uiVisibility) {
            val uiDataEditor = weatherData.weatherPreferences.edit()
            uiDataEditor.putBoolean(WeatherData.UI_VISIBILITY, true)
            uiDataEditor.apply()
        }
        updateSharedPrefListener()
    }

    private fun saveMaxMin(cityPref: String, min: Float, max: Float) {
        val cityDataEditor = weatherData.cityPref(cityPref).edit()
        cityDataEditor.putString(WeatherData.MIN_TEMP, min.toString() + CELSIUS)
        cityDataEditor.putString(WeatherData.MAX_TEMP, max.toString() + CELSIUS)
        cityDataEditor.apply()
        updateSharedPrefListener()
    }

    private fun saveForecast(dayCount: Int, cityPref: String, min: Float, max: Float,
                             icon: String, date: Long) {
        val cityDataEditor = weatherData.cityPref(cityPref).edit()
        when (dayCount) {
            0 -> {
                cityDataEditor.putString(WeatherData.FORECAST_CODE_1, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherData.FORECAST_DAY_1, dayFormat.format(Date(date * 1000)).toString())
                cityDataEditor.putString(WeatherData.FORECAST_MIN_1, min.roundToInt().toString())
                cityDataEditor.putString(WeatherData.FORECAST_MAX_1, max.roundToInt().toString())
            }
            1 -> {
                cityDataEditor.putString(WeatherData.FORECAST_CODE_2, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherData.FORECAST_DAY_2, dayFormat.format(Date(date * 1000)).toString())
                cityDataEditor.putString(WeatherData.FORECAST_MIN_2, min.roundToInt().toString())
                cityDataEditor.putString(WeatherData.FORECAST_MAX_2, max.roundToInt().toString())
            }
            2 -> {
                cityDataEditor.putString(WeatherData.FORECAST_CODE_3, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherData.FORECAST_DAY_3, dayFormat.format(Date(date * 1000)).toString())
                cityDataEditor.putString(WeatherData.FORECAST_MIN_3, min.roundToInt().toString())
                cityDataEditor.putString(WeatherData.FORECAST_MAX_3, max.roundToInt().toString())
            }
            3 -> {
                cityDataEditor.putString(WeatherData.FORECAST_CODE_4, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherData.FORECAST_DAY_4, dayFormat.format(Date(date * 1000)).toString())
                cityDataEditor.putString(WeatherData.FORECAST_MIN_4, min.roundToInt().toString())
                cityDataEditor.putString(WeatherData.FORECAST_MAX_4, max.roundToInt().toString())
            }
            4 -> {
                cityDataEditor.putString(WeatherData.FORECAST_CODE_5, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherData.FORECAST_DAY_5, dayFormat.format(Date(date * 1000)).toString())
                cityDataEditor.putString(WeatherData.FORECAST_MIN_5, min.roundToInt().toString())
                cityDataEditor.putString(WeatherData.FORECAST_MAX_5, max.roundToInt().toString())
            }
            5 -> {
                cityDataEditor.putString(WeatherData.FORECAST_CODE_6, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherData.FORECAST_DAY_6, dayFormat.format(Date(date * 1000)).toString())
                cityDataEditor.putString(WeatherData.FORECAST_MIN_6, min.roundToInt().toString())
                cityDataEditor.putString(WeatherData.FORECAST_MAX_6, max.roundToInt().toString())
            }
            6 -> {
                cityDataEditor.putString(WeatherData.FORECAST_CODE_7, ICON_URL + icon + ICON_EXTENSION)
                cityDataEditor.putString(WeatherData.FORECAST_DAY_7, dayFormat.format(Date(date * 1000)).toString())
                cityDataEditor.putString(WeatherData.FORECAST_MIN_7, min.roundToInt().toString())
                cityDataEditor.putString(WeatherData.FORECAST_MAX_7, max.roundToInt().toString())
            }
        }
        cityDataEditor.apply()
        updateSharedPrefListener()
    }

    private fun updateSharedPrefListener() { // Update livedata only after all shared preferences have been saved
        if (savePrefCounter == 9) {
            val uiDataEditor = weatherData.weatherPreferences.edit()
            uiDataEditor.putBoolean(WeatherData.UPDATE_ALL, !weatherData.updateAll)
            uiDataEditor.apply()
            savePrefCounter = 0
        } else savePrefCounter++
    }

    override fun saveTimeZoneValue(tz: String, cityPref: String, cw: CurrentWeather) {
        dateFormat.timeZone = TimeZone.getTimeZone(tz)
        val cityDataEditor = weatherData.cityPref(cityPref).edit()
        cityDataEditor.putString(WeatherData.LAST_UPDATED, dateFormat.format(Date(cw.dt * 1000)).toString())
        cityDataEditor.putString(WeatherData.LAST_CHECKED, dateFormat.format(Date(System.currentTimeMillis())).toString())
        cityDataEditor.putString(WeatherData.SUNRISE, dateFormat.format(Date(cw.sys!!.sunrise * 1000)).toString())
        cityDataEditor.putString(WeatherData.SUNSET, dateFormat.format(Date(cw.sys!!.sunset * 1000)).toString())
        cityDataEditor.apply()
        updateSharedPrefListener()
    }
}