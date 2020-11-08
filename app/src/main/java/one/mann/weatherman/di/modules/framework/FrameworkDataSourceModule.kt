package one.mann.weatherman.di.modules.framework

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.sources.DatabaseDataSource
import one.mann.interactors.data.sources.DeviceLocationSource
import one.mann.interactors.data.sources.PreferencesDataSource
import one.mann.weatherman.framework.data.database.WeatherDbDataSource
import one.mann.weatherman.framework.data.location.LocationDataSource
import one.mann.weatherman.framework.data.preferences.SettingsDataSource

/* Created by Psmann. */

@Module
internal abstract class FrameworkDataSourceModule {

    @Binds
    abstract fun bindDbDataSource(weatherDbDataSource: WeatherDbDataSource): DatabaseDataSource

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: LocationDataSource): DeviceLocationSource

    @Binds
    abstract fun bindSettingsDataSource(settingsDataSource: SettingsDataSource): PreferencesDataSource
}