package one.mann.weatherman.framework.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class SharedPreferenceStorage(private val context: Context) {

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
        const val FORECAST_DAY_1 = "FORECAST_DAY_1"
        const val FORECAST_CODE_1 = "FORECAST_CODE_1"
        const val FORECAST_MAX_1 = "FORECAST_MAX_1"
        const val FORECAST_MIN_1 = "FORECAST_MIN_1"
        const val FORECAST_DAY_2 = "FORECAST_DAY_2"
        const val FORECAST_CODE_2 = "FORECAST_CODE_2"
        const val FORECAST_MAX_2 = "FORECAST_MAX_2"
        const val FORECAST_MIN_2 = "FORECAST_MIN_2"
        const val FORECAST_DAY_3 = "FORECAST_DAY_3"
        const val FORECAST_CODE_3 = "FORECAST_CODE_3"
        const val FORECAST_MAX_3 = "FORECAST_MAX_3"
        const val FORECAST_MIN_3 = "FORECAST_MIN_3"
        const val FORECAST_DAY_4 = "FORECAST_DAY_4"
        const val FORECAST_CODE_4 = "FORECAST_CODE_4"
        const val FORECAST_MAX_4 = "FORECAST_MAX_4"
        const val FORECAST_MIN_4 = "FORECAST_MIN_4"
        const val FORECAST_DAY_5 = "FORECAST_DAY_5"
        const val FORECAST_CODE_5 = "FORECAST_CODE_5"
        const val FORECAST_MAX_5 = "FORECAST_MAX_5"
        const val FORECAST_MIN_5 = "FORECAST_MIN_5"
        const val FORECAST_DAY_6 = "FORECAST_DAY_6"
        const val FORECAST_CODE_6 = "FORECAST_CODE_6"
        const val FORECAST_MAX_6 = "FORECAST_MAX_6"
        const val FORECAST_MIN_6 = "FORECAST_MIN_6"
        const val FORECAST_DAY_7 = "FORECAST_DAY_7"
        const val FORECAST_CODE_7 = "FORECAST_CODE_7"
        const val FORECAST_MAX_7 = "FORECAST_MAX_7"
        const val FORECAST_MIN_7 = "FORECAST_MIN_7"
        const val UI_VISIBILITY = "UI_VISIBILITY"
        const val UPDATE_ALL = "UPDATE_ALL"
        const val SUN_POSITION = "SUN_POSITION"
    }

    val weatherPreferences: SharedPreferences = context.getSharedPreferences("WEATHER_DATA", Context.MODE_PRIVATE)
    val loadingBar: Boolean
        get() = weatherPreferences.getBoolean(LOADING_BAR, false)
    val uiVisibility: Boolean
        get() = weatherPreferences.getBoolean(UI_VISIBILITY, false)
    val cityCount: Int
        get() = weatherPreferences.getInt(COUNT, 1)
    val updateAll: Boolean
        get() = weatherPreferences.getBoolean(UPDATE_ALL, false)

    fun cityPref(name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    suspend fun getWeatherData(key: String, preferences: SharedPreferences): String? {
        return withContext(Dispatchers.IO) { preferences.getString(key, "") }
    }

    fun getSunPosition(preferences: SharedPreferences): Float = preferences.getFloat(SUN_POSITION, 0.00f)

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
}