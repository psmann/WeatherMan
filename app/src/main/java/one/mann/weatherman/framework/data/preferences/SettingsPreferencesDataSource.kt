package one.mann.weatherman.framework.data.preferences

import android.content.SharedPreferences
import one.mann.interactors.data.sources.framework.PreferencesDataSource
import one.mann.weatherman.common.LAST_UPDATED_DEFAULT
import one.mann.weatherman.common.LAST_UPDATED_KEY
import one.mann.weatherman.common.SETTINGS_UNITS_DEFAULT
import one.mann.weatherman.common.SETTINGS_UNITS_KEY
import javax.inject.Inject

/* Created by Psmann. */

internal class SettingsPreferencesDataSource @Inject constructor(private val preferences: SharedPreferences) :
    PreferencesDataSource {

    override suspend fun getUnits(): String = preferences.getString(SETTINGS_UNITS_KEY, SETTINGS_UNITS_DEFAULT)!!

    override suspend fun getLastUpdated(): Long = preferences.getLong(LAST_UPDATED_KEY, LAST_UPDATED_DEFAULT)
}