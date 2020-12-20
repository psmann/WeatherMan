package one.mann.weatherman.framework.data.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import one.mann.interactors.data.sources.framework.PreferencesDataSource
import one.mann.weatherman.ui.common.util.LAST_UPDATED_KEY
import one.mann.weatherman.ui.common.util.SETTINGS_UNITS_KEY
import javax.inject.Inject

/* Created by Psmann. */

internal class SettingsPreferencesDataSource @Inject constructor(private val preferences: SharedPreferences) : PreferencesDataSource {

    companion object {
        private const val UNITS = "metric"
    }

    override suspend fun getUnits(): String {
        if (preferences.getString(SETTINGS_UNITS_KEY, "")!! == "") preferences.edit { putString(SETTINGS_UNITS_KEY, UNITS) }
        return preferences.getString(SETTINGS_UNITS_KEY, UNITS)!!
    }

    override suspend fun getLastUpdated(): Long = preferences.getLong(LAST_UPDATED_KEY, 0L)
}