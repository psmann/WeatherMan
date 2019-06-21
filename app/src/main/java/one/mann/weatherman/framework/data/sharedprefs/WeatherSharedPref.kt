package one.mann.weatherman.framework.data.sharedprefs

import android.content.Context
import android.content.SharedPreferences

internal class WeatherSharedPref(private val context: Context) {

    companion object {
        const val WEATHER_DATA = "WEATHER_DATA"
        const val COUNT = "COUNT"
        const val LOADING_BAR = "LOADING_BAR"
        const val UI_VISIBILITY = "UI_VISIBILITY"
    }

    val mainPref: SharedPreferences = context.getSharedPreferences(WEATHER_DATA, Context.MODE_PRIVATE)
    val loadingBar: Boolean
        get() = mainPref.getBoolean(LOADING_BAR, false)
    val uiVisibility: Boolean
        get() = mainPref.getBoolean(UI_VISIBILITY, false)
    val cityCount: Int
        get() = mainPref.getInt(COUNT, 1)

    fun saveLoadingBar(value: Boolean) {
        val editor = mainPref.edit()
        editor.putBoolean(LOADING_BAR, value)
        editor.apply()
    }

    fun setCityCount(value: Int) {
        val editor = mainPref.edit()
        editor.putInt(COUNT, value)
        editor.apply()
    }
}