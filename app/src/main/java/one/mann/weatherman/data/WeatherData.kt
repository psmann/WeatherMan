package one.mann.weatherman.data

import android.content.Context
import android.content.SharedPreferences

class WeatherData(context: Context) {
    val preferences: SharedPreferences

    val loadingBar: Boolean
        get() = preferences.getBoolean(LOADING_BAR, false)

    val uiVisibility: Boolean
        get() = preferences.getBoolean(UI_VISIBILITY, false)

    init {
        preferences = context.getSharedPreferences("WEATHER_DATA", Context.MODE_PRIVATE)
    }

    fun getWeatherData(key: String): String {
        return preferences.getString(key, "")
    }

    fun saveLoadingBar(value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(LOADING_BAR, value)
        editor.apply()
    }

    companion object {

        val CURRENT_TEMP = "CURRENT_TEMP"
        val MAX_TEMP = "MAX_TEMP"
        val MIN_TEMP = "MIN_TEMP"
        val PRESSURE = "PRESSURE"
        val HUMIDITY = "HUMIDITY"
        val LOCATION = "LOCATION"
        val LATITUDE = "LATITUDE"
        val LONGITUDE = "LONGITUDE"
        val CITY_NAME = "CITY_NAME"
        val LAST_CHECKED = "LAST_CHECKED"
        val LAST_UPDATED = "LAST_UPDATED"
        val LOADING_BAR = "LOADING_BAR"
        val SUNRISE = "SUNRISE"
        val SUNSET = "SUNSET"
        val DAY_LENGTH = "DAY_LENGTH"
        val COUNTRY_FLAG = "COUNTRY_FLAG"
        val CLOUDS = "CLOUDS"
        val WIND_SPEED = "WIND_SPEED"
        val WIND_DIRECTION = "WIND_DIRECTION"
        val VISIBILITY = "VISIBILITY"
        val DESCRIPTION = "DESCRIPTION"
        val ICON_CODE = "ICON_CODE"
        val UI_VISIBILITY = "UI_VISIBILITY"
    }
}