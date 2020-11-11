package one.mann.weatherman.di.modules.framework

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.sources.framework.DatabaseDataSource
import one.mann.interactors.data.sources.framework.DeviceLocationSource
import one.mann.interactors.data.sources.framework.PreferencesDataSource
import one.mann.weatherman.framework.data.database.WeatherDbDataSource
import one.mann.weatherman.framework.data.location.FusedLocationDataSource
import one.mann.weatherman.framework.data.preferences.SettingsPreferencesDataSource

/* Created by Psmann. */

@Module
internal abstract class FrameworkDataSourceModule {

    @Binds
    abstract fun bindDatabaseDataSource(weatherDbDataSource: WeatherDbDataSource): DatabaseDataSource

    @Binds
    abstract fun bindDeviceLocationDataSource(fusedLocationDataSource: FusedLocationDataSource): DeviceLocationSource

    @Binds
    abstract fun bindPreferencesDataSource(settingsPreferencesDataSource: SettingsPreferencesDataSource): PreferencesDataSource
}