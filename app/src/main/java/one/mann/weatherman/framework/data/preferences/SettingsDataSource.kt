package one.mann.weatherman.framework.data.preferences

import android.content.SharedPreferences
import one.mann.interactors.data.sources.PreferencesDataSource
import one.mann.weatherman.ui.common.util.LAST_UPDATED_KEY
import one.mann.weatherman.ui.common.util.SETTINGS_UNITS_KEY
import javax.inject.Inject

internal class SettingsDataSource @Inject constructor(private val preferences: SharedPreferences) : PreferencesDataSource {

    companion object {
        private const val UNITS_DEFAULT = "metric"
    }

    override suspend fun getUnits(): String = preferences.getString(SETTINGS_UNITS_KEY, UNITS_DEFAULT)!!

    override suspend fun getLastChecked(): Long = preferences.getLong(LAST_UPDATED_KEY, 0L)
}