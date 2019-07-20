package one.mann.weatherman.application.di.module.framework

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.sources.DatabaseDataSource
import one.mann.interactors.data.sources.DeviceLocationSource
import one.mann.interactors.data.sources.PreferencesDataSource
import one.mann.weatherman.framework.data.database.WeatherDbSource
import one.mann.weatherman.framework.data.location.LocationDataSource
import one.mann.weatherman.framework.data.preferences.SettingsDataSource

@Module
internal abstract class FrameworkDataSourceModule {

    @Binds
    abstract fun bindDbDataSource(weatherDbSource: WeatherDbSource): DatabaseDataSource

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: LocationDataSource): DeviceLocationSource

    @Binds
    abstract fun bindSettingsDataSource(settingsDataSource: SettingsDataSource): PreferencesDataSource
}