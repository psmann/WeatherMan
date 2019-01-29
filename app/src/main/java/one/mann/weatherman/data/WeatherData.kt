package one.mann.weatherman.data

import android.content.Context
import android.content.SharedPreferences

// TODO: make top level pref store number of cities only, save each city data in a separate pref with position (int) as name
class WeatherData(private val context: Context) {
    val weatherPreferences: SharedPreferences = context.getSharedPreferences("WEATHER_DATA", Context.MODE_PRIVATE)

    val loadingBar: Boolean
        get() = weatherPreferences.getBoolean(LOADING_BAR, false)

    val uiVisibility: Boolean
        get() = weatherPreferences.getBoolean(UI_VISIBILITY, false)

    val cityCount: Int
        get() = weatherPreferences.getInt(COUNT, 1)

    fun cityPref(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun getWeatherData(key: String, preferences: SharedPreferences): String { // TODO: add arg to open preferences for corresponding city
        return preferences.getString(key, "")
    }

    fun saveLoadingBar(value: Boolean) {
        val editor = weatherPreferences.edit()
        editor.putBoolean(LOADING_BAR, value)
        editor.apply()
    }

    fun setCityCount(value: Int) {
        val editor = weatherPreferences.edit()
        editor.putInt(COUNT, value)
        editor.apply()
    }

    companion object {
        const val COUNT = "COUNT"
        const val CURRENT_TEMP = "CURRENT_TEMP"
        const val FEELS_LIKE = "FEELS_LIKE"
        const val MAX_TEMP = "MAX_TEMP"
        const val MIN_TEMP = "MIN_TEMP"
        const val PRESSURE = "PRESSURE"
        const val HUMIDITY = "HUMIDITY"
        const val LOCATION = "LOCATION"
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"
        const val CITY_NAME = "CITY_NAME"
        const val LAST_CHECKED = "LAST_CHECKED"
        const val LAST_UPDATED = "LAST_UPDATED"
        const val LOADING_BAR = "LOADING_BAR"
        const val SUNRISE = "SUNRISE"
        const val SUNSET = "SUNSET"
        const val DAY_LENGTH = "DAY_LENGTH"
        const val COUNTRY_FLAG = "COUNTRY_FLAG"
        const val CLOUDS = "CLOUDS"
        const val WIND_SPEED = "WIND_SPEED"
        const val WIND_DIRECTION = "WIND_DIRECTION"
        const val VISIBILITY = "VISIBILITY"
        const val DESCRIPTION = "DESCRIPTION"
        const val ICON_CODE = "ICON_CODE"
        const val UI_VISIBILITY = "UI_VISIBILITY"
    }
}