package one.mann.weatherman.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import one.mann.weatherman.WeatherManApp
import one.mann.weatherman.common.SETTINGS_UNITS_DEFAULT
import one.mann.weatherman.common.SETTINGS_UNITS_KEY
import javax.inject.Inject
import javax.inject.Singleton

/* Created by Psmann. */

@Module
internal class WeatherManAppModule @Inject constructor(private val application: WeatherManApp) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideDefaultPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context).apply {
            if (getString(SETTINGS_UNITS_KEY, "")!! == "") edit {
                putString(SETTINGS_UNITS_KEY, SETTINGS_UNITS_DEFAULT)
            }
        }
    }

    @Provides
    @Singleton
    fun provideWorkManager(context: Context): WorkManager = WorkManager.getInstance(context)
}