package one.mann.weatherman.api

import android.content.Context
import android.widget.Toast

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

import one.mann.weatherman.R
import one.mann.weatherman.data.WeatherData
import one.mann.weatherman.model.openWeatherMap.Clouds
import one.mann.weatherman.model.openWeatherMap.Main
import one.mann.weatherman.model.openWeatherMap.Sys
import one.mann.weatherman.model.openWeatherMap.CurrentWeather
import one.mann.weatherman.model.openWeatherMap.Weather
import one.mann.weatherman.model.openWeatherMap.Wind
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherResult(private val context: Context) {
    private val weatherData: WeatherData = WeatherData(context)
    private val dateFormat: DateFormat
    private val hoursFormat: DateFormat

    init {
        dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        hoursFormat = SimpleDateFormat(HOURS_PATTERN, Locale.getDefault())
        hoursFormat.timeZone = TimeZone.getTimeZone("UTC") // Remove time offset
    }

    fun weatherCall(geoCoordinates: Array<Double?>) {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        val openWeatherMapApi = retrofit.create<OpenWeatherMapApi>(OpenWeatherMapApi::class.java)
        val weatherCall = openWeatherMapApi.getWeather(geoCoordinates[0], geoCoordinates[1], UNITS, APP_ID)

        weatherCall.enqueue(object : Callback<CurrentWeather> {
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                weatherData.saveLoadingBar(false)
                if (!response.isSuccessful) {
                    Toast.makeText(context, R.string.server_not_found, Toast.LENGTH_SHORT).show()
                    return
                }
                val currentWeather = response.body() ?: return
                saveWeather(currentWeather.main, currentWeather.sys, currentWeather.wind,
                        currentWeather.clouds, currentWeather.weather!![0], geoCoordinates,
                        currentWeather.name, currentWeather.dt, currentWeather.visibility.toLong())
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                weatherData.saveLoadingBar(false)
                Toast.makeText(context, R.string.server_not_found, Toast.LENGTH_SHORT).show()
            }
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

    private fun saveWeather(main: Main?, sys: Sys?, wind: Wind?, clouds: Clouds?, weather: Weather?, location: Array<Double?>,
                            name: String?, dt: Long, visibility: Long) {
        val coordinates = location[0].toString() + ", " + location[1].toString()
        val editor = weatherData.preferences.edit()
        editor.putString(WeatherData.CURRENT_TEMP, main?.temp.toString() + CELSIUS)
        editor.putString(WeatherData.MAX_TEMP, main?.temp_max?.toInt().toString() + CELSIUS)
        editor.putString(WeatherData.MIN_TEMP, main?.temp_min?.toInt().toString() + CELSIUS)
        editor.putString(WeatherData.PRESSURE, main?.pressure?.toInt().toString() + HECTOPASCAL)
        editor.putString(WeatherData.HUMIDITY, main?.humidity?.toInt().toString() + PERCENT)
        editor.putString(WeatherData.LOCATION, coordinates)
        editor.putString(WeatherData.LATITUDE, location[0].toString())
        editor.putString(WeatherData.LONGITUDE, location[1].toString())
        editor.putString(WeatherData.CITY_NAME, name)
        editor.putString(WeatherData.LAST_UPDATED, dateFormat.format(Date(dt * 1000)).toString())
        editor.putString(WeatherData.LAST_CHECKED, dateFormat.format(Date(System.currentTimeMillis())).toString())
        editor.putString(WeatherData.SUNRISE, dateFormat.format(Date(sys!!.sunrise * 1000)).toString())
        editor.putString(WeatherData.SUNSET, dateFormat.format(Date(sys!!.sunset * 1000)).toString())
        editor.putString(WeatherData.DAY_LENGTH, lengthOfDay(sys!!.sunrise, sys.sunset))
        editor.putString(WeatherData.COUNTRY_FLAG, countryCodeToEmoji(sys.country.toString()))
        editor.putString(WeatherData.CLOUDS, clouds?.all?.toInt().toString() + PERCENT)
        editor.putString(WeatherData.WIND_SPEED, wind?.speed.toString() + METERS_PER_SECOND)
        editor.putString(WeatherData.WIND_DIRECTION, wind?.deg?.toInt().toString() + DEGREES)
        editor.putString(WeatherData.VISIBILITY, visibility.toInt().toString() + METERS)
        editor.putString(WeatherData.DESCRIPTION, weather?.main.toString())
        editor.putString(WeatherData.ICON_CODE, ICON_URL + weather?.icon.toString() + ICON_EXTENSION)
        editor.putBoolean(WeatherData.UI_VISIBILITY, true)
        editor.apply()
    }

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        private const val ICON_URL = "http://openweathermap.org/img/w/"
        private const val APP_ID = "bd7173aa3aec6c2d8f88b500666a116e"
        private const val DATE_PATTERN = "d MMM, h:mm aa"
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
}