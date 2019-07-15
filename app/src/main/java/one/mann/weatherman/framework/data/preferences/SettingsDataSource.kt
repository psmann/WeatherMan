package one.mann.weatherman.framework.data.preferences

import android.content.SharedPreferences
import one.mann.interactors.data.source.PreferencesDataSource
import javax.inject.Inject

internal class SettingsDataSource @Inject constructor(private val preferences: SharedPreferences) : PreferencesDataSource {

    companion object {
        const val UNITS_KEY = "units"
        private const val UNITS_DEFAULT = "metric"
    }

    override suspend fun getUnits(): String = preferences.getString(UNITS_KEY, UNITS_DEFAULT)!!
}