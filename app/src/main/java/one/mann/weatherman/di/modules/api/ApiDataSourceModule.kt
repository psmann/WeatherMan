package one.mann.weatherman.di.modules.api

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.sources.api.TimezoneDataSource
import one.mann.interactors.data.sources.api.WeatherDataSource
import one.mann.weatherman.api.openweathermap.OwmWeatherDataSource
import one.mann.weatherman.api.teleport.TeleportTimezoneDataSource

/* Created by Psmann. */

@Module
internal abstract class ApiDataSourceModule {

    @Binds
    abstract fun bindWeatherDataSource(owmWeatherDataSource: OwmWeatherDataSource): WeatherDataSource

    @Binds
    abstract fun bindTimezoneDataSource(teleportTimezoneDataSource: TeleportTimezoneDataSource): TimezoneDataSource
}