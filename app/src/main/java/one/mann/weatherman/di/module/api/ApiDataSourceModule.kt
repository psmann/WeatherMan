package one.mann.weatherman.di.module.api

import dagger.Binds
import dagger.Module
import one.mann.interactors.data.sources.TimezoneDataSource
import one.mann.interactors.data.sources.WeatherDataSource
import one.mann.weatherman.api.openweathermap.OwmDataSource
import one.mann.weatherman.api.teleport.TeleportDataSource

@Module
internal abstract class ApiDataSourceModule {

    @Binds
    abstract fun bindOwmDataSource(owmDataSource: OwmDataSource): WeatherDataSource

    @Binds
    abstract fun bindTeleportDataSource(teleportDataSource: TeleportDataSource): TimezoneDataSource
}